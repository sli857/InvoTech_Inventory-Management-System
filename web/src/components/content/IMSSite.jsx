import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, Card, Col, Form, Row, Table, Dropdown, DropdownButton, Container, OverlayTrigger, Tooltip, } from 'react-bootstrap';
import 'leaflet/dist/leaflet.css';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';

const backend_baseurl = 'http://cs506-team-35.cs.wisc.edu:8080'

/**
 * The IMSSite component is responsible for displaying detailed information about a specific site.
 * It includes a table listing items available at the site and a map with a marker showing the site's location.
 * The site's data is fetched based on the `siteId` obtained from the URL parameters.
 */
function IMSSite() {

  // State for storing the items fetched for the site
  const [items, setItems] = useState([]);
  const [site, setSite] = useState([]);
  const [newId, setNewId] = useState([]);
  const [requestBody, setRequestBody] = useState();
  const [itemName, setItemName] = useState('');
  const [itemPrice, setItemPrice] = useState('');
  const [addQuantity, setQuantity] = useState('');
  const [itemID, setItemID] = useState('');
  const [changeQuantity, setChangeQuantity] = useState('');
  const [newQuantity, setNewQuantity] = useState('');
  const [allItems, setAllItems] = useState([]);
  const [addItem, setAddItem] = useState([]);

  // State for storing the parsed coordinates for the map marker
  const [coordinates, setCoordinates] = useState(null);

  // Extracts `siteId` from the URL parameters
  const { siteId } = useParams();

  // Fetches site data including items and location when the component mounts or `siteId` changes
  useEffect(() => {

    fetch(`${backend_baseurl}/availabilities/site?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => {
        // Assumes the first item contains site information including location
        if (data && data.length > 0) {
          setItems(data);
        }
      })
      .catch(error => console.error("Failed to fetch items", error));

    fetch(`${backend_baseurl}/sites/site?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => {
        setSite(data);
        if (data != null) {
          const location = data.siteLocation;
          const coords = parseLocation(location);
          if (coords) {
            setCoordinates([coords.latitude, coords.longitude]);
          }
        }
      }).catch(error => console.error("Failed to fetch site", error));

    //fetch all the items from the database
    fetch(`${backend_baseurl}/items`)
      .then(res => res.json())
      .then(data => {
        if (data != null) {
          setAllItems(data);
        }
      }).catch(error => console.error("Failed to fetch all items", error));


  }, [siteId]);

  /**
   * Reload the items
   * 
   */
  function loadItems() {

    fetch(`${backend_baseurl}/availabilities/site?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => {
        // Assumes the first item contains site information including location
        if (data && data.length > 0) {
          setItems(data);
        }
      })
      .catch(error => console.error("Failed to fetch items", error));
  }

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


  /**
  * Handles submission of the change quantity form. Validates input, sends data to the backend, and updates local state.
  * @param {React.FormEvent} event - The form submission event.
  */
  async function handleQuantityChange(event) {
    event.preventDefault();


  
    const res = await fetch(`${backend_baseurl}/availabilities/quantity`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        siteId: siteId,
        itemId: itemID,
        operation: changeQuantity,
        quantity: newQuantity
      }),
    });

    if (res.status === 200) {
      loadItems();
    }
  }


  /**
 * Handles submission of the add item form. Validates input, sends data to the backend, and updates local state.
 * @param {React.FormEvent} event - The form submission event.
 */
  async function handleAddItem(event) {
    event.preventDefault();

    if (addItem === "new item") {
      await fetch(`${backend_baseurl}items/add`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          itemName: itemName,
          itemPrice: itemPrice,
        }),
      })
        .then((itemResponse) => {
          if (!itemResponse.ok) {
            alert("Make sure the item you are trying to add doesn't already exist")
            throw new Error("Network response was not ok");
          }
          return itemResponse.json();
        })
        .then((responseData) => {
          setNewId(responseData.itemId)

          const body = {
            siteId: site,
            itemId: {
              itemId: responseData.itemId,
              itemName: itemName,
              itemPrice: itemPrice
            },
            quantity: addQuantity
          }
          setRequestBody(body)

          return body;
        }).then((body) => {

          

          fetch(`${backend_baseurl}/availabilities/add`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
          }).then((response) => {
            if (response.status === 200) {
              loadItems()
            }
          });
        });

    } else {
      fetch(`${backend_baseurl}/items/item?itemId=${addItem}`)
        .then((res) => {
          if (res.status === 200) {
            return res.json();
          }
        }).then((data) => {
          const body = {
            siteId: site,
            itemId: data,
            quantity: addQuantity
          }

          fetch(`${backend_baseurl}/availabilities/add`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
          }).then((response) => {
            if (response.status === 200) {
              loadItems()
            }
          });

        })
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
        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <MapContainer center={coordinates} zoom={17} style={{ height: '400px', width: '90%' }}>
            <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            <Marker position={coordinates}>
              <Popup>
                {site.siteName} - {site.siteStatus}
              </Popup>
            </Marker>
          </MapContainer>

        </div>
      )}

      <div style={{ display: 'flex', padding: 10 }}>


        <Col md={6} style={{ flex: 1, marginRight: '20px' }}>
          <Card>
            <Card.Body>
              <Card.Title>Add Item</Card.Title>
              <Form onSubmit={handleAddItem}>

                <Form.Group>
                  <Form.Label>Select Item</Form.Label>

                  <Form.Select
                    value={addItem}
                    onChange={(e) => {
                      setAddItem(e.target.value);
                    }}>
                    <option value="">Select the item you want to add to the site</option>
                    <option value="new item">Add a new item</option>
                    {allItems.map(option => (
                      <option key={option.itemId} value={option.itemId}>{option.itemName}</option>
                    ))}


                  </Form.Select>
                </Form.Group>
                {
                  addItem === "new item" && (
                    <Form.Group>
                      <Form.Label>Item Name</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Enter item name"
                        value={itemName}
                        onChange={(e) => {
                          setItemName(e.target.value);
                        }}
                      />
                    </Form.Group>
                  )
                }

                {
                  addItem === "new item" && (
                    <Form.Group>
                      <Form.Label>Item Price</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Enter item price"
                        value={itemPrice}
                        onChange={(e) => {
                          setItemPrice(e.target.value);
                        }}
                      />
                    </Form.Group>
                  )
                }

                <Form.Group>
                  <Form.Label>Item Quantity</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter item quantity as positive integer"
                    value={addQuantity}
                    onChange={(e) => {
                      setQuantity(e.target.value);
                    }}
                  />
                </Form.Group>
                <Button style={{ margin: 10 }} variant="success" type="submit">Add Item</Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} style={{ flex: 1 }}>
          <Card>
            <Card.Body>
              <Card.Title>Change Item Quantity</Card.Title>
              <Form onSubmit={handleQuantityChange}>
                <Form.Group>
                  <Form.Label>Item ID</Form.Label>
                  <Form.Select
                    value={itemID}
                    onChange={(e) => {
                      setItemID(e.target.value);
                    }}>
                    <option value="">Select the item you want to modify</option>
                    {items.map(({ itemId, siteId, quantity }) => (
                      <option key={itemId.itemId} value={itemId.itemId}>{itemId.itemName}</option>
                    ))}

                  </Form.Select>
                </Form.Group>
                <Form.Group>
                  <Form.Label>Change Quantity</Form.Label>
                  <Form.Select
                    value={changeQuantity}
                    onChange={(e) => {
                      setChangeQuantity(e.target.value);
                    }}
                  >
                    <option value="">Select how you want to change the quantity</option>
                    <option value="Direct modification">Direct modification</option>
                    <option value="+">+</option>
                    <option value="-">-</option>
                  </Form.Select>
                </Form.Group>

                <Form.Group>
                  <Form.Label>New Quantity</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter the amount of quantity you want to change as positive integer"
                    value={newQuantity}
                    onChange={(e) => {
                      setNewQuantity(e.target.value);
                    }}
                  />
                </Form.Group>

                <Button style={{ margin: 10 }} variant="success" type="submit">Change quantity</Button>

              </Form>
            </Card.Body>
          </Card>
        </Col>

      </div>

    </>

  );
}

export default IMSSite;
