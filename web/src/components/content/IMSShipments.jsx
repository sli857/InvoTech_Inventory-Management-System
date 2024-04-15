import { useEffect, useState, useCallback } from 'react';
import { Container, Row, Col, Table, Card, Form, Button } from 'react-bootstrap';

/**
 * IMSShipments Component
 * Fetches and displays a list of shipment information from a mock API endpoint through Postman.
 * Showcases each shipment's details including source, destination, current location,
 * departure and arrival times, and status.
 *
 * @returns {JSX.Element} A container with a table displaying the shipment data.
 */
function IMSShipments() {
  const [sites, setSites] = useState([]);
  const [items, setItems] = useState([]);
  const [shipments, setShipments] = useState([]);
  const [quantity, setQuantity] = useState(0);

  const fetchShipments = useCallback(async () => {
    try {
      const response = await fetch("https://930ca83e-ce9f-4adc-bb48-3e7f2cf4773b.mock.pstmn.io/shipments")
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setShipments(data);
      setQuantity(data.length);
    } catch (error) {
      console.error("There was a problem with fetching shipments:", error);
    }
  }, []);

  useEffect(() => {
    fetchShipments();
    fetchSites();
    fetchItems();
  }, [fetchShipments]);

  const fetchSites = async () => {
    try {
      const response = await fetch("http://localhost:8080/sites");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setSites(data);
    } catch (error) {
      console.error("There was a problem with fetching sites:", error);
    }
  };

  const fetchItems = async () => {
    try {
      const response = await fetch("http://localhost:8080/items");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setItems(data);
    } catch (error) {
      console.error("There was a problem with fetching items:", error);
    }
  }

  async function handleSubmit(event) {
    event.preventDefault();
    try {
      const shipmentReponse = await fetch("http://localhost:8080/shipments/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          sourceId: event.target.formSource.value,
          destinationId: event.target.formDestination.value,
          currentLocationId: null,
          departureTime: null,
          estimatedArrivalTime: null,
          actualArrivalTime: null,
          shipmentStatus: "PENDING",
        }),
      });
      if (!shipmentReponse.ok) {
        console.error("There was a problem adding the shipment.");
        return;
      }
      const newShipmentData = await shipmentReponse.json();

      const shipResponse = await fetch("http://localhost:8080/ship/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          itemId: event.target.formItem.value,
          shipmentId: newShipmentData.id,
          quantity: event.target.formQuantity.value,
        }),
      });
      if (!shipResponse.ok) {
        console.error("There was a problem adding the site.");
        return;
      }
      fetchShipments();
    } catch (error) {
      console.error("Error occurred during shipment submission:", error);
    }
  }

  return (
    <Container fluid="md" style={{ padding: '20px', paddingBottom: "200px", marginTop: '20px', background: "#f7f7f7", boxShadow: "0 2px 4px rgba(0,0,0,.1)" }}>
      <h2 style={{ marginBottom: '20px', textAlign: 'center' }}>Shipments: {quantity} Active</h2>
      <Row className="mb-4">
        <Col>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>ID</th>
                <th>Source</th>
                <th>Destination</th>
                <th>Current Location</th>
                <th>Departed</th>
                <th>Estimated Arrival</th>
                <th>Actual Arrival</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {shipments.map(shipment => (
                <tr key={shipment.id}>
                  <td>{shipment.id}</td>
                  <td>{shipment.source}</td>
                  <td>{shipment.destination}</td>
                  <td>{shipment.currentLocation}</td>
                  <td>{shipment.departureTime || 'N/A'}</td>
                  <td>{shipment.estimatedArrivalTime}</td>
                  <td>{shipment.actualArrivalTime || 'N/A'}</td>
                  <td>{shipment.shipmentStatus}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
      <Row>
        <Card>
          <Card.Body>
            <Card.Title>Add Shipment</Card.Title>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="formSource">
                <Form.Label>From Site</Form.Label>
                <Form.Control as="select">
                  {sites.map(site => <option key={site.id} value={site.id}>{site.name}</option>)}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formDestination">
                <Form.Label>To Site</Form.Label>
                <Form.Control as="select">
                  {sites.map(site => <option key={site.id} value={site.id}>{site.name}</option>)}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formItem">
                <Form.Label>Item</Form.Label>
                <Form.Control as="select">
                  {items.map(item => <option key={item.id} value={item.id}>{item.name}</option>)}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formQuantity">
                <Form.Label>Quantity</Form.Label>
                <Form.Control type="number" placeholder="Enter quantity" />
              </Form.Group>
              <Button variant="primary" type="submit">
                Submit
              </Button>
            </Form>
          </Card.Body>
        </Card>
      </Row>
    </Container>
  );
}

export default IMSShipments;
