let socket = null;
var connectionOpened = false;
var heartBeatVar;

const resetInterface = () => {
    const status = document.querySelector('#connection-status-indicator');
    const statusString = document.querySelector("#connection-status-string");
    const connectButton = document.querySelector("#connect-button");
    status.classList.add('ytr-inactive');
    status.classList.remove('ytr-active');
    statusString.innerText = 'Disconnected';
    connectButton.innerText = 'Connect';
}

const stopScripts = () => {
    clearInterval(heartBeatVar);
}

function initializeWebSocket(address, deviceName) {
    socket = new WebSocket(address, ['remote']);
    socket.addEventListener('open', (event) => {
        connectionOpened = true;
        socket.send(`{"messageType": "START", "memberType" : "RECEIVER" , "deviceName" :  "${deviceName}"}`);
        const status = document.querySelector('#connection-status-indicator');
        const statusString = document.querySelector("#connection-status-string");
        const connectButton = document.querySelector("#connect-button");
        status.classList.remove('ytr-inactive');
        status.classList.add('ytr-active');
        statusString.innerText = 'Connected';
        connectButton.innerText = 'Disconnect';
    });

    socket.addEventListener('message', function (event) {
        const message = JSON.parse(event.data);
        console.log('Message from server ', event.data);

        if (message.messageType === "MEDIA_CONTROL") {
            handleMediaControl(message);
        }
        if (message.messageType === "QUEUE_CONTROL") {
            handleQueueControl(message);
        }
        if (message.messageType === "CURRENT_RECEIVER") {
            handleSetReceiver(message);
        }
    });

    socket.addEventListener('close', () => {
        console.log("Closing socket");
        connectionOpened = false;
        resetInterface();
    })

    socket.addEventListener('error', () => {
        console.log("Socket error");
        connectionOpened = false;
        resetInterface();
    })
}

