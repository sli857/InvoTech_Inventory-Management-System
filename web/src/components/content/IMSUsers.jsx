import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Container, Row, Col, Form, Button, Card } from 'react-bootstrap';

const backend_baseurl = 'http://cs506-team-35.cs.wisc.edu:8080'
// Define the IMSUsers component
const IMSUsers = () => {
    // State for storing user data
    const [users, setUsers] = useState([]);
    // State for handling the search term input for the username
    const [searchTerm, setSearchTerm] = useState('');
    // State for handling the search term input for position
    const [searchPosition, setSearchPosition] = useState('');

    // useEffect to fetch users data on component mount
    useEffect(() => {
        // Asynchronous function to fetch users from the server
        const fetchUsers = async () => {
            try {
                // Attempt to retrieve user data from the server
                const response = await axios.get(`${backend_baseurl}/users`);
                // Update the users state with the fetched data
                setUsers(response.data);
            } catch (error) {
                // Log any errors to the console
                console.error('Error fetching users:', error);
            }
        };

        // Execute the fetch operation
        fetchUsers();
    }, []);

    // Handle changes to the search input field
    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };


    const handlePositionChange = (event) => {
        setSearchPosition(event.target.value);
    };

    // Prevent form submission from refreshing the page
    const handleFormSubmit = (event) => {
        event.preventDefault();
    };

    // Filter users based on username and position
    const filteredUsers = users.filter(user =>
        user.username.toLowerCase().includes(searchTerm.toLowerCase()) &&
        user.position.toLowerCase().includes(searchPosition.toLowerCase())
    );

    // Render the component

    return (
        <Container fluid="md">
            <Card className="mt-5">
                <Card.Header as="h1">User Management</Card.Header>
                <Card.Body>
                    <Form onSubmit={handleFormSubmit}>
                        <Row className="mb-3">
                            <Col sm="6">
                                <Form.Group as={Row}>
                                    <Form.Label column sm="4">Search by Username:</Form.Label>
                                    <Col sm="8">
                                        <Form.Control
                                            type="text"
                                            placeholder="Username"
                                            value={searchTerm}
                                            onChange={handleSearchChange}
                                        />
                                    </Col>
                                </Form.Group>
                            </Col>
                            <Col sm="6">
                                <Form.Group as={Row}>
                                    <Form.Label column sm="4">Search by Position:</Form.Label>
                                    <Col sm="8">
                                        <Form.Control
                                            type="text"
                                            placeholder="Position"
                                            value={searchPosition}
                                            onChange={handlePositionChange}
                                        />
                                    </Col>
                                </Form.Group>
                            </Col>
                        </Row>
                    </Form>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Position</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredUsers.length > 0 ? (
                                filteredUsers.map(user => (
                                    <tr key={user.id}>
                                        <td>{user.username}</td>
                                        <td>{user.position}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="2">No users found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        </Container>
    );
};

// Export the IMSUsers component for use in other parts of the application
export default IMSUsers;
