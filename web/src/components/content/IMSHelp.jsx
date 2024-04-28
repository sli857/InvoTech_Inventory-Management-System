import React, { useState } from 'react';

function IMSHelp() {
  // State to keep track of the currently active accordion section
  const [activeIndex, setActiveIndex] = useState(null);

  // Function to toggle the accordion section
  const toggleAccordion = (index) => {
    // Set the active index to the selected index, or null if it's already active
    setActiveIndex(activeIndex === index ? null : index);
  };

  // Styles object containing CSS styles for various parts of the component
  const styles = {
    accordionItem: {
      marginBottom: '10px',
      boxShadow: '0 2px 5px rgba(0,0,0,0.1)'
    },
    accordionButton: {
      background: '#f7f7f7',
      border: 'none',
      outline: 'none',
      width: '100%',
      textAlign: 'left',
      fontSize: '16px',
      padding: '15px 20px',
      cursor: 'pointer',
      borderBottom: '1px solid #ddd'
    },
    accordionContent: {
      maxHeight: '0',
      overflow: 'hidden',
      transition: 'max-height 0.3s ease',
      background: '#fff',
      padding: '0 20px',
      fontSize: '14px'
    },
    accordionContentActive: {
      maxHeight: '150px',
      padding: '10px 20px'
    }
  };

  return (
    <div>
      <h1>Help Page</h1>
      <section>
        <h2>Frequently Asked Questions</h2>
        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(0)}>
            <strong>How do I reset my password?</strong>
          </button>
          <div style={activeIndex === 0 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>To reset your password, you will need to contact management for further instruction.</p>
          </div>
        </div>

        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(1)}>
            <strong>How do I find other user?</strong>
          </button>
          <div style={activeIndex === 1 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>You can navigate to the User Page and search a User by username or position.</p>
          </div>
        </div>

        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(2)}>
            <strong>How do I change my position?</strong>
          </button>
          <div style={activeIndex === 2 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>Only Admin is allowed to make position changes.</p>
          </div>
        </div>

        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(3)}>
            <strong>How do I find a shipment?</strong>
          </button>
          <div style={activeIndex === 3 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>You can navigate to the Shipment Page and search the shipment you are looking for.</p>
          </div>
        </div>

        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(4)}>
            <strong>How do I find a site?</strong>
          </button>
          <div style={activeIndex === 4 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>You can navigate to the Site Page and search the Site you are looking for.
            The Site page also provides a visual view of the site on the map.</p>
          </div>
        </div>

        <div style={styles.accordionItem}>
          <button style={styles.accordionButton} onClick={() => toggleAccordion(5)}>
            <strong>How can I get technical support?</strong>
          </button>
          <div style={activeIndex === 5 ?
          {...styles.accordionContent, ...styles.accordionContentActive} : styles.accordionContent}>
            <p>Our technical support team is available to assist you with any queries or issues you may encounter.
            You can contact us through several methods: Email us at ims@invotech.com.</p>
          </div>
        </div>
      </section>

      <section>
        <h2>Contact Us</h2>
        <p>If you need further assistance, please contact our support team at ims@invotech.com.</p>
      </section>
    </div>
  );
}

export default IMSHelp;
