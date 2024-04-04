import { Container, Nav, Navbar } from "react-bootstrap";
import { Link, Outlet } from "react-router-dom";
import crest from "../../assets/logo.webp";

/**
 * IMSLayout Component
 * This component defines the layout of the Inventory Management System (IMS) application.
 * It includes a navigation bar at the top and a content area where the current route's component will be rendered.
 * The navigation links use React Router's <Link> component for client-side routing without page reloads.
 *
 * @returns {JSX.Element} The layout component with a navigation bar and an outlet for child components.
 */
function IMSLayout() {
  return (
    <div style={{
      minHeight: '100vh', 
      display: 'flex',
      flexDirection: 'column', 
      backgroundColor: "#007699"
    }}>
      <Navbar sticky="top" bg="dark" variant="dark">
        <Container style={{ fontSize: "30px" }}>
          <Navbar.Brand as={Link} to="/">
            <img
              alt="IMS Logo"
              src={crest}
              width="100"
              height="70"
            />{" "}
          </Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">
              Sites
            </Nav.Link>
            <Nav.Link as={Link} to="shipments">
              Shipments
            </Nav.Link>
            <Nav.Link as={Link} to="logout">
              Logout
            </Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <div style={{ flex: 1, margin: "1rem" }}>
        <Outlet />
      </div>
    </div>
  );
}
export default IMSLayout;