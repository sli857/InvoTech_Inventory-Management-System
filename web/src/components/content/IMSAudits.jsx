import React, { useState, useEffect } from 'react';
import { Button, Card, Container, Row, Col, Form, Table } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

const backend_baseurl = 'http://cs506-team-35.cs.wisc.edu:8080'

/**
 * The IMSAudits component displays a list of audits, allows for filtering by table name and date range.
 * It fetches all audits from a backend service on initial load and provides an interface for audit filtering.
 */
function IMSAudits() {
    // State to store the audits
    const [audits, setAudits] = useState([]);
    // State for selected table name for filtering
    const [selectedTable, setSelectedTable] = useState('');
    // State for the date range selection for filtering
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());

    /**
     * Fetches all audits from the backend on component mount.
     */
    useEffect(() => {
        handleFetchAudits(`${backend_baseurl}/audits`);
    }, []);

    /**
     * Fetches audits based on the provided URL.
     * @param {string} queryURL - The URL to fetch audits from.
     */
    const handleFetchAudits = (queryURL) => {
        fetch(queryURL)
            .then(response => response.json())
            .then(data => setAudits(data))
            .catch(error => console.error('Failed to fetch audits', error));
    };

    return (
        <Container fluid="md" className="mt-3">
            <Row>
                <Col md={12}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Filter Audits</Card.Title>
                            <Form>
                                <Row>
                                    <Col sm={4}>
                                        <Form.Control as="select" value={selectedTable} onChange={e => setSelectedTable(e.target.value)}>
                                            <option value="">Select Table</option>
                                            {['Availabilities', 'Items', 'Ships', 'Shipments', 'Sites', 'Users'].map(table => (
                                                <option key={table} value={table}>{table}</option>
                                            ))}
                                        </Form.Control>
                                        <Button className="mt-2" onClick={() => handleFetchAudits(`http://localhost:8080/audits/onTable?tableName=${selectedTable}`)}>Filter by Table</Button>
                                    </Col>
                                    <Col sm={6}>
                                        <DatePicker className="form-control" selected={startDate} onChange={date => setStartDate(date)} />
                                        <DatePicker className="form-control mt-2" selected={endDate} onChange={date => setEndDate(date)} />
                                        <Button className="mt-2" onClick={() => handleFetchAudits(`http://localhost:8080/audits/betweenPeriod?start=${startDate.toISOString().slice(0,10)}&end=${endDate.toISOString().slice(0,10)}`)}>Filter by Date</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <Row className="mt-4">
                <Col md={12}>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>Audit ID</th>
                                <th>Table Name</th>
                                <th>Field Name</th>
                                <th>Old Value</th>
                                <th>New Value</th>
                                <th>Action</th>
                                <th>Action Timestamp</th>
                            </tr>
                        </thead>
                        <tbody>
                            {audits.map(audit => (
                                <tr key={audit.auditId}>
                                    <td>{audit.auditId}</td>
                                    <td>{audit.tableName}</td>
                                    <td>{audit.fieldName}</td>
                                    <td>{audit.oldValue}</td>
                                    <td>{audit.newValue}</td>
                                    <td>{audit.action}</td>
                                    <td>{audit.actionTimestamp}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    );
}

export default IMSAudits;
