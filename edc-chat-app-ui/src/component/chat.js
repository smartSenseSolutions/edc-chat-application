import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
const Chat = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const webSocketRef = useRef(null);

    const [messages, setMessages] = useState(""); // Chat messages
    const [newMessage, setNewMessage] = useState(""); // Current message to send
    const [error, setError] = useState(null); // Error messages

    const { bpn, selectedValue } = location.state || {};




    const [receiver, setReceiver] = useState("");
    const [client, setClient] = useState(null);
    const [message, setMessage] = useState("");

    useEffect(() => {
        if (!bpn || !selectedValue) {
            debugger;
            setError("BPN is missing. Redirecting to home...");
            setTimeout(() => navigate("/"), 3000);
            return;
        } else {
            console.log("bpn -> " + bpn);
            console.log("selected values ->" + selectedValue);
        }

        // WebSocket connection with BPN in headers (via query string)
        const socket = new SockJS("http://localhost:8081/chat");
        const stompClient = new Client({
            webSocketFactory: () => socket,
            debug: console.log,
            onConnect: () => {
                console.log("Connected to WebSocket");
                stompClient.subscribe("/user/queue/messages", (msg) => {
                    const newMessage = JSON.parse(msg.body);
                    setMessages((prev) => [...prev, newMessage]);
                });
            },
        });
        stompClient.activate();
        setClient(stompClient);

        return () => stompClient.deactivate();
    }, [bpn]);

    const handleSendMessage = () => {
        if (webSocketRef.current && webSocketRef.current.readyState === WebSocket.OPEN) {
            const message = {
                sender: "user", // Customize as needed
                message: newMessage,
            };
            webSocketRef.current.send(JSON.stringify(message));
            setMessages((prevMessages) => [...prevMessages, { sender: "You", text: newMessage }]);
            setNewMessage(""); // Clear input field
        } else {
            setError("WebSocket is not connected");
        }
    };

    const sendMessage = () => {
        if (client && receiver) {
            client.publish({
                destination: "/app/privateMessage",
                body: JSON.stringify({
                    senderBpn: "BPNL000000000000", 
                    receiverBpn:"BPNL000000000001",
                    message: message,
                    messageId:"1",
                }),
            });
            setMessage("");
        }
    };
    

    return (
        <div className="container mt-5">
            <h1 className="text-center">Chat</h1>

            {/* Display BPN */}
            {bpn && (
                <div className="alert alert-info">
                    <strong>Your BPN: {bpn}</strong>
                </div>
            )}

            {bpn && (
                <div className="alert alert-info">
                    <strong>Partner BPN: {selectedValue}</strong>
                </div>
            )}

            <div className="card p-3 mb-3" style={{ height: "300px", overflowY: "auto" }}>
                {messages.map((msg, index) => (
                    <p key={index} className={msg.sender === "You" ? "text-end" : "text-start"}>
                        <strong>{msg.sender}: </strong> {msg.text}
                    </p>
                ))}
            </div>

            {error && <div className="alert alert-danger">{error}</div>}

            <div className="d-flex">
                <input
                    type="text"
                    className="form-control me-2"
                    value={messages}
                    onChange={(e) => setMessage(e.target.value)}
                />
                <button className="btn btn-primary" onClick={sendMessage}>
                    Send
                </button>
            </div>
        </div>
    );
};

export default Chat;
