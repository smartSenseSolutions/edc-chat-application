import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Home = () => {
    const [bpn, setBpn] = useState(null); // Store the fetched BPN
    const [error, setError] = useState(null); // Error messages
    const navigate = useNavigate();
    const [dropdownData, setDropdownData] = useState([]);
    const [selectedValue, setSelectedValue] = useState("");

    useEffect(() => {
        fetchConfig();
        fetchDropdownData();
    }, []);

    //Fetch BPN when the home page loads
    const fetchConfig = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/config`);
            if (response.status === 200) {
                const data = await response.data;
                setBpn(data.bpn); // Save the BPN in state
            } else {
                setError("Failed to fetch BPN");
            }
        } catch (err) {
            setError("Network error: Unable to fetch initial config");
        }
    };

    const fetchDropdownData = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/partners`);
            const apiResponse = response.data;
            // Transform response into key-value pairs for dropdown
            const dropdownItems = apiResponse.map((item) => ({
                label: `${item.name} - ${item.bpn}`,
                value: item.bpn,
            }));
            setDropdownData(dropdownItems);
        } catch (error) {
            console.error("Error fetching dropdown data:", error);
        }
    };
    const handleStartChat = () => {
        console.log("selected BPN -" + selectedValue);
        if (!bpn) {
            setError("BPN is not available. Please try again later.");
            return;
        }
        if (!selectedValue) {
            document.getElementById("dropdown").style.borderColor = "red";
            setError("Please select business partner to start chat.");
            return;
        }
        document.getElementById("dropdown").style.borderColor = "";

        navigate("/chat", { state: { bpn, selectedValue } });
    };

    const handleAddPartner = () => {
        if (!bpn) {
            setError("BPN is not available. Please try again later.");
            return;
        }
        navigate("/add-business-partner", { state: { bpn } });
    };

    const handleSelectChange = (event) => {
        setSelectedValue(event.target.value);
        console.log("Selected value:", event.target.value);
    };

    return (
        <div className="form-container mt-5">
            {/* <h1 className="text-center">Chat Application using EDC</h1> */}

            {bpn ? (
                <div className="bpnInfo">
                    <strong>Your BPN: {bpn}</strong>
                </div>
            ) : (
                <div className="bpnInfo">Fetching your BPN...</div>
            )}

            {error && <div className="bpnInfo error">{error}</div>}

            <div className="card p-3 bgBlack" style={{ color: "white" }}>
                <div className="mb-3">
                    <label>Select Business Partner to start chat</label>
                    <select
                        id="dropdown"
                        className="form-control custom-select-form-control"
                        value={selectedValue}
                        onChange={handleSelectChange}
                    >
                        <option value="">-- Select an Option --</option>
                        {dropdownData.map((item, index) => (
                            <option key={index} value={item.value}>
                                {item.label}
                            </option>
                        ))}
                    </select>
                </div>
                <button className="btn btn-primary primary-btn w-100" onClick={handleStartChat} disabled={!bpn}>
                    Start Chat
                </button>

                <div className="text-center mt-3">OR</div>

                <button className="btn btn-secondary w-100 mt-2 outline-btn" onClick={handleAddPartner}>
                    Connect with New Business Partner
                </button>
            </div>
        </div>
    );
};

export default Home;
