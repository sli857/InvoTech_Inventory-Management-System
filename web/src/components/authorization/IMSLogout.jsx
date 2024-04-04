import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types'; // Import PropTypes

/**
 * Component that handles user logout.
 *
 * @param {{ setAuth: Function }} props - Component props containing the setAuth function to update authentication state.
 */
function IMSLogout({setAuth}) {
    const navigate = useNavigate();

    useEffect(() => {
        // Clear the authentication state
        setAuth(false);

        // Redirect to login page
        navigate('/login');
    }, [setAuth, navigate]);

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh'}}>
            Logging you out...
        </div>
    );
}

// Define PropTypes for IMSLogout
IMSLogout.propTypes = {
    setAuth: PropTypes.func.isRequired, // Validate setAuth as a required function
};

export default IMSLogout;
