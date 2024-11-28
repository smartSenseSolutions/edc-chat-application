/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./component/home";
import Chat from "./component/chat";
import AddBpn from "./component/add-bpn";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/chat" element={<Chat />} />
                <Route path="/add-business-partner" element={<AddBpn />} />
            </Routes>
        </Router>
    );
}

export default App;
