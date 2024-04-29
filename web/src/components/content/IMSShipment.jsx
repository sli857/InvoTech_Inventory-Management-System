import { useEffect, useState } from 'react';
import { Container, Table } from 'react-bootstrap';
import { useParams } from 'react-router-dom';

const backendBaseurl = 'http://cs506-team-35.cs.wisc.edu:8080';

function IMSShipment() {
  const [ships, setShips] = useState([]);

  const { shipmentId } = useParams();

  const fetchData = async () => {
    try {
      const responseShips = await fetch(
          `${backendBaseurl}/ships/shipment=${shipmentId}`
      );
      if (!responseShips.ok) {
        throw new Error('Network response was not ok');
      }
      const dataShips = await responseShips.json();
      setShips(dataShips);
    } catch (error) {
      console.error('There was a problem with fetching:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [shipmentId]);

  return (
      <Container fluid="md" className="mt-3">
        <Table striped bordered hover responsive>
          <thead className="table-dark">
          <tr>
            <th>Item</th>
            <th>Quantity</th>
          </tr>
          </thead>
          <tbody>
          {ships.map((ship) => (
              <tr key={ship.shipmentId}>
                <td>{ship.itemId.itemName}</td>
                <td>{ship.quantity}</td>
              </tr>
          ))}
          </tbody>
        </Table>
      </Container>
  );
}

export default IMSShipment;
