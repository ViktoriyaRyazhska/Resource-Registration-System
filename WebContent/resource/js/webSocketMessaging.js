$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
    $("#btnpopover").click(function() {
        $('#changeLanguage').popover('show');
    });

    messageWindow = new MessageWindow();
    messageWindow.header("TEST");
    $('#changeLanguage').popover('show');
    messageWindow.hide();

    var Chat = {};

    Chat.socket = null;

    Chat.connect = (function (host) {
        if ('WebSocket' in window) {
            Chat.socket = new WebSocket(host);
        } else if ('MozWebSocket' in window) {
            Chat.socket = new MozWebSocket(host);
        } else {
            console.log('Error: WebSocket is not supported by this browser.');
            return;
        }

        Chat.socket.onopen = function () {
            console.log('Info: WebSocket connection opened.');
        };

        Chat.socket.onclose = function () {
            console.log('Info: WebSocket closed.');
        };

        Chat.socket.onmessage = function (message) {
            console.log('WebSocket message: ' + message.data);
            messageWindow.text(message.data);
            messageWindow.show();
        };
    });

    Chat.initialize = function () {
        var wsProtocol = 'ws://';
        if (window.location.protocol == 'https:') {
            wsProtocol = 'wss://';
        }
        Chat.connect(wsProtocol + window.location.host + '/registrator/websocket/messages/' + sessionID);
    };

    Chat.sendMessage = (function () {
        var message = document.getElementById('chat').value;
        if (message != '') {
            Chat.socket.send(message);
            document.getElementById('chat').value = '';
        }
    });

    Chat.initialize();

});

function MessageWindow(fOnClose){
    MessageWindow.prototype.messageId = -1;

    $("#messageClose").click(function () {
        $('#messageView').hide();
        if (fOnClose) {

        }
    })
}

MessageWindow.prototype = {
    header : function header(data) {
        $('#messageTitle').text(data);
    }
};

MessageWindow.prototype.text = function (data) {
    $('#messageText').text(data);
};

MessageWindow.prototype.hide = function () {
    $('#messageView').hide();
};

MessageWindow.prototype.isHidden = function () {
    return !$('#messageView').is(':visible');
};

MessageWindow.prototype.show = function () {
    $('#messageView').show();
};