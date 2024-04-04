import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import IMSApp from "./components/structural/IMSApp";
import IMSLogin from "./components/authorization/IMSLogin"; 
import IMSLogout from "./components/authorization/IMSLogout"; 

/**
 * Main application component that handles routing and authentication.
 * Utilizes React Router for navigation between the login page and the main app interface.
 * It maintains an authentication state to determine access to the IMSApp component.
 * If the user is not authenticated, they are redirected to the login page.
 *
 * @returns {JSX.Element} The App component with routing configured.
 */
function App() {
  // State to track if the user is authenticated
  const [isAuthenticated, setIsAuthenticated] = useState(false);


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<IMSLogin setAuth={setIsAuthenticated} />} />
        { /* If the authentication is succesfull route to the home page, otherwise route to login page */}
        <Route path="/logout" element={<IMSLogout setAuth={setIsAuthenticated} />} />
        <Route path="/*" element={isAuthenticated ? <IMSApp /> : <Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
