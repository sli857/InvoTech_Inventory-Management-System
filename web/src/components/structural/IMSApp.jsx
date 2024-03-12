import {BrowserRouter, Route, Routes} from "react-router-dom";

import IMSLayout from "./IMSLayout";
import IMSHome from "./../content/IMSHome"
import IMSWarehouses from "./../content/IMSWarehouses"
import IMSShipments from "./../content/IMSShipments"
import IMSNoMatch from "./../content/IMSNoMatch"

function IMSApp() {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<IMSLayout /*sites={sites}*/ />}>
                    <Route index element={<IMSHome/>}/>
                    <Route path="/shipments" element={<IMSShipments/>}></Route>
                    <Route path="/warehouses" element={<IMSWarehouses/>}></Route>
                    <Route path="*" element={<IMSNoMatch/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default IMSApp;