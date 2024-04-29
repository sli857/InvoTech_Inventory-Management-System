import { useEffect, useState } from 'react';
import { Container, Table } from 'react-bootstrap';
import { useParams } from "react-router-dom";

const backendBaseurl = 'http://cs506-team-35.cs.wisc.edu:8080';

function IMSShipment() {
  const [sites, setSites] = useState([]);
  const [items, setItems] = useState([]);
  const [shipments, setShipments] = useState([]);
  const [ships, setShips] = useState([]);

  const { shipmentId } = useParams();

  const filteredShips = ships.filter(ship => ship.shipmentId === parseInt(shipmentId));

  useEffect(() => {
    const fetchData = async () => {
      try {
        const responseShipments = await fetch(`${backendBaseurl}/shipments/shipment?shipmentId=${shipmentId}`);
        if (!responseShipments.ok) {
          throw new Error('Network response was not ok');
        }
        const dataShipments = await responseShipments.json();
        setShipments(dataShipments);

        const responseSites = await fetch(`${backendBaseurl}/sites`);
        if (!responseSites.ok) {
          throw new Error('Network response was not ok');
        }
        const dataSites = await responseSites.json();
        setSites(dataSites);

        const responseItems = await fetch(`${backendBaseurl}/items`);
        if (!responseItems.ok) {
          throw new Error('Network response was not ok');
        }
        const dataItems = await responseItems.json();
        setItems(dataItems);

        const responseShips = await fetch(`${backendBaseurl}/shipments`);
        if (!responseShips.ok) {
          throw new Error('Network response was not ok');
        }
        const dataShips = await responseShips.json();
        setShips(dataShips);

        console.log("filtered Ships:", filteredShips);
        console.log("ShipmentID:", shipmentId);
        console.log("Ships:", ships);
      } catch (error) {
        console.error('There was a problem with fetching:', error);
      }
    };

    fetchData().then(() => console.log('Data fetched'));
  }, []);

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
          {filteredShips.map((ship) => (
              <tr key={ship.shipmentId}>
                <td>{items.find(item => item.itemId === ship.itemId)?.itemName}</td>
                <td>{ship.quantity}</td>
              </tr>
          ))}
          </tbody>
        </Table>
      </Container>
  );
}

export default IMSShipment;
