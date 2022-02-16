var stompClient = null;
var notificationCount = 0;

$(document).ready(function() {
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendMessage();
    });

    $("#send-private").click(function() {
        sendPrivateMessage();
    });

    $("#notifications").click(function() {
        resetNotificationCount();
    });
});

function connect() {
    var socket = new SockJS('/our-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        updateNotificationDisplay();

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            showMessage(JSON.parse(message.body));
        });

        stompClient.subscribe('/user/topic/private-notifications', function (message) {
            notificationCount = notificationCount + 1;
            updateNotificationDisplay();
        });
    });
}

function showMessage(message) {
    $("#messages").append("<tr>" +
        "<td>" + message.sender + "</td>" +
        "<td>" + message.subject + "</td>" +
        "<td>" + message.full_text + "</td>" +
        "</tr>");

}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/private-message", {}, JSON.stringify({'receiver': $("#receiver").val(), 'subject': $("#subject").val(), 'full_text': $("#message").val()}));
}

function updateNotificationDisplay() {
    if (notificationCount == 0) {
        $('#notifications').hide();
    } else {
        $('#notifications').show();
        $('#notifications').text(notificationCount);
    }
}

function resetNotificationCount() {
    notificationCount = 0;
    updateNotificationDisplay();
}