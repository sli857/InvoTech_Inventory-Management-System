import { useEffect, useState } from 'react';
import { Col, Form, Button } from 'react-bootstrap';

function IMSRecordShipment() {
    const [items, setItems] = useState([]);
    const [sites, setSites] = useState([]);

    const API_URL = 'http://localhost:8080';

    useEffect(() => {
        fetch(`${API_URL}/sites`)
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    setSites(data);
                } else {
                    throw new Error("No sites found.");
                }
            })
            .catch(error => console.error("Failed to fetch sites", error));

        fetch(`${API_URL}/items`)
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    setItems(data);
                } else {
                    throw new Error("No items found.");
                }
            })
            .catch(error => console.error("Failed to fetch items", error));
    }, []);

    return (
        <Col md={6}>
            <Form>
                <Form.Group controlId="formSite">
                    <Form.Label>Site</Form.Label>
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
        </Col>
    );
}

export default IMSRecordShipment;