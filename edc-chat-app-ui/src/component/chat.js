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

    const { bpn: selfBpn, selectedValue: partnerBpn } = location.state || {};

    useEffect(() => {
        //validate partner BPN is selected
        if (!selfBpn || !partnerBpn) {
            setError("BPN is missing. Redirecting to home...");
            setTimeout(() => navigate("/"), 3000);
            return;
        } else {
            console.log("bpn -> " + selfBpn);
            console.log("selected values ->" + partnerBpn);
        }

        //get chat history
        getChatHistory();

        //connect to WS for message transfer
        connectWs();
    }, [selfBpn]);

    const connectWs = () => {
        const wsUrl = "wss://example.com/chat"; // Replace with your WebSocket URL

        // WebSocket connection with BPN in headers (via query string)
        const socket = new WebSocket(`${wsUrl}?bpn=${selfBpn}&partnerBpn=${partnerBpn}`);

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
        const lastMessage = chatContainerRef.current?.lastElementChild;
        if (lastMessage) {
            setTimeout(() => {
                lastMessage.scrollIntoView({ behavior: "smooth" });
            }, 10);
        }
    };
    const getChatHistory = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_API_BASE_URL}/chat/history?partnerBpn=${partnerBpn}`
            );
            if (response.status === 200) {
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

    const handleSendMessage = () => {
        if (!newMessage.trim()) {
            // Highlight the input box in red if the message is empty
            document.getElementById("messageInput").style.borderColor = "red";
            return; // Prevent sending an empty message
        }

        // Reset the border color if the message is valid
        document.getElementById("messageInput").style.borderColor = "";

        //send message to backend
        const chatRequest = {
            message: newMessage,
            receiverBpn: partnerBpn,
        };
        const response = axios
            .post(`${process.env.REACT_APP_API_BASE_URL}/chat`, JSON.stringify(chatRequest), {
                headers: {
                    "Content-Type": "application/json",
                },
            })
            .then((response) => {
                console.log(response.data);
                if (response.status === 200) {
                    const message = {
                        timestamp: Math.floor(Date.now() / 1000), // Current time in seconds
                        content: newMessage,
                        sender: selfBpn,
                        receiver: partnerBpn,
                    };
                    setMessages((prevMessages) => [
                        ...prevMessages,
                        { sender: selfBpn, text: message.content, timestamp: message.timestamp },
                    ]);

                    // Clear the input field
                    setNewMessage("");

                    // Scroll chat window to bottom
                    scrollToBottom();
                } else {
                    setError("Failed to send message");
                } // Print the response in the console
            })
            .catch((error) => {
                setError("Network error: Failed to send message.");
            });
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
            <div className="d-flex justify-content-between alert alert-info">
                {selfBpn && (
                    <div className="text-start">
                        <strong>Your BPN: {selfBpn}</strong>
                    </div>
                )}

                {partnerBpn && (
                    <div className="text-end">
                        <strong>Partner BPN: {partnerBpn}</strong>
                    </div>
                )}
            </div>
            <div className="card p-3 mb-3" style={{ height: "400px", overflowY: "auto" }} ref={chatContainerRef}>
                {[...chatHistory, ...messages].map((msg, index) => {
                    // Determine if the message is sent by the current user
                    const isCurrentUser = msg.sender === selfBpn;

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
                    id="messageInput"
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
