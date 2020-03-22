function executeControlCommand(message) {
    switch (message.action) {
        case 'PLAY':
            play();
            break;
        case  'PAUSE':
            pause();
            break;
    }
}

function play() {
    if (oldCurrentTime.playing === false) {
        document.querySelector("#play-pause-button").click();
    }
}

function pause() {
    if (oldCurrentTime.playing === true) {
        document.querySelector("#play-pause-button").click();
    }
}