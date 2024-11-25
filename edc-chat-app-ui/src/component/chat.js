import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { Client } from "@stomp/stompjs";

const Chat = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const chatContainerRef = useRef(null); // Ref to the chat container for scrolling
    const [chatHistory, setChatHistory] = useState([]);
    const [connected, setConnected] = useState(false); // Connection status
    const [stompClient, setStompClient] = useState(null); // Stomp client instance
    const [messages, setMessages] = useState([]); // Chat messages
    const [newMessage, setNewMessage] = useState(""); // Current message to send
    const [error, setError] = useState(null); // Error messages

    const [errorPopup, setErrorPopup] = useState({ isVisible: false, errorMessage: "" });

    const { bpn: selfBpn, selectedValue: partnerBpn } = location.state || {};

    useEffect(() => {

        //validate partner BPN is selected
        if (!selfBpn || !partnerBpn) {
            setError("BPN is missing. Redirecting to home...");
            setTimeout(() => navigate("/"), 1000);
            return;
        } else {
            // console.log("bpn -> " + selfBpn);
            // console.log("selected values ->" + partnerBpn);
        }

        //get chat history
        getChatHistory();

        //connect to WS for message transfer
        const client = new Client({
        
            webSocketFactory: () => new WebSocket(`${process.env.REACT_APP_WEBSOCKET_URL}/ws-chat`), // Replace with your backend WebSocket endpoint
            
            onConnect: () => {
                console.log("Connected to WebSocket");
                setConnected(true);
                
                client.subscribe("/topic/messages", (msg) => {
                    const newMessage = JSON.parse(msg.body);
                    if(newMessage.receiver == partnerBpn){
            
                        setMessages((prevMessages) => [
                            ...prevMessages,
                            { sender: newMessage.receiver, text: newMessage.content, timestamp: newMessage.timestamp },
                        ]);
                        scrollToBottom()
                    }

                });
            },
            onStompError: (frame) => {
                console.error("WebSocket error:", frame.headers["message"], frame.body);
                setConnected(false);
            },
           
        });

        client.activate(); // Connect to WebSocket
        setStompClient(client);

        // Cleanup WebSocket on component unmount
        return () => {
            client.deactivate(); // Cleanup on unmount
        };

    }, [selfBpn]);


    const handleMessageClick = (msg) => {
        // Show popup only if there is a valid errorMessage
        if (msg.status !== "SENT" && msg.errorMessage) {
            setErrorPopup({ isVisible: true, errorMessage: msg.errorMessage });
        }
    };

    const closePopup = () => {
        setErrorPopup({ isVisible: false, errorMessage: "" });
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
                // console.log(response.data);
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

                    // Format the timestamp (assumes `timestamp` is in milliseconds)
                    const formattedTimestamp = new Date(msg.timestamp).toLocaleString();

                    // Determine status and its styling
                    const status = msg.status;
                    const showStatus = status && status !== "NONE";
                    const statusColor = status === "SENT" ? "text-success" : "text-danger";
                    const statusText = status === "SENT" ? "Sent" : "Failed";

                    return (
                        <div
                            key={index}
                            className={`d-flex ${isCurrentUser ? "justify-content-end" : "justify-content-start"} mb-2`}
                        >
                            <div
                                className={`p-2 rounded ${isCurrentUser ? "bg-light" : "bg-info text-white"}`}
                                onClick={() => handleMessageClick(msg)} // Add click handler
                                style={{ cursor: status !== "SENT" && msg.errorMessage ? "pointer" : "default" }}
                            >
                                <strong>{isCurrentUser ? "You" : msg.sender}: </strong> {msg.content || msg.text}
                                <div className="text-muted small">{formattedTimestamp}</div>
                                {/* Conditionally render status */}
                                {showStatus && <div className={`small ${statusColor}`}>{statusText}</div>}
                            </div>
                        </div>
                    );
                })}
            </div>
            {/* Error Popup */}
            {errorPopup.isVisible && (
                <div
                    className="modal fade show d-block"
                    style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
                    onClick={closePopup}
                >
                    <div
                        className="modal-dialog modal-lg modal-dialog-centered"
                        onClick={(e) => e.stopPropagation()} // Prevent modal close when clicking inside
                    >
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Error Message</h5>
                                <button type="button" className="btn-close" onClick={closePopup}></button>
                            </div>
                            <div className="modal-body">
                                <p>{errorPopup.errorMessage}</p>
                            </div>
                            <div className="modal-footer">
                                <button className="btn btn-primary" onClick={closePopup}>
                                    Close
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

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