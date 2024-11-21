import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const [bpn, setBpn] = useState(null); // Store the fetched BPN
  const [error, setError] = useState(null); // Error messages
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch BPN when the home page loads
    const fetchBpn = async () => {
      try {
        const response = await fetch("https://api.example.com/get-bpn");
        if (response.status === 200) {
          const data = await response.json();
          setBpn(data.bpn); // Save the BPN in state
        } else {
          setError("Failed to fetch BPN");
        }
      } catch (err) {
        setError("Network error: Unable to fetch BPN");
      }
    };

    fetchBpn();
  }, []);

  const handleStartChat = () => {
    if (!bpn) {
      setError("BPN is not available. Please try again later.");
      return;
    }
    navigate("/chat", { state: { bpn } });
  };

  const handleAddPartner = () => {
    if (!bpn) {
      setError("BPN is not available. Please try again later.");
      return;
    }
    navigate("/add-partner", { state: { bpn } });
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center">Chat Application using EDC</h1>

      {bpn ? (
        <div className="alert alert-info">
          <strong>Your BPN: {bpn}</strong>
        </div>
      ) : (
        <div className="alert alert-warning">Fetching your BPN...</div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      <div className="card p-3">
        <div className="mb-3">
          <label>Select Business Partner to start chat</label>
          <select className="form-select">
            <option>Select a partner</option>
            {/* Replace with dynamic dropdown options */}
          </select>
        </div>
        <button className="btn btn-primary w-100" onClick={handleStartChat} disabled={!bpn}>
          Start Chat
        </button>

        <div className="text-center mt-3">OR</div>

        <button className="btn btn-secondary w-100 mt-2" onClick={handleAddPartner}>
          Connect with New Business Partner
        </button>
      </div>
    </div>
  );
};

export default Home;
