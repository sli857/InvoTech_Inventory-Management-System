import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx'; 

/**
 * Entry point for the React application.
 * Utilizes ReactDOM to render the App component into the root DOM node.
 * React.StrictMode is applied to the App component to identify potential problems in an application.
 * StrictMode does not render any visible UI. It activates additional checks and warnings for its descendants.
 */
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
