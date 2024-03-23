import { useEffect, useState } from 'react';
import { Container, Row, Col, Table } from 'react-bootstrap';

/**
 * IMSShipments Component
 * Fetches and displays a list of shipment information from a mock API endpoint through Postman.
 * Showcases each shipment's details including source, destination, current location,
 * departure and arrival times, and status.
 *
 * @returns {JSX.Element} A container with a table displaying the shipment data.
 */
function IMSShipments() {
  const [shipments, setShipments] = useState([]);
  const [shipmentCount, setShipmentCount] = useState(0);

  useEffect(() => {
    // Fetches shipment data from a mock API endpoint.
    fetch("https://930ca83e-ce9f-4adc-bb48-3e7f2cf4773b.mock.pstmn.io/shipments")
      .then(res => res.json())
      .then(data => {
        setShipments(data);
        setShipmentCount(data.length);
      })
      .catch(error => console.error("Failed to fetch shipments:", error));
  }, []);

  return (
    <Container fluid="md" style={{ padding: '20px', paddingBottom: "200px", marginTop: '20px', background: "#f7f7f7", boxShadow: "0 2px 4px rgba(0,0,0,.1)" }}>
      <h2 style={{ marginBottom: '20px', textAlign: 'center' }}>Shipments: {shipmentCount} Active</h2>
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
    </Container>
  );
}

export default IMSShipments;
