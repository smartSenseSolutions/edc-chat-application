import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const Chat = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const webSocketRef = useRef(null);

  const [messages, setMessages] = useState([]); // Chat messages
  const [newMessage, setNewMessage] = useState(""); // Current message to send
  const [error, setError] = useState(null); // Error messages

  const { bpn } = location.state || {}; // Get BPN from navigation state

  useEffect(() => {
    if (!bpn) {
      setError("BPN is missing. Redirecting to home...");
      setTimeout(() => navigate("/"), 3000);
      return;
    }

    const wsUrl = "wss://example.com/chat"; // Replace with your WebSocket URL

    // WebSocket connection with BPN in headers (via query string)
    const socket = new WebSocket(`${wsUrl}?bpn=${bpn}`);

    webSocketRef.current = socket;

    // WebSocket event handlers
    socket.onopen = () => console.log("WebSocket connected!");

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data); // Assuming JSON messages
      setMessages((prevMessages) => [
        ...prevMessages,
        { sender: data.sender, text: data.message },
      ]);
    };

    socket.onerror = () => setError("WebSocket connection error");

    socket.onclose = () => console.log("WebSocket closed");

    // Cleanup WebSocket on component unmount
    return () => {
      if (socket.readyState === WebSocket.OPEN) {
        socket.close();
      }
    };
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

  return (
    <div className="container mt-5">
      <h1 className="text-center">Chat</h1>

      {/* Display BPN */}
      {bpn && (
        <div className="alert alert-info">
          <strong>Your BPN: {bpn}</strong>
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
          value={newMessage}
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
