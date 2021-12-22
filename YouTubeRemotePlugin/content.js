// const address = 'ws://localhost:8079/remote';
const address = 'wss://globalcapsleague.com/remote';
const deviceName =`${navigator.platform}` + ID()  ;
const initializeScripts = () => {

    initializeWebSocket(address, deviceName);
    initializeObservers();

    heartBeatVar =  setInterval(heartBeat, 30000);

// setTimeout(scrollToBottom, 2000);
}