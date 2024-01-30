const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws',
    connectHeaders: {
        Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NjQ3NzMsInVzZXJfaWQiOjF9.o2l9ZN4aCGhYMEslxwv6rtc-y7Oi8G_nz9OlfNk7kJk',
    }
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/locations/user/1', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        showGreeting(greeting.body);
    });
    stompClient.subscribe('/topic/friend-locations', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        showGreeting(greeting.body);
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
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/update-location",
        body: JSON.stringify({'userId': 1, 'lat': 37.123, 'lng': 127.123})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});
