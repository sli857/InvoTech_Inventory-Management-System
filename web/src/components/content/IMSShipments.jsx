import {useCallback, useEffect, useState} from 'react';
import {
  Button,
  Card,
  Col,
  Container,
  Dropdown,
  DropdownButton,
  Form,
  OverlayTrigger,
  Row,
  Table,
  Tooltip,
} from 'react-bootstrap';

const backendBaseurl = 'http://cs506-team-35.cs.wisc.edu:8080';

/**
 * IMSShipments Component
 * Fetches and displays a list of shipment information from a mock API endpoint through Postman.
 * Showcases each shipment's details including a source, destination, current location,
 * departure and arrival times, and status.
 *
 * @return {JSX.Element} A container with a table displaying the shipment data.
 */
function IMSShipments() {
  // State for managing site data
  const [sites, setSites] = useState([]);
  const [items, setItems] = useState([]);
  const [shipments, setShipments] = useState([]);

  // State for form validation
  const [sourceValid, setSourceValid] = useState(true);
  const [destinationValid, setDestinationValid] = useState(true);
  const [itemValid, setItemValid] = useState(true);
  const [quantityValid, setQuantityValid] = useState(true);

  // State for filter queries
  const [selectedSource, setSelectedSource] = useState('Select Source');
  const [selectedDestination, setSelectedDestination] = useState('Select Destination');
  const [selectedStatus, setSelectedStatus] = useState('Select Status');

  // Filter shipment based on selected filters
  const filteredShipments = shipments.filter((shipment) => {
    return (selectedSource === 'Select Source' || shipment.source === selectedSource) &&
            (selectedDestination === 'Select Destination' || shipment.destination === selectedDestination) &&
            (selectedStatus === 'Select Status' || shipment.shipmentStatus === selectedStatus);
  });

  // Helper function to reset filter fields
  const resetFilters = () => {
    setSelectedSource('Select Source');
    setSelectedDestination('Select Destination');
    setSelectedStatus('Select Status');
  };

  /**
   * Fetches the list of shipments from the mock API endpoint and sets the shipments state to the fetched data.
   *
   * @type {(function(): Promise<void>)|*}
   */
  const fetchShipments = useCallback(async () => {
    try {
      const response = await fetch(`${backendBaseurl}/shipments`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setShipments(data);
    } catch (error) {
      console.error('There was a problem with fetching shipments:', error);
    }
  }, []);

  /**
   * Fetches the list of sites, items, and shipments from the mock API endpoint when the component mounts.
   */
  useEffect(() => {
    fetchShipments();
    fetchSites();
    fetchItems();
  }, [fetchShipments]);

  /**
   * Fetches the list of sites from the mock API endpoint and sets the Sites state to the fetched data.
   *
   * @return {Promise<void>}
   */
  const fetchSites = async () => {
    try {
      const response = await fetch(`${backendBaseurl}/sites`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setSites(data);
    } catch (error) {
      console.error('There was a problem with fetching sites:', error);
    }
  };

  /**
   * Fetches the list of items from the mock API endpoint and sets the items state to the fetched data.
   *
   * @return {Promise<void>}
   */
  const fetchItems = async () => {
    try {
      const response = await fetch(`${backendBaseurl}/items`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setItems(data);
    } catch (error) {
      console.error('There was a problem with fetching items:', error);
    }
  };

  /**
   * Validates the form inputs.
   *
   * @param {number} source The source site value.
   * @param {number} destination The destination site value.
   * @param {number} item The item value.
   * @param {number} quantity The quantity value.
   * @return {boolean} True if all inputs are valid, otherwise false.
   */
  function validateInputs(source, destination, item, quantity) {
    let isValid = true;

    // Reset validation states
    setSourceValid(true);
    setDestinationValid(true);
    setItemValid(true);
    setQuantityValid(true);

    if (!source || !destination || !item) {
      console.error('Please fill out all fields');
      setSourceValid(false);
      setDestinationValid(false);
      setItemValid(false);
      isValid = false;
    }

    if (source === destination) {
      console.error('Source and destination cannot be the same.');
      setSourceValid(false);
      setDestinationValid(false);
      isValid = false;
    }

    if (!quantity || quantity <= 0) {
      console.error('Quantity must be a positive number.');
      setQuantityValid(false);
      isValid = false;
    }

    return isValid;
  }

  /**
   * Handles the form submission for adding a new shipment.
   *
   * @param {SubmitEvent} event The form submission event.
   * @return {Promise<void>} A promise that resolves after the shipment is added.
   */
  async function handleSubmit(event) {
    event.preventDefault();

    const source = event.target.formSource.value;
    const destination = event.target.formDestination.value;
    const item = event.target.formItem.value;
    const quantity = parseInt(event.target.formQuantity.value);

    // Validate inputs
    if (!validateInputs(source, destination, item, quantity)) {
      return;
    }

    const date = new Date().toISOString();

    // Start of submitting the form
    try {
      // API call to add a new shipment
      const shipmentResponse = await fetch(`${backendBaseurl}/shipments/add`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          source: source,
          destination: destination,
          currentLocation: 'Site ' + source,
          departureTime: date,
          estimatedArrivalTime: date,
          actualArrivalTime: date,
          shipmentStatus: 'Delivered',
        }),
      });

      // If the shipment was added successfully, add the ship
      if (!shipmentResponse.ok) {
        console.error('There was a problem adding the shipment.', shipmentResponse);
      } else {
        // API call to add a new ship
        const shipResponse = await fetch(`${backendBaseurl}/ships/add`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            itemId: item,
            shipmentId: shipments.length + 1,
            quantity: quantity,
          }),
        });

        if (!shipResponse.ok) {
          console.error('There was a problem adding the ship.', shipResponse);
        } else {
          await fetchShipments();
          event.target.reset();
        }
      }
    } catch (error) {
      console.error('Error occurred during submission:', error);
    }
  }

  return (
    <Container fluid="md" className="mt-3">

      {/* Table of shipments */}
      <Row className="mb-4">
        <Col>
          <Table striped bordered hover responsive>
            <caption style={{fontSize: '20px'}}>{shipments.length} Shipments In Table</caption>
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Source</th>
                <th>Destination</th>
                <th>Current Location</th>
                <th>Departed</th>
                <th>Estimated Arrival</th>
                <th>Actual Arrival</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredShipments.map((shipment) => (
                <OverlayTrigger
                  key={shipment.shipmentId}
                  overlay={<Tooltip id={`tooltip-${shipment.shipmentId}`}>Press this site to see its
                                    inventory contents.</Tooltip>}
                  placement={'top'}
                >
                  <tr key={shipment.shipmentId} style={{cursor: 'pointer'}}>
                    <td>{shipment.shipmentId}</td>
                    <td>{sites.find((site) => site.siteId === shipment.source)?.siteName}</td>
                    <td>{sites.find((site) => site.siteId === shipment.destination)?.siteName}</td>
                    <td>{shipment.currentLocation}</td>
                    <td>{shipment.departureTime ? new Date(shipment.departureTime).toLocaleDateString('en-US', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                    }) : 'Null'}</td>
                    <td>{shipment.estimatedArrivalTime ? new Date(shipment.estimatedArrivalTime).toLocaleDateString('en-US', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                    }) : 'Null'}</td>
                    <td>{shipment.actualArrivalTime ? new Date(shipment.actualArrivalTime).toLocaleDateString('en-US', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                    }) : 'Null'}</td>
                    <td>{shipment.shipmentStatus}</td>
                  </tr>
                </OverlayTrigger>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
      <Row>

        {/* Form for adding a new shipment */}
        <Col md={6} className="mb-4">
          <Card>
            <Card.Body>
              <Card.Title>Add Shipment</Card.Title>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formSource">
                  <Form.Label>From Site</Form.Label>
                  <Form.Control as="select" isInvalid={!sourceValid}>
                    {sites.map((site) => <option key={site.siteId}
                      value={site.siteId}>{site.siteName}</option>)}
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">
                                        Source and destination cannot be the same.
                  </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formDestination">
                  <Form.Label>To Site</Form.Label>
                  <Form.Control as="select" isInvalid={!destinationValid}>
                    {sites.map((site) => <option key={site.siteId}
                      value={site.siteId}>{site.siteName}</option>)}
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">
                                        Source and destination cannot be the same.
                  </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formItem">
                  <Form.Label>Item</Form.Label>
                  <Form.Control as="select" isInvalid={!itemValid}>
                    {items.map((item) => <option key={item.itemId}
                      value={item.itemId}>{item.itemName}</option>)}
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">
                                        Input valid item.
                  </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formQuantity">
                  <Form.Label>Quantity</Form.Label>
                  <Form.Control type="number" placeholder="Enter quantity"
                    isInvalid={!quantityValid}/>
                  <Form.Control.Feedback type="invalid">
                                        Please enter a valid quantity.
                  </Form.Control.Feedback>
                </Form.Group>
                <Button variant="success" type="submit">
                                    Submit
                </Button>
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
                {/* Filter by Source Location */}
                <DropdownButton id="dropdown-location-button"
                  title={sites.find((site) => site.siteId === selectedSource)?.siteName || 'Select Source'}>
                  {sites.map((site) => (
                    <Dropdown.Item key={`site-${site.siteId}`}
                      onClick={() => setSelectedSource(site.siteId)}>
                      {site.siteName}
                    </Dropdown.Item>
                  ))}
                  <Dropdown.Item
                    onClick={() => setSelectedSource('Select Source')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Filter by Destination Location */}
                <DropdownButton id="dropdown-location-button"
                  title={sites.find((site) => site.siteId === selectedDestination)?.siteName || 'Select Destination'}>
                  {sites.map((site) => (
                    <Dropdown.Item key={`site-${site.siteId}`}
                      onClick={() => setSelectedDestination(site.siteId)}>
                      {site.siteName}
                    </Dropdown.Item>
                  ))}
                  <Dropdown.Item
                    onClick={() => setSelectedDestination('Select Destination')}>None</Dropdown.Item>
                </DropdownButton>

                {/* Filter by Shipment Status */}
                <DropdownButton id="dropdown-status-button" title={selectedStatus || 'Shipment Status'}>
                  <Dropdown.Item onClick={() => setSelectedStatus('Pending')}>Pending</Dropdown.Item>
                  <Dropdown.Item onClick={() => setSelectedStatus('In Transit')}>In
                                        Transit</Dropdown.Item>
                  <Dropdown.Item
                    onClick={() => setSelectedStatus('Delivered')}>Delivered</Dropdown.Item>
                  <Dropdown.Item
                    onClick={() => setSelectedStatus('Select Status')}>None</Dropdown.Item>
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

export default IMSShipments;
