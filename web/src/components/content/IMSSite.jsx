import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Table } from 'react-bootstrap';

function IMSSite() {
  const [items, setItems] = useState([]);
  const { siteId } = useParams();

  useEffect(() => {
    fetch(`http://localhost:8080/availabilities/site?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => {
        setItems(data);
      })
      .catch(error => console.error("Failed to fetch items", error));
  }, [siteId]);

  return (
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
        {/* creates a key by combining the siteId, itemId, and index which guarantee that each key is unique */}
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
  );
}

export default IMSSite;
