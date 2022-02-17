var stompClient = null;
var notificationCount = 0;

$(document).ready(function() {
    console.log("Index page is ready");
    connect();

    $("#send-private").click(function() {
        sendPrivateMessage();
        document.getElementById("receiver").value="";
        document.getElementById("subject").value="";
        document.getElementById("message").value="";
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

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            showMessage(JSON.parse(message.body));
            createToast(message);
        });
    });
}

function createToast (message) {
    $("#toasts").prepend(
        "<div class=\"toast fade show\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\">\n" +
        "        <div class=\"toast-header\">\n" +
        "            <strong class=\"me-auto\">task4 project</strong>\n" +
        "            <small class=\"text-muted\">" + timeFormat(new Date) + "</small>\n" +
        "            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"toast\" aria-label=\"Close\"></button>\n" +
        "        </div>\n" +
        "        <div class=\"toast-body\">\n" +
        "            New message from: \n" + JSON.parse(message.body).sender +
        "        </div>\n" +
        "    </div>"
    );
    setTimeout(function (){$("#toasts").children().first().remove()}, 5000);
}

function timeFormat(time) {
    let date = new Date(time);
    return  date.getDate().toString().padStart(2,'0') + "." + (date.getMonth()+1).toString().padStart(2,'0') + "." + date.getFullYear() + " " + date.getHours().toString().padStart(2, '0') + ":" + date.getMinutes().toString().padStart(2,'0')+ ":" + date.getSeconds().toString().padStart(2, '0');
}

function showMessage(message) {
    $("#messages").prepend(
        "<div class=\"card mb-2 p-3 shadow around new-message border border-primary\">\n" +
        "                <div class=\"row mb-2\" data-bs-toggle=\"collapse\" href=\"#collapseExample" + message.id + "\" role=\"button\" aria-expanded=\"false\" aria-controls=\"collapseExample\">\n" +
        "                    <div class=\"col\">" +
        message.sender + "</div>\n" +
        "                    <div class=\"col\">" +
        message.subject + "</div>\n" +
        "                    <div class=\"col\">" +
        timeFormat(message.time) + "</div>\n" +
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
    $(".new-message").click(function () {
        $(this).removeClass('new-message border border-primary');
    });
}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/private-message", {}, JSON.stringify({'receiver': $("#receiver").val(), 'subject': $("#subject").val(), 'full_text': $("#message").val()}));
}