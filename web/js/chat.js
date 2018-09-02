var chatConfig = {
    serverURI: "ws://localhost:8080/ChatTest_war_exploded/ws",
    clientRegistryMessage: JSON.stringify({
                "content": "clientRegistry",
                "from": "testUser",
                "type": "clientRegistry"
    }),
    css: {
        selectors: {
            inputId: "input",
            outputId: "log"
        }
    }

};


var dataConnector = (function getConnector () {

    var ws;

    function setupConnection(uri, onOpenCallback) {
        return new Promise( function (resolve) {
            ws = new WebSocket("ws://localhost:8080/ChatTest_war_exploded/ws");
            ws.addEventListener("open", function (event) {
                onOpenCallback.call(ws, event.data);
            });
            ws.addEventListener("message", function (event) {
                resolve(event.data)
            });
        })
    }

    return {
        setup: setupConnection,
        send: ws.send
    }

})();


var chatController  = (function getChatController (config, connector) {

    var log = document.querySelector("#" + config.css.selectors.outputId);
    var input = document.querySelector("#" + config.css.selectors.inputId);
    var connection;

    var services = {
        getTime: function timestamp() {
            var d = new Date(), minutes = d.getMinutes();
            if (minutes < 10) {
                minutes = '0' + minutes;
            }
            return d.getHours() + ':' + minutes;
        }

    };

    function setupListeners() {
        input.addEventListener("keyup", function (event) {
            if (event.keyCode === 13) {
                var json = JSON.stringify({
                    "content": event.target.value,
                    "from": "webClient",
                    "type": "message"
                });
                displayMessage(event.target.value);
                connector.send(json);
                event.target.value = "";
            }
        });
    }

    function onOpenWsCallback () {
        this.send(config.clientRegistryMessage);
    }

    function displayMessage (message) {
        log.value += "[" + services.getTime() + "] " + message + "\n";
    }

    function start () {
        setupListeners();
        connection = connector.setup(config.serverURI, onOpenWsCallback);
        connection.then(function (data) {
            displayMessage(data.content);
        })
    }

    return {
        start: start
    }


})(chatConfig, dataConnector);

// chatController.start();