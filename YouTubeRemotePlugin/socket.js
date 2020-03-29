let socket  = null;

function initializeWebSocket(address,deviceName)
{
    socket = new WebSocket(address, ['remote']);
    socket.addEventListener('open', (event) => {
        socket.send(`{"messageType": "START", "memberType" : "RECEIVER" , "deviceName" :  "${deviceName}"}`);
    });

    socket.addEventListener('message', function (event) {
        const message = JSON.parse(event.data);
        if (message.messageType === "MEDIA_CONTROL") {
            handleMediaControl(message);
        }
        if (message.messageType === "QUEUE_CONTROL") {
            handleQueueControl(message);
        }
        if (message.messageType === "CURRENT_RECEIVER") {
            handleSetReceiver(message);
        }
        console.log('Message from server ', event.data);
    });
}

