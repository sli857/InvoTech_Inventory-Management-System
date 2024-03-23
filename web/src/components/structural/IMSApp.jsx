import {Route, Routes } from "react-router-dom";

// Import layout and page components
import IMSLayout from "./IMSLayout";
import IMSHome from "./../content/IMSHome"
import IMSWarehouses from "./../content/IMSWarehouses"
import IMSShipments from "./../content/IMSShipments"
import IMSNoMatch from "./../content/IMSNoMatch"
import IMSSite from "../content/IMSSite";

/**
 * IMSApp Component
 * Defines the main routes of the IMS application.
 * Uses React Router's <Routes> and <Route> components to set up routing.
 * <IMSLayout> is used as a wrapper for the main content routes, providing consistent layout across pages.
 * 
 * Routes:
 * - "/" : Renders the IMSHome component as the homepage.
 * - "/site/:siteId" : Dynamically renders details for a specific site using the IMSSite component.
 * - "/shipments" : Renders the IMSShipments component to show shipment information.
 * - "/warehouses" : Renders the IMSWarehouses component to show warehouse information. THIS PAGE WILL NOT BE USED
 * - "*" : Catch-all route that renders the IMSNoMatch component for unmatched paths (404).
 * 
 * @returns {JSX.Element} The router setup for the application.
 */
function IMSApp() {
  return (
    <Routes>
      <Route path="/" element={<IMSLayout />}>
        <Route index element={<IMSHome />} />
        <Route path="/site/:siteId" element={<IMSSite />} />
        <Route path="shipments" element={<IMSShipments />} />
        <Route path="warehouses" element={<IMSWarehouses />} />
        <Route path="*" element={<IMSNoMatch />} />
      </Route>
    </Routes>
  );
}
export default IMSApp;
