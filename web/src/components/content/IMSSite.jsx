import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Table } from 'react-bootstrap';
import 'leaflet/dist/leaflet.css';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';

/**
 * The IMSSite component is responsible for displaying detailed information about a specific site.
 * It includes a table listing items available at the site and a map with a marker showing the site's location.
 * The site's data is fetched based on the `siteId` obtained from the URL parameters.
 */
function IMSSite() {
  // State for storing the items fetched for the site
  const [items, setItems] = useState([]);
  
  // State for storing the parsed coordinates for the map marker
  const [coordinates, setCoordinates] = useState(null);
  
  // Extracts `siteId` from the URL parameters
  const { siteId } = useParams();

  // Fetches site data including items and location when the component mounts or `siteId` changes
  useEffect(() => {
    fetch(`http://localhost:8080/availabilities/site?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => {
        // Assumes the first item contains site information including location
        if (data && data.length > 0) {
          setItems(data);
          const location = data[0].siteId.siteLocation; 
          const coords = parseLocation(location);
          if (coords) {
            setCoordinates([coords.latitude, coords.longitude]);
          }
        }
      })
      .catch(error => console.error("Failed to fetch items", error));
  }, [siteId]);

  /**
   * Parses a location string into latitude and longitude.
   * @param {string} input - The location string in 'latitude longitude' format.
   * @returns An object with latitude and longitude or null if the format is invalid.
   */
  function parseLocation(input) {
    const regex = /^([-+]?\d{1,2}(\.\d+)?)[ ]([-+]?\d{1,3}(\.\d+)?)$/;
    const match = input.match(regex);
    if (match) {
      const latitude = parseFloat(match[1]);
      const longitude = parseFloat(match[3]);
      return { latitude, longitude };
    } else {
      console.error("Invalid location format or out of range.");
      return null;
    }
  }

  // Component render
  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Item ID</th>
            <th>Item Name</th>
            <th>Item Price</th>
            <th>Quantity Available</th>
            <th>Site Name</th>
            <th>Location</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {items.map(({ itemId, quantity, siteId }, index) => (
            <tr key={`${siteId.siteId}-${itemId.itemId}-${index}`}>
              <td>{itemId.itemId}</td>
              <td>{itemId.itemName}</td>
              <td>${itemId.itemPrice.toFixed(2)}</td>
              <td>{quantity}</td>
              <td>{siteId.siteName}</td>
              <td>{siteId.siteLocation}</td>
              <td>{siteId.siteStatus}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      {coordinates && (
        <MapContainer center={coordinates} zoom={17} style={{ height: '400px', width: '100%' }}>
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          <Marker position={coordinates}>
            <Popup>
              {items[0].siteId.siteName} - {items[0].siteId.siteStatus}
            </Popup>
          </Marker>
        </MapContainer>
      )}
    </>
  );
}

export default IMSSite;
