const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/chat-websocket',
    connectHeaders: {
        login: "guest",
        passcode: "guest",
        Authorization: 'Bearer xyz'
   }
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/message', (chatMessage) => {
        showMessage(JSON.parse(chatMessage.body).message);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate({
        headers: {
            Authorization: 'Bearer xyz'
        }
    });
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/message",
        body: JSON.stringify({'message': $("#name").val()}),
        headers: {
            Authorization: 'Bearer xyz'
        }
    });
}

function showMessage(message) {
    console.log(message)
    $("#receivedMessage").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});
