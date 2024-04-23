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
    // State for managing site data
    const [sites, setSites] = useState([]);
    const [items, setItems] = useState([]);
    const [shipments, setShipments] = useState([]);

    // State for form validation
    const [sourceValid, setSourceValid] = useState(true);
    const [destinationValid, setDestinationValid] = useState(true);
    const [itemValid, setItemValid] = useState(true);
    const [quantityValid, setQuantityValid] = useState(true);

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
     * Validates the form inputs.
     *
     * @param source The source site value.
     * @param destination The destination site value.
     * @param item The item value.
     * @param quantity The quantity value.
     * @returns {boolean} True if all inputs are valid, otherwise false.
     */
    function validateInputs(source, destination, item, quantity) {
        let isValid = true;

        // Reset validation states
        setSourceValid(true);
        setDestinationValid(true);
        setItemValid(true);
        setQuantityValid(true);

        if (!source || !destination || !item) {
            console.error("Please fill out all fields");
            setSourceValid(false);
            setDestinationValid(false);
            setItemValid(false);
            isValid = false;
        }

        if (source === destination) {
            console.error("Source and destination cannot be the same.");
            setSourceValid(false);
            setDestinationValid(false);
            isValid = false;
        }

        if (!quantity || quantity <= 0) {
            console.error("Quantity must be a positive number.");
            setQuantityValid(false);
            isValid = false;
        }

        return isValid;
    }

    /**
     * Handles the form submission for adding a new shipment.
     *
     * @param event The form submission event.
     * @returns {Promise<void>} A promise that resolves after the shipment is added.
     */
    async function handleSubmit(event) {
        event.preventDefault();

        const source = event.target.formSource.value;
        const destination = event.target.formDestination.value;
        const item = event.target.formItem.value;
        const quantity = parseInt(event.target.formQuantity.value);

        // Validate inputs
        if (!validateInputs(source, destination, item, quantity)) {
            return;
        }

        // Start of submitting the form
        try {
            // API call to add a new shipment
            const shipmentResponse = await fetch("http://localhost:8080/shipments/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    source: source,
                    destination: destination,
                    currentLocation: "Site " + source,
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
                // API call to add a new ship
                const shipResponse = await fetch("http://localhost:8080/ships/add", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        itemId: item,
                        shipmentId: shipments.length + 1,
                        quantity: quantity,
                    }),
                });

                if (!shipResponse.ok) {
                    console.error("There was a problem adding the ship.", shipResponse);
                } else {
                    await fetchShipments();
                    event.target.reset();
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
                                <Form.Control as="select" isInvalid={!sourceValid}>
                                    {sites.map(site => <option key={site.siteId}
                                                               value={site.siteId}>{site.siteName}</option>)}
                                </Form.Control>
                                <Form.Control.Feedback type="invalid">
                                    Source and destination cannot be the same.
                                </Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group controlId="formDestination">
                                <Form.Label>To Site</Form.Label>
                                <Form.Control as="select" isInvalid={!destinationValid}>
                                    {sites.map(site => <option key={site.siteId}
                                                               value={site.siteId}>{site.siteName}</option>)}
                                </Form.Control>
                                <Form.Control.Feedback type="invalid">
                                    Source and destination cannot be the same.
                                </Form.Control.Feedback>
                            </Form.Group>
                            <Row>
                                <Col>
                                    <Form.Group controlId="formItem">
                                        <Form.Label>Item</Form.Label>
                                        <Form.Control as="select" isInvalid={!itemValid}>
                                            {items.map(item => <option key={item.itemId}
                                                                       value={item.itemId}>{item.itemName}</option>)}
                                        </Form.Control>
                                        <Form.Control.Feedback type="invalid">
                                            Input valid item.
                                        </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group controlId="formQuantity">
                                        <Form.Label>Quantity</Form.Label>
                                        <Form.Control type="number" placeholder="Enter quantity"
                                                      isInvalid={!quantityValid}/>
                                        <Form.Control.Feedback type="invalid">
                                            Please enter a valid quantity.
                                        </Form.Control.Feedback>
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
