import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const AddBpn = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { bpn } = location.state || {}; // Get BPN from navigation state
  const [formData, setFormData] = useState({ name: "", edcUrl: "", bpn: "" });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = async () => {
    const requestData = { ...formData, bpn }; // Include BPN in the request body

    try {
      const response = await fetch("https://api.example.com/add-partner", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      });

      if (response.status === 200) {
        setSuccess("Business partner added successfully!");
        setTimeout(() => navigate("/"), 2000); // Redirect to Screen1 after success
      } else {
        setError("Failed to save business partner. Please try again.");
      }
    } catch (err) {
      setError("Network error: Unable to save business partner.");
    }
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center">Add New Business Partner</h1>

      {bpn && (
        <div className="alert alert-info">
          <strong>Your BPN: {bpn}</strong>
        </div>
      )}

      {success && <div className="alert alert-success">{success}</div>}
      {error && <div className="alert alert-danger">{error}</div>}

      <div className="card p-3">
        <div className="mb-3">
          <label>Name</label>
          <input
            type="text"
            name="name"
            className="form-control"
            value={formData.name}
            onChange={handleInputChange}
          />
        </div>
        <div className="mb-3">
          <label>BPN</label>
          <input
            type="text"
            name="bpn"
            className="form-control"
            value={bpn || ""}
            disabled
          />
        </div>
        <div className="mb-3">
          <label>EDC URL</label>
          <input
            type="text"
            name="edcUrl"
            className="form-control"
            value={formData.edcUrl}
            onChange={handleInputChange}
          />
        </div>
        <button className="btn btn-primary w-100" onClick={handleSave}>
          Save
        </button>
      </div>
    </div>
  );
};

export default AddBpn;
