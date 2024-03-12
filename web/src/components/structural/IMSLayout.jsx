import {Container, Nav, Navbar} from "react-bootstrap";
import {Link, Outlet} from "react-router-dom";

import crest from "../../assets/logo.webp";


function IMSLayout() {

    return (
        <div>
            <Navbar bg="dark" variant="dark">
                <Container>
                    <Navbar.Brand as={Link} to="/">
                        <img
                            alt="IMS Logo"
                            src={crest}
                            width="60"
                            height="60"
                            className="d-inline-block align-top"
                        />{" "}
                        Inventories
                    </Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">
                            Home
                        </Nav.Link>
                        <Nav.Link as={Link} to="shipments">
                            Shipments
                        </Nav.Link>
                        <Nav.Link as={Link} to="warehouses">
                            Warehouses
                        </Nav.Link>
                    </Nav>
                </Container>
            </Navbar>
            <div style={{margin: "1rem"}}>
                <Outlet/>
            </div>
        </div>
    );
}

export default IMSLayout;