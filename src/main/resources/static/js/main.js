'use strict';

var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');

var stompClient = null;
var username = null;

function connect() {
    username = document.querySelector('#username').innerText.trim();
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/publicChatRoom', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
}

function onError(error) {
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    // event.preventDefault();
}


function onMessageReceived(payload) {
    var today = new Date(Date.now());
    var date = today.toLocaleDateString("en-US");
    var message = JSON.parse(payload.body);
    console.log(message);
    if (message.type != 'JOIN' && message.type != 'LEAVE' && message.content != null && message.sender != null) {
        var _html = "<div class='comment'><div class='img-comment w3-left'><img src='https://www.yamcode.com/img/default-avatar.png' />";
        _html += "</div><div class='comment-content w3-right'><h4><b>" + message.sender + "</b></h4>";
        _html += "<i class='w3-right'>" + date + "</i><p>" + message.content + "</p>";
        _html += "</div></div>";
        $('.list-comment').append(_html);
        messageArea.scrollTop = messageArea.scrollHeight;
    }

}

$('#submit_comment').click(function (ev) {
    ev.preventDefault();
    $("#messageForm").ajaxSubmit({url: 'http://localhost:8080/post/comment/add', type: 'POST'});
    sendMessage();
})