import { useEffect, useState } from 'react';
import { Table } from 'react-bootstrap';

/**
 * This Page is made up and I am not sure if it will be used at all.
 * 
 * @returns 
 */
function IMSWarehouses() {
  const [items, setItems] = useState([]);
  const [availabilities, setAvailabilities] = useState([]);


  useEffect(() => {
    fetch("http://localhost:8080/items")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setItems(data);
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
  }, []);

  useEffect(() => {
    fetch("http://localhost:8080/api/availabilities")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setAvailabilities(data);
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
  }, []);
  // A function to get the total availability of an item across all sites
  const getTotalAvailability = (itemId) => {
    return availabilities.filter(a => a.itemId === itemId)
      .reduce((acc, current) => acc + current.quantity, 0);
  };

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Item ID</th>
          <th>Item Name</th>
          <th>Total Availability Across Sites</th>
        </tr>
      </thead>
      <tbody>
        {items.map(item => (
          <tr key={item.itemId}>
            <td>{item.itemId}</td>
            <td>{item.itemName}</td>
            <td>{getTotalAvailability(item.itemId)}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

export default IMSWarehouses;