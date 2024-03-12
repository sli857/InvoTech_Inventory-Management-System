import {useEffect, useState} from "react";

function IMSHome() {

    const [sites, setSites] = useState([]);
    const [siteName, setSiteName] = useState('');
    const [siteLocation, setLocation] = useState('');
    const [siteStatus, setStatus] = useState('');
    const [ceaseDate, setCeaseDate] = useState('');
    const [internalSite, setInternalSite] = useState(false);
    // fetching the sites from the database
    useEffect(() => {
        fetch("http://localhost:8080/sites")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                setSites(data);
                console.log(data);
            })
            .catch((error) => {
                console.error("There was a problem with the fetch operation:", error);
            });
    }, []);

    async function handleSubmit(event) {
        event.preventDefault(); // Prevent default form submission

        const response = await fetch("http://localhost:8080/sites/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                siteName: siteName,
                siteLocation: siteLocation,
                siteStatus: siteStatus,
                ceaseDate: ceaseDate,
                internalSite: internalSite,
            }),
        });
        if (response.status === 200) {
            console.log("success");
            // Refresh sites data after adding a new site
            fetchSites();
        }
    }

    const fetchSites = async () => {
        try {
            const response = await fetch("http://localhost:8080/sites");
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            const data = await response.json();
            setSites(data);
        } catch (error) {
            console.error("There was a problem with fetching sites:", error);
        }
    };

    return (

        <div style={{padding: '20px'}}>
            <h2>Sites Inventory</h2>
            <table style={{width: '100%', borderCollapse: 'collapse'}}>
                <thead>
                <tr>
                    <th style={{border: '1px solid black', padding: '8px'}}>ID</th>
                    <th style={{border: '1px solid black', padding: '8px'}}>Site Name</th>
                    <th style={{border: '1px solid black', padding: '8px'}}>Location</th>
                    <th style={{border: '1px solid black', padding: '8px'}}>Status</th>
                    <th style={{border: '1px solid black', padding: '8px'}}>Cease Date</th>
                    <th style={{border: '1px solid black', padding: '8px'}}>Internal Site</th>
                </tr>
                </thead>
                <tbody>
                {sites.map((site) => (
                    <tr key={site.id}>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.id}</td>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.siteName}</td>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.siteLocation}</td>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.siteStatus}</td>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.ceaseDate || 'N/A'}</td>
                        <td style={{border: '1px solid black', padding: '8px'}}>{site.internalSite ? 'Yes' : 'No'}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Site Name:</label>
                    <input
                        type="text"
                        value={siteName}
                        onChange={(e) => setSiteName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Location:</label>
                    <input
                        type="text"
                        value={siteLocation}
                        onChange={(e) => setLocation(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Status:</label>
                    <input
                        type="text"
                        value={siteStatus}
                        onChange={(e) => setStatus(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Cease Date:</label>
                    <input
                        type="date"
                        value={ceaseDate}
                        onChange={(e) => setCeaseDate(e.target.value)}
                    />
                </div>
                <div>
                    <label>
                        Internal Site:
                        <input
                            type="checkbox"
                            checked={internalSite}
                            onChange={(e) => setInternalSite(e.target.checked)}
                        />
                    </label>
                </div>
                <button type="submit">Add Site</button>
            </form>
        </div>
    );
}

export default IMSHome;