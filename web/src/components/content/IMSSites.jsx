import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card, Col, Form, Row, Table, Dropdown, DropdownButton, Container, OverlayTrigger, Tooltip, } from 'react-bootstrap';
import removeIcon from '../../assets/remove_icon.png';
import { Typeahead } from 'react-bootstrap-typeahead';
import 'react-bootstrap-typeahead/css/Typeahead.css';

/**
 * The IMSSites component displays a list of sites, allows for filtering, adding, and removing sites.
 * It fetches site data from a backend service on load and provides an interface for site management.
 * Users can add a new site via a form, filter sites based on location and name, and remove sites.
 */
function IMSSites() {
  // State for managing site data
  const [sites, setSites] = useState([]);
  const [siteName, setSiteName] = useState('');
  const [siteLocation, setLocation] = useState('');
  const [siteStatus, setStatus] = useState('');
  const [ceaseDate, setCeaseDate] = useState('');
  const [internalSite, setInternalSite] = useState(false);
  const [availableItems, setAvailableItems] = useState([]);
  
  // State for filter queries
  const [selectedLocation, setSelectedLocation] = useState('Select Location');
  const [selectedName, setSelectedName] = useState('Select Name');
  const [selectedStatus, setSelectedStatus] = useState('Select Status');
  const [selectedInternalSite, setSelectedInternalSite] = useState('Select Internal Site');
  // State for item querry 
  const [selectedItems, setSelectedItems] = useState([]);
  
  // State for form validation
  const [siteNameValid, setSiteNameValid] = useState(true);
  const [siteLocationValid, setSiteLocationValid] = useState(true);
  const [siteStatusValid, setSiteStatusValid] = useState(true);
  
  // Used for displaying an error for GPS coordinates 
  let locationError = "Please enter site location in format 'latitude longitude'.";
  
  // Navigation hook for redirecting
  const navigate = useNavigate();

  

  /**
   * Calls the fetchSites function on page LOAD
   */
  useEffect(() => {
    fetchSites();
    fetchItems();
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
    } catch (error) {
      console.error("There was a problem with fetching sites:", error);
    }
  };

  /**
   * Fetches all items from the backend and updates the component state.
   */
  const fetchItems = async () => {
    try {
      const response = await fetch("http://localhost:8080/items");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setAvailableItems(data);
    } catch (error) {
      console.error("There was a problem with fetching sites:", error);
    }
  };

  /**
   * Fetches all the sites that contain all the items selected in the Select Items querry 
   */
  const fetchSitesWithItems = async (currentSelection) => {
    let counter = 0;
    // for each item selected: generate an url extension by combining them with an & for api request
    const queryParams = currentSelection.map(item => `item${counter++}=${item.itemId}`).join(`&`); 
    const url = `http://localhost:8080/availabilities/searchByItems?${queryParams}`;
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setSites(data);
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
    const statusIsValid = siteStatus.trim() !== '' && (siteStatus === 'open' || siteStatus === 'closed');
    // Validate location using the helper function
    locationError = validateLocationFormat(siteLocation);
    const locationIsValid = locationError === "";
  
    setSiteNameValid(nameIsValid);
    setSiteLocationValid(locationIsValid);
    setSiteStatusValid(statusIsValid);

    if (!nameIsValid || !locationIsValid || !statusIsValid) {
      // Prevent form submission if validation fails
      return;
    }
    // Add the site to the database 
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

  // Filter sites based on name, locaation, status and whether they are internal site
  const filteredSites = sites.filter(site => {
    return (selectedName === 'Select Name' || site.siteName === selectedName) &&
      (selectedLocation === 'Select Location' || site.siteLocation === selectedLocation) &&
      (selectedInternalSite === 'Select Internal Site' || (selectedInternalSite === 'Yes' && site.internalSite) || (selectedInternalSite === 'No' && !site.internalSite)) &&
      (selectedStatus === 'Select Status' || site.siteStatus === selectedStatus);
  });

  // Helper function to reset filter fields 
  const resetFilters = () => {
    setSelectedLocation('Select Location');
    setSelectedName('Select Name');
    setSelectedInternalSite('Select Internal Site');
    setSelectedStatus('Select Status');
  };

  // Helper function to validate the location format and range
  function validateLocationFormat(location) {
    const regex = /^([-+]?\d{1,2}(\.\d+)?)[ ]([-+]?\d{1,3}(\.\d+)?)$/;
    const match = location.match(regex);

    if (!match) {
      return "Location must be in the format: 'latitude longitude'.";
    }

    const latitude = parseFloat(match[1]);
    const longitude = parseFloat(match[3]);

    if (latitude < -90 || latitude > 90) {
      return "Latitude must be between -90 and +90 degrees.";
    }

    if (longitude < -180 || longitude > 180) {
      return "Longitude must be between -180 and +180 degrees.";
    }

    // If the location is valid
    return "";
  }



  

  /**
   * JSX Component that represents the Home Page of the Application
   */
  return (
    <Container fluid="md" className="mt-3">
      {/* Filter sites based on item */}
      <Row className="mb-3">
        <Col>
          <Typeahead
            id="items-typeahead"
            labelKey="itemName"
            multiple
            onChange={(selected) => {
              // Update the state component with each new selection
              setSelectedItems(selected); 
              // Fetch sites with the new set of selected Items
              fetchSitesWithItems(selected);
            }}
            options={availableItems}
            placeholder="Select items..."
            selected={selectedItems}
          />
        </Col>
      </Row>
      {/* Table of sites */}
      <Row className="mb-4">
        <Col>
          <Table striped bordered hover responsive>
            <caption style={{fontSize: '20px'}}>{filteredSites.length} Sites In Table</caption>
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Site Name</th>
                <th>Location</th>
                <th>Status</th>
                <th>Cease Date</th>
                <th>Internal Site</th>
                <th>Remove Site</th>
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
                  {!siteLocationValid && <Form.Control.Feedback type="invalid">{locationError}</Form.Control.Feedback>}
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
          <Card>
            <Card.Body>
              <Card.Title>Filters</Card.Title>
              <div className="d-flex flex-column gap-3">
                {/* Filter by Name */}
                <DropdownButton id="dropdown-name-button" title={selectedName || 'Select Name'}>
                  {sites.map((site) => (
                    <Dropdown.Item key={`name-${site.siteId}`} onClick={() => setSelectedName(site.siteName)}>
                      {site.siteName}
                    </Dropdown.Item>
                  ))}
                  <Dropdown.Item onClick={() => setSelectedName('Select Name')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Filter by Location */}
                <DropdownButton id="dropdown-location-button" title={selectedLocation || 'Select Location'}>
                  {sites.map((site) => (
                    <Dropdown.Item key={`location-${site.siteId}`} onClick={() => setSelectedLocation(site.siteLocation)}>
                      {site.siteLocation}
                    </Dropdown.Item>
                  ))}
                  <Dropdown.Item onClick={() => setSelectedLocation('Select Location')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Filter by Internal Site */}
                <DropdownButton id="dropdown-internal-button" title={selectedInternalSite || 'Internal Site'}>
                  <Dropdown.Item onClick={() => setSelectedInternalSite('Yes')}>Yes</Dropdown.Item>
                  <Dropdown.Item onClick={() => setSelectedInternalSite('No')}>No</Dropdown.Item>
                  <Dropdown.Item onClick={() => setSelectedInternalSite('Select Internal Site')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Filter by Site Status */}
                <DropdownButton id="dropdown-status-button" title={selectedStatus || 'Site Status'}>
                  <Dropdown.Item onClick={() => setSelectedStatus('Open')}>Open</Dropdown.Item>
                  <Dropdown.Item onClick={() => setSelectedStatus('Closed')}>Closed</Dropdown.Item>
                  <Dropdown.Item onClick={() => setSelectedStatus('Select Status')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Reset Filters Button */}
                <Button variant="secondary" onClick={resetFilters}>Reset Filters</Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default IMSSites;