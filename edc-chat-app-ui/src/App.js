import React from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import Home from "./component/home";
import Chat from "./component/chat";
import AddBpn from "./component/add-bpn";

function App() {
    return (
        <Router>
            <div className="main-container">
                <div className="header">
                    <Link to={"/"} className="edcLogo">
                        <img src={"/edclogo.svg"} width={"44"} height={"42"} />
                        <span>Eclipse Tractus-X</span>
                    </Link>

                    <h1 className="text-center">Chat Application using EDC</h1>
                </div>
                <div className="content">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/chat" element={<Chat />} />
                        <Route path="/add-business-partner" element={<AddBpn />} />
                    </Routes>
                </div>
                <div className="footer">
                    <span>Developed By:</span>{" "}
                    <a href="https://smartsensesolutions.com/" target="_blank">
                        <img src={"/smartSense-logo.svg"} width={"300"} height={"100"} />
                    </a>
                </div>
            </div>
        </Router>
    );
}

export default App;
