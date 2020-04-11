const address = 'wss://ytremote.ngrok.io/remote';
const deviceName = 'my-browser';

initializeWebSocket(address, deviceName);
initializeObservers();
setInterval(heartBeat, 45000);

setTimeout(scrollToBottom, 2000);