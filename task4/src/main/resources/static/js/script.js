var stompClient = null;
var notificationCount = 0;

$(document).ready(function() {
    console.log("Index page is ready");
    connect();

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
    $("#messages").prepend(
        "<div class=\"card mb-2 p-3 shadow around\">\n" +
        "                <div class=\"row mb-2\" data-bs-toggle=\"collapse\" href=\"#collapseExample" + message.id + "\" role=\"button\" aria-expanded=\"false\" aria-controls=\"collapseExample\">\n" +
        "                    <div class=\"col\">" +
        message.sender + "</div>\n" +
        "                    <div class=\"col\">" +
        message.subject + "</div>\n" +
        "                    <div class=\"col\">" +
        message.time + "</div>\n" +
        "                    <div class=\"collapse mt-2\" id=\"collapseExample" + message.id + "\">\n" +
        "                        <div class=\"fw-bolder\">\n" +
        "                            Message text:\n" +
        "                        </div>\n" +
        "                        <div>" +
        message.full_text + "</div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>"
    );
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