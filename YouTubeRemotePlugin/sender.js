let isReceiver = false;

function getStateMessage(stateType, content) {
    return JSON.stringify({messageType: stateType, 'content': content});

}

function emitState(state, content) {

    if (isReceiver && content !== null && socket.readyState === 1) {
        if (state === "QUEUE") {
            let songs = content.songs;
            const length = songs.length;
            if (songs.length > 25) {
                while (songs.length > 0) {
                    socket.send(getStateMessage(state, songs.splice(0, 25)));
                }
            } else {
                socket.send(getStateMessage(state, songs))
            }
            socket.send(getStateMessage("QUEUE_LENGTH", length))
        } else if (state === "HOME") {
            const copy = content.slice();
            if (copy.length > 2) {
                while (copy.length > 0) {
                    socket.send(getStateMessage(state, copy.splice(0, 2)));
                }
            } else {
                socket.send(getStateMessage(state, copy))
            }
        } else {
            socket.send(getStateMessage(state, content));
        }
    }

}

function emitAllStates() {
    emitState("CONTROLS_SONG", getSongState().songState);
    emitState("CONTROLS_TIME", getTimeState().time);
    emitState("CONTROLS_DETAILS", getDetailsState().detailsState);
    emitState("QUEUE", getWholeQueue());
    emitState("HOME", home);
}

function heartBeat() {
    console.log('Sending heart beat');
    emitState("HEART_BEAT", new Date());
}

