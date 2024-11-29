/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

import axios from "axios";
import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

const AddBpn = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const { bpn: selfBpn } = location.state || {}; // Get BPN from navigation state
    const [formData, setFormData] = useState({ name: "", edcUrl: "", bpn: "" });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [validationErrors, setValidationErrors] = useState({
        name: false,
        bpn: false,
        edcUrl: false,
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
        setValidationErrors({ ...validationErrors, [name]: false }); // Reset validation error on change
    };

    const handleSave = async () => {
        // Check if any field is empty
        const { name, bpn, edcUrl } = formData;
        let isValid = true;

        // Set validation errors for empty fields
        const errors = { name: false, bpn: false, edcUrl: false };
        if (!name.trim()) {
            errors.name = true;
            isValid = false;
        }
        if (!bpn.trim()) {
            errors.bpn = true;
            isValid = false;
        }
        if (!edcUrl.trim()) {
            errors.edcUrl = true;
            isValid = false;
        }
        setValidationErrors(errors);

        // If form is not valid, return early and show error message
        if (!isValid) {
            setError("All fields are required.");
            return;
        }

        setError(null); // Clear

        const requestData = { ...formData }; // Include BPN in the request body

        try {
            const response = await axios.post(
                `${process.env.REACT_APP_API_BASE_URL}/partners`,
                JSON.stringify(requestData),
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );
            if (response.status === 200) {
                setSuccess("Business partner added successfully!");
                setTimeout(() => navigate("/"), 1000); // Redirect to Screen1 after success
            } else {
                setError("Failed to save business partner. Please try again.");
            }
        } catch (err) {
            if (err.response.status == 409 || err.response.status == 400) {
                setError(err.response.data.title);
            } else {
                setError("Network error: Unable to save business partner.");
            }
        }
    };
    return (
        <div className="form-container  mt-5">
            <Link to="/" className="backBtn">
                &lt;&nbsp;Back
            </Link>
            <h2 className="text-center">Add New Business Partner</h2>

            {selfBpn && (
                <div className="bpnInfo">
                    <strong>Your BPN: {selfBpn}</strong>
                </div>
            )}

            {success && <div className="alert alert-success">{success}</div>}
            {error && <div className="alert alert-danger">{error}</div>}

            <div className="custom-form">
                <div className="mb-3">
                    <label>Name</label>
                    <input
                        type="text"
                        name="name"
                        placeholder="Your company name"
                        className="form-control custom-form-contol "
                        value={formData.name}
                        onChange={handleInputChange}
                        style={{
                            borderColor: validationErrors.name ? "red" : "",
                        }}
                    />
                </div>
                <div className="mb-3">
                    <label>BPN</label>
                    <input
                        type="text"
                        name="bpn"
                        value={formData.bpn}
                        className="form-control custom-form-contol "
                        placeholder="BPNL000000000000"
                        onChange={handleInputChange}
                        style={{
                            borderColor: validationErrors.bpn ? "red" : "",
                        }}
                    />
                </div>
                <div className="mb-3">
                    <label>EDC URL</label>
                    <input
                        type="text"
                        name="edcUrl"
                        placeholder="http://your-controlplane-host/api/v1/dsp"
                        className="form-control custom-form-contol"
                        value={formData.edcUrl}
                        onChange={handleInputChange}
                        style={{
                            borderColor: validationErrors.edcUrl ? "red" : "",
                        }}
                    />
                </div>
                <button className="btn btn-primary primary-btn w-100" onClick={handleSave}>
                    Save
                </button>
            </div>
        </div>
    );
};
export default AddBpn;
