let isReceiver = false;

function getStateMessage(stateType, content) {
    return JSON.stringify({messageType: stateType, 'content': content});

}

function emitState(state, content) {

    if (isReceiver && content !== null) {
        if (state === "QUEUE") {
            let songs = content.songs;
            if (songs.length > 25) {
                while (songs.length > 0) {
                    socket.send(getStateMessage(state, songs.splice(0, 25)));
                }
            } else {
                socket.send(getStateMessage(state, songs))
            }
        } else if (state === "HOME") {
            if (content.length > 2) {
                while (content.length > 0) {
                    socket.send(getStateMessage(state, content.splice(0, 2)));
                }
            } else {
                socket.send(getStateMessage(state, content))
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

}

function heartBeat() {
    console.log('sending heart beat');
    emitState("HEART_BEAT", new Date());
}

