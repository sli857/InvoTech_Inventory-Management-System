import { Link } from "react-router-dom";

/**
 * IMSNoMatch Component
 * Displays a message indicating that the requested page could not be found (404 error).
 * Provides a link to navigate back to the home page.
 * 
 * Use this component for undefined routes in your application to improve user experience
 * by guiding them back to a known route when they encounter a dead-end.
 *
 * @returns {JSX.Element} Renders a not found (404) message with a link to the home page.
 */
function IMSNoMatch() {
    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2>That&apos;s a 404.</h2>
            <p>Uh oh, looks like you&apos;re lost!</p>
            <p>
                <Link to="/">Back to safety.</Link>
            </p>
        </div>
    );
}

export default IMSNoMatch;
