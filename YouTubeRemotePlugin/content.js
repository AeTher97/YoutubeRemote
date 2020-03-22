const address = 'wss://ytremote.ngrok.io/remote';

const controlsSongState = {title: '', performer: '', imgSrc: ''};
const controlsTimeState = {playing: false, time: 0, maxTime: 0};
const socket = new WebSocket(address, ['remote']);
let isReceiver = false;
const deviceName = 'my-browser2';
let currentCheck = 0;

socket.addEventListener('open', (event) => {
    socket.send(`{"messageType": "START", "memberType" : "RECEIVER" , "deviceName" :  "${deviceName}"}`);
});

socket.addEventListener('message', function (event) {
    const message = JSON.parse(event.data);
    if (message.messageType === "MEDIA_CONTROL") {
        handleMediaControl(message);
    }
    if (message.messageType === "SET_RECEIVER") {
        handleSetReceiver(message);
    }
    console.log('Message from server ', event.data);
});

function handleMediaControl(message) {
    switch (message.action) {
        case 'PLAY':
            document.querySelector("#play-pause-button").click();
            break;
    }
}

function handleSetReceiver(message) {
    if (message.deviceName === deviceName) {
        isReceiver = true;
    } else {
        isReceiver = false;
    }
    emitState("CONTROLS_SONG");
    emitState("CONTROLS_TIME");
}


function getStateMessage(stateType, content) {
    return JSON.stringify({messageType: stateType, 'content': content});

}

function emitState(state) {
    if (isReceiver) {
        switch (state) {
            case "CONTROLS_SONG":
                socket.send(getStateMessage(state, controlsSongState));
                break;
            case "CONTROLS_TIME":
                socket.send(getStateMessage(state, controlsTimeState));
                break;

        }
    }

}


function checkControlsTimeState() {
    let changed = false;
    currentCheck = currentCheck + 1;
    try {
        const playButtonAttribute = document.getElementsByClassName('play-pause-button').valueOf()[0].children[0].children[0].children[0].children[0].getAttribute('d');
        const playingAttribute = ('M6 19h4V5H6v14zm8-14v14h4V5h-4z');
        const isPlaying = playingAttribute === playButtonAttribute;

        if (controlsTimeState.playing !== isPlaying) {
            controlsTimeState.playing = playingAttribute === playButtonAttribute;
            changed = true;
        }


        const foundTime = document.querySelector("#left-controls > span");
        const currentTime = foundTime.innerText.split("/")[0].trim();
        const maxTime = foundTime.innerText.split("/")[1].trim();

        const a = currentTime.split(':'); // split it at the colons
        const b = maxTime.split(':'); // spl
        const currentSeconds = (+a[0]) * 60 + (+a[1]);
        const maxSeconds = (+b[0]) * 60 + (+b[1]);

        if (maxSeconds !== controlsTimeState.maxTime || currentSeconds !== controlsTimeState.time) {
            controlsTimeState.time = currentSeconds;
            controlsTimeState.maxTime = maxSeconds;
            changed = true;
        }

    } catch (e) {
        console.log('no song chosen');
    }

    if (changed || currentCheck === 5) {
        emitState("CONTROLS_TIME");
        currentCheck = 0;
    }

    console.log(currentCheck);
}

function checkSongState() {
    let changed = false;
    try {
        let foundTitle = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > yt-formatted-string");
        let foundPerformer = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > span > span.subtitle.style-scope.ytmusic-player-bar > yt-formatted-string:nth-child(1)");


        foundPerformer = foundPerformer = foundPerformer.title.split("•")[0];

        if (foundPerformer !== controlsSongState.performer || foundTitle.title !== controlsSongState.title) {
            controlsSongState.performer = foundPerformer;
            controlsSongState.title = foundTitle.title;
            changed = true;
        }

        let imageSource = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > img").getAttribute('src');
        if (imageSource !== controlsSongState.imgSrc) {
            controlsSongState.imgSrc = imageSource;
            changed = true;
        }

    } catch (e) {
        console.log('no song chosen');
    }

    if (changed || currentCheck === 5) {
        emitState("CONTROLS_SONG");
        currentCheck = 0;
    }

    console.log(currentCheck);
}

setInterval(checkControlsTimeState, 1000);
setInterval(checkSongState, 1000);

