import {useCallback, useEffect, useState} from 'react';
import {Button, Card, Col, Container, Form, Row, Table} from 'react-bootstrap';

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

    /**
     * Fetches the list of shipments from the mock API endpoint and sets the shipments state to the fetched data.
     *
     * @type {(function(): Promise<void>)|*}
     */
    const fetchShipments = useCallback(async () => {
        try {
            const response = await fetch("http://localhost:8080/shipments")
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            const data = await response.json();
            setShipments(data);
        } catch (error) {
            console.error("There was a problem with fetching shipments:", error);
        }
    }, []);

    /**
     * Fetches the list of sites, items, and shipments from the mock API endpoint when the component mounts.
     */
    useEffect(() => {
        fetchShipments();
        fetchSites();
        fetchItems();
    }, [fetchShipments]);

    /**
     * Fetches the list of sites from the mock API endpoint and sets the sites state to the fetched data.
     *
     * @returns {Promise<void>}
     */
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

    /**
     * Fetches the list of items from the mock API endpoint and sets the items state to the fetched data.
     *
     * @returns {Promise<void>}
     */
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

    /**
     * Handles the form submission for adding a new shipment.
     *
     * @param event The form submission event.
     * @returns {Promise<void>} A promise that resolves after the shipment is added.
     */
    async function handleSubmit(event) {
        event.preventDefault();

        // Verify the form fields
        // TODO: Add form validation

        try {
            // API call to add a new shipment
            const shipmentResponse = await fetch("http://localhost:8080/shipments/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    source: event.target.formSource.value,
                    destination: event.target.formDestination.value,
                    currentLocation: "Site " + event.target.formSource.value,
                    departureTime: new Date().toISOString(),
                    estimatedArrivalTime: null,
                    actualArrivalTime: null,
                    shipmentStatus: "Pending",
                }),
            });

            // If the shipment was added successfully, add the ship
            if (!shipmentResponse.ok) {
                console.error("There was a problem adding the shipment.", shipmentResponse);
            } else {
                // After adding the shipment, fetch the updated list of shipments
                await fetchShipments();

                // API call to add a new ship
                const shipResponse = await fetch("http://localhost:8080/ships/add", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        itemId: event.target.formItem.value,
                        shipmentId: shipments.length + 1,
                        quantity: event.target.formQuantity.value,
                    }),
                });

                if (!shipResponse.ok) {
                    console.error("There was a problem adding the ship.", shipResponse);
                } else {
                    await fetchShipments();
                }
            }

        } catch (error) {
            console.error("Error occurred during submission:", error);
        }
    }

    return (
        <Container fluid="md" style={{
            padding: '20px',
            paddingBottom: "200px",
            marginTop: '20px',
            background: "#f7f7f7",
            boxShadow: "0 2px 4px rgba(0,0,0,.1)"
        }}>
            <h2 style={{marginBottom: '20px', textAlign: 'center'}}>Shipments: {shipments.length} Active</h2>
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
                                <td>{shipment.departureTime ? new Date(shipment.departureTime).toLocaleDateString("en-US", {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                }) : 'Null'}</td>
                                <td>{shipment.estimatedArrivalTime ? new Date(shipment.estimatedArrivalTime).toLocaleDateString("en-US", {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                }) : 'Null'}</td>
                                <td>{shipment.actualArrivalTime ? new Date(shipment.actualArrivalTime).toLocaleDateString("en-US", {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                }) : 'Null'}</td>
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
                                    {sites.map(site => <option key={site.siteId}
                                                               value={site.siteId}>{site.siteName}</option>)}
                                </Form.Control>
                            </Form.Group>
                            <Form.Group controlId="formDestination">
                                <Form.Label>To Site</Form.Label>
                                <Form.Control as="select">
                                    {sites.map(site => <option key={site.siteId}
                                                               value={site.siteId}>{site.siteName}</option>)}
                                </Form.Control>
                            </Form.Group>
                            <Row>
                                <Col>
                                    <Form.Group controlId="formItem">
                                        <Form.Label>Item</Form.Label>
                                        <Form.Control as="select">
                                            {items.map(item => <option key={item.itemId}
                                                                       value={item.itemId}>{item.itemName}</option>)}
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group controlId="formQuantity">
                                        <Form.Label>Quantity</Form.Label>
                                        <Form.Control type="number" placeholder="Enter quantity"/>
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
