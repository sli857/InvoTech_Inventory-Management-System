import {Route, Routes} from 'react-router-dom';

// Import layout and page components
import IMSLayout from './IMSLayout';
import IMSSites from './../content/IMSSites';
import IMSShipments from './../content/IMSShipments';
import IMSNoMatch from './../content/IMSNoMatch';
import IMSSite from '../content/IMSSite';
import IMSAudits from '../content/IMSAudits';
import IMSLogout from '../authorization/IMSLogout';
import IMSUsers from '../content/IMSUsers';
import IMSHelp from '../content/IMSHelp';


/**
 * IMSApp Component
 * Defines the main routes of the IMS application.
 * Uses React Router's <Routes> and <Route> components to set up routing.
 * <IMSLayout> is used as a wrapper for the main content routes, providing consistent layout across pages.
 *
 * Routes:
 * - "/" : Renders the IMSSites component as the homepage.
 * - "/site/:siteId" : Dynamically renders details for a specific site using the IMSSite component.
 * - "/shipments" : Renders the IMSShipments component to show shipment information.
 * - "*" : Catch-all route that renders the IMSNoMatch component for unmatched paths (404).
 *
 * @return {JSX.Element} The router setup for the application.
 */
function IMSApp() {
  return (
    <Routes>
      <Route path="/" element={<IMSLayout />}>
        <Route index element={<IMSSites />} />
        <Route path="/site/:siteId" element={<IMSSite />} />
        <Route path="shipments" element={<IMSShipments />} />
        <Route path="audits" element={<IMSAudits />} />
        <Route path="logout" element={<IMSLogout />} />
        <Route path="users" element={<IMSUsers />} />
        <Route path="help" element={<IMSHelp />} />
        <Route path="*" element={<IMSNoMatch />} />
      </Route>
    </Routes>
  );
}
export default IMSApp;
