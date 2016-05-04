$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
    $("#btnpopover").click(function() {
        $('#changeLanguage').popover('show');
    });

    messageWindow = new MessageWindow();
    $('#changeLanguage').popover('show');

    var Messenger = {};

    Messenger.socket = null;

    Messenger.connect = (function (host) {
        if ('WebSocket' in window) {
            Messenger.socket = new WebSocket(host);
        } else if ('MozWebSocket' in window) {
            Messenger.socket = new MozWebSocket(host);
        } else {
            console.log('Error: WebSocket is not supported by this browser.');
            return;
        }

        Messenger.socket.onopen = function () {
            console.log('Info: WebSocket connection opened.');
        };

        Messenger.socket.onclose = function () {
            console.log('Info: WebSocket closed.');
        };

        Messenger.socket.onmessage = function (message) {
            console.log('Got message: ' + message.data);
            messageWindow.processMessage(JSON.parse(message.data));
        };
    });

    $("#messageClose").click(function () {
        messageWindow.close();
            console.log("sending message");
        var message = messageWindow.message;
        message.command = "CLOSE";
        Messenger.socket.send(JSON.stringify(message));
    });

    Messenger.initialize = function () {
        var wsProtocol = 'ws://';
        if (window.location.protocol == 'https:') {
            wsProtocol = 'wss://';
        }
        Messenger.connect(wsProtocol + window.location.host + baseUrl.trim() + 'websocket/messages/' + sessionID);
    };

    Messenger.initialize();

});

function MessageWindow(fOnClose){
    this.message = undefined;
    this.onClose = fOnClose;
    this.messagePool = [];

}

MessageWindow.prototype = {
    title : function title(data) {
        $('#messageTitle').text(data);
    },

    text: function text(data) {
        $('#messageText').text(data);
    },

    visible: function visible() {
        return $('#messageView').is(':visible');
    },

    show: function show(message) {
        this.message = message;
        this.title(jQuery.i18n.prop(message.title));
        this.text(jQuery.i18n.prop(message.text));
        $('#messageView').show();
    },

    close: function close() {
        $('#messageView').hide();
        //if (this.onClose) {
        //    this.onClose(this.message);
        //}
        var message = this.messagePool.pop();
        if (message != undefined) {
            this.show(message);
        }
    },


    //onClose: function (callback, message) {
    //    console.log('callback from prototype');
    //    callback(this, message)
    //},

    processMessage: function processMessage(message) {
        if (message.command === "SHOW") {
            if (this.visible()) {
                this.messagePool.push(message);
            } else {
                this.show(message);
            }
        } else if (message.command === "CLOSE") {
            if (this.message.id === message.id) {
                this.close();
            } else {
                var index = -1;
                for(var i = 0; i < this.messagePool.length; i++) {
                    if (message.id === this.messagePool[i].id) {
                        index = i;
                        break;
                    }
                }
                if (index > -1) {
                    this.messagePool.splice(index, 1);
                }
            }
        }

    }

};