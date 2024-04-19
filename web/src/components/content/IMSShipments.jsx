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
      const response = await fetch("http://localhost:8080/shipments")
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
          source: event.target.formSource.value,
          destination: event.target.formDestination.value,
          currentLocation: null,
          departureTime: null,
          estimatedArrivalTime: null,
          actualArrivalTime: null,
          shipmentStatus: "PENDING",
        }),
      });
      console.log(JSON.stringify({
        sourceId: event.target.formSource.value,
        destinationId: event.target.formDestination.value,
        currentLocationId: null,
        departureTime: null,
        estimatedArrivalTime: null,
        actualArrivalTime: null,
        shipmentStatus: "PENDING",
      }), shipmentReponse);
      if (!shipmentReponse.ok) {
        console.error("There was a problem adding the shipment.");
        return;
      }
      const newShipmentData = await shipmentReponse.json();

      const shipResponse = await fetch("http://localhost:8080/ships/add", {
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
      console.error("Error occurred during submission:", error);
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
                <tr key={shipment.shipmentId}>
                  <td>{shipment.shipmentId}</td>
                  <td>{sites.find(site => site.siteId === shipment.source)?.siteName}</td>
                  <td>{sites.find(site => site.siteId === shipment.destination)?.siteName}</td>
                  <td>{shipment.currentLocation}</td>
                  <td>{shipment.departureTime ? new Date(shipment.departureTime).toLocaleDateString("en-US", { year: 'numeric', month: 'long', day: 'numeric' }) : 'Null'}</td>
                  <td>{shipment.estimatedArrivalTime ? new Date(shipment.estimatedArrivalTime).toLocaleDateString("en-US", { year: 'numeric', month: 'long', day: 'numeric' }) : 'Null'}</td>
                  <td>{shipment.actualArrivalTime ? new Date(shipment.actualArrivalTime).toLocaleDateString("en-US", { year: 'numeric', month: 'long', day: 'numeric' }) : 'Null'}</td>
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
                  {sites.map(site => <option key={site.siteId} value={site.siteId}>{site.siteName}</option>)}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formDestination">
                <Form.Label>To Site</Form.Label>
                <Form.Control as="select">
                  {sites.map(site => <option key={site.siteId} value={site.siteId}>{site.siteName}</option>)}
                </Form.Control>
              </Form.Group>
              <Row>
                <Col>
                  <Form.Group controlId="formItem">
                    <Form.Label>Item</Form.Label>
                    <Form.Control as="select">
                      {items.map(item => <option key={item.itemId} value={item.itemId}>{item.itemName}</option>)}
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col>
                  <Form.Group controlId="formQuantity">
                    <Form.Label>Quantity</Form.Label>
                    <Form.Control type="number" placeholder="Enter quantity" />
                  </Form.Group>
                </Col>
              </Row>
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
