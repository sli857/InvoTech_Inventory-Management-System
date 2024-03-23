import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Table } from 'react-bootstrap';

function IMSSite() {
  const [items, setItems] = useState([]);
  const { siteId } = useParams();

  useEffect(() => {
    fetch(`http://localhost:8080/availabilities?siteId=${siteId}`)
      .then(response => response.json())
      .then(data => { setItems(data); console.log(data); })
      .catch(error => console.error("Failed to fetch items", error));
  }, [siteId]);

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Item ID</th>
          <th>Item Name</th>
          <th>Quantity Available</th>
        </tr>
      </thead>
      <tbody>
        {items.map(item => (
          <tr key={item.itemId}>
            <td>{item.itemId}</td>
            <td>{item.itemName}</td> 
            <td>{item.quantity}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

export default IMSSite;