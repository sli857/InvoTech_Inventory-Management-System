import { useEffect, useState } from 'react';
import { Container, Row, Col, Table } from 'react-bootstrap';

function IMSShipments() {
  const [shipments, setShipments] = useState([]);
  const [filteredShipments, setFilteredShipments] = useState([]);
  const [filter, setFilter] = useState({
    id: '',
    source: '',
    destination: '',
  });

  useEffect(() => {
    fetch("https://localhost:8080/shipments")
      .then(res => res.json())
      .then(data => {
        setShipments(data);
        setFilteredShipments(data); // Initialize filtered shipments
      })
      .catch(error => console.error("Failed to fetch shipments:", error));
  }, []);

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilter(prev => ({ ...prev, [name]: value }));
  };

  // Filter the shipments based on input fields
    const handleFilter = () => {
      let filtered = shipments.filter(shipment =>
        (filter.id ? shipment.id.toString().includes(filter.id) : true) &&
        (filter.source ? shipment.source.toLowerCase().includes(filter.source.toLowerCase()) : true) &&
        (filter.destination ? shipment.destination.toLowerCase().includes(filter.destination.toLowerCase()) : true)
      );
      setFilteredShipments(filtered);
    };

      const handleReset = () => {
        setFilter({ id: '', source: '', destination: '' });
        setFilteredShipments(shipments);
      };


  return (
    <Container fluid="md" style={{ padding: '20px', paddingBottom: "200px", marginTop: '20px', background: "#f7f7f7", boxShadow: "0 2px 4px rgba(0,0,0,.1)" }}>
      <h2 style={{ marginBottom: '20px', textAlign: 'center' }}>Shipments: {filteredShipments.length} Active</h2>
      <Row>
        <Col md={3}>
          <Form.Control type="text" placeholder="Filter by ID" name="id" onChange={handleChange} value={filter.id} />
        </Col>
        <Col md={3}>
          <Form.Control type="text" placeholder="Filter by Source" name="source" onChange={handleChange} value={filter.source} />
        </Col>
        <Col md={3}>
          <Form.Control type="text" placeholder="Filter by Destination" name="destination" onChange={handleChange} value={filter.destination} />
        </Col>
        <Col md={3}>
          <Button onClick={handleFilter} variant="primary">Apply Filters</Button>
          <Button onClick={handleReset} variant="secondary" style={{ marginLeft: "10px" }}>Reset</Button>
        </Col>
      </Row>
      <Row className="mb-4" style={{ marginTop: '20px' }}>
        <Col>
          <Table striped bordered hover size="sm">
            <head>
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
            </head>
            <body>
              {filteredShipments.map(shipment => (
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
            </body>
          </Table>
        </Col>
      </Row>
    </Container>
  );
}

export default IMSShipments;
