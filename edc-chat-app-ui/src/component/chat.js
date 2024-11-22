import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

const Chat = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const webSocketRef = useRef(null);
    const chatContainerRef = useRef(null); // Ref to the chat container for scrolling
    const [chatHistory, setChatHistory] = useState([]);

    const [messages, setMessages] = useState([]); // Chat messages
    const [newMessage, setNewMessage] = useState(""); // Current message to send
    const [error, setError] = useState(null); // Error messages

    const { bpn, selectedValue } = location.state || {};

    const connectWs = () => {
        const wsUrl = "wss://example.com/chat"; // Replace with your WebSocket URL

        // WebSocket connection with BPN in headers (via query string)
        const socket = new WebSocket(`${wsUrl}?bpn=${bpn}`);

        webSocketRef.current = socket;

        // WebSocket event handlers
        socket.onopen = () => console.log("WebSocket connected!");

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data); // Assuming JSON messages
            setMessages((prevMessages) => [...prevMessages, { sender: data.sender, text: data.message }]);
        };

        socket.onerror = () => setError("WebSocket connection error");

        socket.onclose = () => console.log("WebSocket closed");

        // Cleanup WebSocket on component unmount
        return () => {
            if (socket.readyState === WebSocket.OPEN) {
                socket.close();
            }
        };
    };

    const scrollToBottom = () => {
        // Scroll chat window to bottom
        chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    };
    const getChatHistory = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/chat/history`);
            if (response.status === 200) {
                debugger;
                const data = await response.data;
                setChatHistory(data);
                scrollToBottom();
            } else {
                setError("Failed to fetch BPN");
            }
        } catch (err) {
            setError("Network error: Unable to fetch initial config");
        }
    };

    useEffect(() => {
        //validate partner BPN is selected
        if (!bpn || !selectedValue) {
            debugger;
            setError("BPN is missing. Redirecting to home...");
            setTimeout(() => navigate("/"), 3000);
            return;
        } else {
            console.log("bpn -> " + bpn);
            console.log("selected values ->" + selectedValue);
        }

        //get chat history
        getChatHistory();
        connectWs();
    }, [bpn]);

    const handleSendMessage = () => {
        //push
        const message = {
            timestamp: Math.floor(Date.now() / 1000), // Current time in seconds
            content: newMessage,
            sender: bpn,
            receiver: selectedValue,
        };
        setMessages((prevMessages) => [
            ...prevMessages,
            { sender: bpn, text: message.content, timestamp: message.timestamp },
        ]);

        //TODO push message to backend, this should be first
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

        // Clear the input field
        setNewMessage("");

        // Scroll chat window to bottom
        scrollToBottom();
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            // Only trigger on Enter without Shift
            e.preventDefault(); // Prevents a new line from being added
            handleSendMessage(); // Send message when Enter is pressed
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="text-center">Welcome to EDC Chat</h1>

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

            <div className="card p-3 mb-3" style={{ height: "300px", overflowY: "auto" }} ref={chatContainerRef}>
                {[...chatHistory, ...messages].map((msg, index) => {
                    // Determine if the message is sent by the current user
                    const isCurrentUser = msg.sender === bpn;

                    // Format the timestamp (assumes `timestamp` is in seconds)
                    const formattedTimestamp = new Date(msg.timestamp * 1000).toLocaleString();

                    return (
                        <div
                            key={index}
                            className={`d-flex ${isCurrentUser ? "justify-content-end" : "justify-content-start"} mb-2`}
                        >
                            <div className={`p-2 rounded ${isCurrentUser ? "bg-light" : "bg-info text-white"}`}>
                                <strong>{isCurrentUser ? "You" : msg.sender}: </strong> {msg.content || msg.text}
                                <div className="text-muted small">{formattedTimestamp}</div>
                            </div>
                        </div>
                    );
                })}
            </div>

            {error && <div className="alert alert-danger">{error}</div>}

            <div className="d-flex">
                <input
                    type="text"
                    className="form-control me-2"
                    value={newMessage}
                    onKeyDown={handleKeyDown}
                    rows={3}
                    onChange={(e) => setNewMessage(e.target.value)}
                />
                <button className="btn btn-primary" onClick={handleSendMessage}>
                    Send
                </button>
            </div>
        </div>
    );
};

export default Chat;
