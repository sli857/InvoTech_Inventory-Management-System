import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card, Col, Form, Row, Table, Dropdown, DropdownButton, Container, OverlayTrigger, Tooltip, } from 'react-bootstrap';
import removeIcon from '../../assets/remove_icon.png';

/**
 * The IMSHome component displays a list of sites, allows for filtering, adding, and removing sites.
 * It fetches site data from a backend service on load and provides an interface for site management.
 * Users can add a new site via a form, filter sites based on location and name, and remove sites.
 */
function IMSHome() {
  // State for managing site data
  const [sites, setSites] = useState([]);
  const [siteName, setSiteName] = useState('');
  const [siteLocation, setLocation] = useState('');
  const [siteStatus, setStatus] = useState('');
  const [ceaseDate, setCeaseDate] = useState('');
  const [internalSite, setInternalSite] = useState(false);
  const [siteCount, setSiteCount] = useState(0);
  
  // State for filter queries
  const [selectedLocation, setSelectedLocation] = useState('Select Location');
  const [selectedName, setSelectedName] = useState('Select Name');
  
  // State for form validation
  const [siteNameValid, setSiteNameValid] = useState(true);
  const [siteLocationValid, setSiteLocationValid] = useState(true);
  const [siteStatusValid, setSiteStatusValid] = useState(true);
  
  // Navigation hook for redirecting
  const navigate = useNavigate();

  /**
   * Calls the fetchSites function on page LOAD
   */
  useEffect(() => {
    fetchSites();
  }, []);

  /**
     * Fetches sites from the backend and updates the component state.
     */
  const fetchSites = async () => {
    try {
      const response = await fetch("http://localhost:8080/sites");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setSites(data);
      setSiteCount(data.length);
    } catch (error) {
      console.error("There was a problem with fetching sites:", error);
    }
  };

  /**
   * Handles submission of the add site form. Validates input, sends data to the backend, and updates local state.
   * @param {React.FormEvent} event - The form submission event.
   */
  async function handleSubmit(event) {
    event.preventDefault();

    // Validate inputs
    const nameIsValid = siteName.trim() !== '';
    const locationIsValid = siteLocation.trim() !== '';
    const statusIsValid = siteStatus.trim() !== '' && (siteStatus === 'open' || siteStatus === 'closed');

    setSiteNameValid(nameIsValid);
    setSiteLocationValid(locationIsValid);
    setSiteStatusValid(statusIsValid);

    if (!nameIsValid || !locationIsValid || !statusIsValid) {
      // Prevent form submission if validation fails
      return;
    }
    const response = await fetch("http://localhost:8080/sites/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        siteName: siteName,
        siteLocation: siteLocation,
        siteStatus: siteStatus,
        ceaseDate: ceaseDate,
        internalSite: internalSite,
      }),
    });
    if (response.status === 200) {
      fetchSites(); // Re-fetch sites to update the list
    }
    // Assuming the form submission is successful, reset form fields here
    setSiteName('');
    setLocation('');
    setStatus('');
    setCeaseDate(''); 
    setInternalSite(false); 

    // Reset validation states
    setSiteNameValid(true);
    setSiteLocationValid(true);
    setSiteStatusValid(true);
  }
 
  /**
   * Removes a site by its ID.
   * @param {number} siteId - The ID of the site to be removed.
   */
  const removeSite = async (siteId) => {
    try {
      const response = await fetch(`http://localhost:8080/sites/delete?siteId=${siteId}`, {
        method: 'DELETE',
      });
      if (response.status === 200) {
        console.log('Site successfully removed');
        fetchSites();
      } else {
        console.error('Failed to delete the site');
      }
    } catch (error) {
      console.error('There was a problem with the fetch operation:', error);
    }
  };

  // Filter sites based on selectedName and selectedLocation
  const filteredSites = sites.filter(site => {
    return (selectedName === 'Select Name' || site.siteName === selectedName) &&
      (selectedLocation === 'Select Location' || site.siteLocation === selectedLocation);
  });
  // Helper function to reset filter fields 
  const resetFilters = () => {
    setSelectedLocation('Select Location');
    setSelectedName('Select Name');
  };

  return (
    <Container fluid="md" className="mt-3">
      <h2 className="text-center mb-4">{siteCount} Sites Active</h2>
      <Row className="mb-4">
        <Col>
          <Table striped bordered hover responsive>
            <caption>{filteredSites.length} Sites Active</caption>
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Site Name</th>
                <th>Location</th>
                <th>Status</th>
                <th>Cease Date</th>
                <th>Internal Site</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredSites.map((site) => (
                <OverlayTrigger
                  key={site.siteId}
                  overlay={<Tooltip id={`tooltip-${site.siteId}`}>Press this site to see its inventory contents.</Tooltip>}
                  placement="top"
                >
                  <tr key={site.siteId} onClick={() => navigate(`/site/${site.siteId}`)} style={{ cursor: 'pointer' }}>
                    <td>{site.siteId}</td>
                    <td>{site.siteName}</td>
                    <td>{site.siteLocation}</td>
                    <td>{site.siteStatus}</td>
                    <td>{site.ceaseDate || 'N/A'}</td>
                    <td>{site.internalSite ? 'Yes' : 'No'}</td>
                    <td onClick={(e) => { e.stopPropagation(); removeSite(site.siteId); }} style={{ cursor: 'pointer' }}>
                      <img src={removeIcon} alt="Delete" style={{ width: '20px', height: '20px' }} />
                    </td>
                  </tr>
                </OverlayTrigger>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
      <Row>
        {/* Form for Adding Sites */}
        <Col md={6} className="mb-4">
          <Card>
            <Card.Body>
              <Card.Title>Add Site</Card.Title>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formSiteName">
                  <Form.Label>Site Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter site name"
                    value={siteName}
                    onChange={(e) => {
                      setSiteName(e.target.value);
                      setSiteNameValid(e.target.value.trim() !== '');
                    }}
                    isInvalid={!siteNameValid}
                  />
                  {!siteNameValid && <Form.Control.Feedback type="invalid">Please enter a site name.</Form.Control.Feedback>}
                </Form.Group>
                <Form.Group className="mb-3" controlId="formSiteLocation">
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter location"
                    value={siteLocation}
                    onChange={(e) => {
                      setLocation(e.target.value);
                      setSiteLocationValid(e.target.value.trim() !== '');
                    }}
                    isInvalid={!siteLocationValid}
                  />
                  {!siteLocationValid && <Form.Control.Feedback type="invalid">Please enter a location.</Form.Control.Feedback>}
                </Form.Group>
                <Form.Group className="mb-3" controlId="formSiteStatus">
                  <Form.Label>Status</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter status"
                    value={siteStatus}
                    onChange={(e) => {
                      setStatus(e.target.value);
                      setSiteStatusValid(e.target.value.trim() !== '' && (e.target.value === 'open' || e.target.value === 'closed'));
                    }}
                    isInvalid={!siteStatusValid}
                  />
                  {!siteStatusValid && <Form.Control.Feedback type="invalid">Invalid status. Only &apos;open&apos; or &apos;closed&apos; is allowed.</Form.Control.Feedback>}
                </Form.Group>
                <Form.Group className="mb-3" controlId="formCeaseDate">
                  <Form.Label>Cease Date</Form.Label>
                  <Form.Control type="date" value={ceaseDate} onChange={(e) => setCeaseDate(e.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formInternalSite">
                  <Form.Check type="checkbox" label="Internal site" checked={internalSite} onChange={(e) => setInternalSite(e.target.checked)} />
                </Form.Group>
                <Button variant="success" type="submit">Add Site</Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>

        {/* Filters */}
        <Col md={6}>
          <Row className="mb-3">
            <Col>
              <Card>
                <Card.Body>
                  <Card.Title>Filter by Location</Card.Title>
                  <DropdownButton id="dropdown-location-button" title={selectedLocation}>
                    {sites.map((site) => (
                      <Dropdown.Item key={site.siteId} onClick={() => setSelectedLocation(site.siteLocation)}>
                        {site.siteLocation}
                      </Dropdown.Item>
                    ))}
                    <Dropdown.Item onClick={() => setSelectedLocation('Select Location')}>None</Dropdown.Item>
                  </DropdownButton>
                </Card.Body>
              </Card>
            </Col>
          </Row>
          <Row>
            <Col>
              <Card>
                <Card.Body>
                  <Card.Title>Filter by Name</Card.Title>
                  <DropdownButton id="dropdown-name-button" title={selectedName}>
                    {sites.map((site) => (
                      <Dropdown.Item key={site.siteId} onClick={() => setSelectedName(site.siteName)}>
                        {site.siteName}
                      </Dropdown.Item>
                    ))}
                    <Dropdown.Item onClick={() => setSelectedName('Select Name')}>None</Dropdown.Item>
                  </DropdownButton>
                </Card.Body>
              </Card>
            </Col>
          </Row>
          <Row>
            <Col>
              <Button variant="secondary" onClick={resetFilters}>Reset Filters</Button>
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}

export default IMSHome;