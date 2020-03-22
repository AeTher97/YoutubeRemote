

function handleMediaControl(message) {
    executeControlCommand(message);
}

function handleSetReceiver(message) {
    if (message.deviceName === deviceName) {
        isReceiver = true;
    } else {
        isReceiver = false;
    }
    emitAllStates();
}