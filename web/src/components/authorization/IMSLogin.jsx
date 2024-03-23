import { useState } from 'react';
import {Form, Button, Alert, Container} from 'react-bootstrap';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import crest from '../../assets/logo.webp'; 

/**
 * A login component for handling user authentication.
 * It allows users to enter their username and password, validates these credentials,
 * and navigates to the home page upon successful authentication.
 * 
 * @param {{ setAuth: Function }} props - Component props containing the setAuth function to update authentication state.
 * @returns {JSX.Element} The rendered component.
 */
function IMSLogin({ setAuth }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  /**
   * Handles the form submission event.
   * Validates the username and password and sets the authentication state accordingly.
   * 
   * @param {React.FormEvent<HTMLFormElement>} e - The form event.
   */
  const handleSubmit = (e) => {
    e.preventDefault();
    // Placeholder logic for authentication. Replace with your authentication logic.
    if (username === 'admin' && password === 'admin') {
      setAuth(true);
      navigate('/'); // Navigate to the home page upon successful login.
    } else {
      setError('Invalid credentials');
    }
  };

  return (
    <Container style={{
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: `url(${crest}) no-repeat center center`,
      backgroundSize: 'cover'
    }}>
      {/* React Bootstrap Form component */}
      <Form onSubmit={handleSubmit} style={{ width: '100%', maxWidth: '330px', padding: '15px', background: '#fff', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0,0,0,.05)' }}>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form.Group className="mb-3" controlId="username">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="password">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>

        <Button variant="primary" type="submit" style={{ width: '100%' }}>
          Login
        </Button>
      </Form>
    </Container>
  );
}

IMSLogin.propTypes = {
  setAuth: PropTypes.func.isRequired, // Validates that setAuth prop is a function and is required
};


export default IMSLogin;
