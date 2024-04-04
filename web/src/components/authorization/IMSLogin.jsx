import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Alert, Button, Container, Form } from 'react-bootstrap';
import PropTypes from 'prop-types';
import crest from '../../assets/logo.webp'; 

/**
 * The IMSLogin component handles user authentication, allowing users to log in or sign up.
 * It interacts with a backend service to authenticate users and optionally register new accounts.
 */
function IMSLogin({ setAuth }) {
  // State variables for managing user input and authentication status
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [position, setPosition] = useState('');
  const [isSignUp, setIsSignUp] = useState(false); // Toggle between SignUp and Login modes
  const [error, setError] = useState('');
  //const [userLogged, setUserLogged] = useState(); // Stores the logged-in user's information
  const navigate = useNavigate();

  /**
   * Handles the sign-in process, validating inputs and submitting them to the backend.
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSignIn = async (e) => {
    e.preventDefault();

    // Input validation
    if (!username || !password) {
      setError('Both username and password are required.');
      return;
    }
    if (isSignUp) {
      const allowedPositions = ['Admin', 'Manager', 'Worker'];
      if (!position || !allowedPositions.includes(position)) {
        setError('Position must be one of the following: Admin, Manager, Worker.');
        return;
      }
    }

    // Form submission
    const bodyContent = { username, password, position };
    const url = `http://localhost:8080/users/add`;
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(bodyContent),
    };

    try {
      const response = await fetch(url, options);
      const data = await response.json();

      if (response.ok) {
        setAuth(true);
        navigate('/');
      } else {
        setError(data.message || 'Something went wrong. Please try again.');
      }
    } catch (err) {
      console.error('There was an error:', err);
      setError('Network error. Please try again later.');
    }
  };

  /**
   * Handles the login process by first confirming the user's credentials,
   * then fetching and storing the user's data.
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleLogin = async (e) => {
    e.preventDefault();

    const confirmUrl = `http://localhost:8080/users/confirm?username=${username}&password=${password}`;

    try {
      const confirmResponse = await fetch(confirmUrl);
      const confirmData = await confirmResponse.text();

      if (confirmResponse.ok) {
        const userUrl = `http://localhost:8080/users/user?username=${username}&password=${password}`;
        const response = await fetch(userUrl);
        const data = await response.json();

        if (response.ok) {
          setAuth(true);
          //setUserLogged(data);
          navigate('/');
        } else {
          setError(data.message || 'Invalid login. Please try again.');
        }
      } else {
        setError(confirmData || 'Login failed. Please check your credentials and try again.');
      }
    } catch (err) {
      setError('Network error. Please try again later.');
    }
  };

  // Component layout
  return (
    <Container style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: `url(${crest}) no-repeat center center`, backgroundSize: 'cover' }}>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={isSignUp ? handleSignIn : handleLogin} style={{ width: '100%', maxWidth: '330px', padding: '15px', background: '#fff', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0,0,0,.05)' }}>
        <Form.Group className="mb-3" controlId="username">
          <Form.Label>Username</Form.Label>
          <Form.Control type="text" placeholder="Enter username" value={username} onChange={(e) => { setUsername(e.target.value); if (error) setError(''); }} />
        </Form.Group>
        <Form.Group className="mb-3" controlId="password">
          <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" value={password} onChange={(e) => { setPassword(e.target.value); if (error) setError(''); }} />
        </Form.Group>
        {/* The position input field is only shown if the signing in proccess is open (isSignUp = true)*/}  
        {isSignUp && (
          <Form.Group className="mb-3" controlId="position">
            <Form.Label>Position</Form.Label>
            <Form.Control type="text" placeholder="Enter your position" value={position} onChange={(e) => { setPosition(e.target.value); if (error) setError(''); }} />
          </Form.Group>
        )}
        <Button variant="primary" type="submit" onClick={() => { if (error) setError(''); }}>{isSignUp ? 'Sign Up' : 'Login'}</Button>
        <Button variant="secondary" onClick={() => { setIsSignUp(!isSignUp); if (error) setError(''); }} style={{ marginTop: '10px', width: '100%' }}>{isSignUp ? 'Switch to Login' : 'Switch to Sign Up'}</Button>
      </Form>
    </Container>
  );
}

IMSLogin.propTypes = {
  setAuth: PropTypes.func.isRequired,
};

export default IMSLogin;
