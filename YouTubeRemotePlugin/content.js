const address = 'wss://a9e9-195-234-9-233.ngrok.io/remote';
const deviceName = 'my-browser';

initializeWebSocket(address, deviceName);
initializeObservers();

setInterval(heartBeat, 45000);

// setTimeout(scrollToBottom, 2000);