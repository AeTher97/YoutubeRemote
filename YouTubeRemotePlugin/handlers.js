

function handleMediaControl(message) {
    executeControlCommand(message);
}

function handleQueueControl(message) {
    executeQueueControlCommand(message);
}

function handleSetReceiver(message) {
    isReceiver = message.deviceName === deviceName;
    emitAllStates();
}