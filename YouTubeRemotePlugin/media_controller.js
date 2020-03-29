function executeControlCommand(message) {
    switch (message.action) {
        case 'PLAY':
            play();
            break;
        case  'PAUSE':
            pause();
            break;
        case 'NEXT':
            next();
            break;
        case 'PREVIOUS' :
            previous();
            break;
        case 'SET_TIME' :
            setTime(message.timeSet);
            break;
        case 'VOLUME' :
            setVolume(message.volumeSet);
            break;
        case 'SHUFFLE' :
            shuffle();
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

function next() {
    document.querySelector("#left-controls > div > paper-icon-button.next-button.style-scope.ytmusic-player-bar").click();
}

function previous() {
    document.querySelector("#left-controls > div > paper-icon-button.previous-button.style-scope.ytmusic-player-bar").click();
}

function setTime(timeSet) {
    clickOnProgressBarAtTime(timeSet);
}

function setVolume(volumeSet) {
    exposeDetailControls();
    clickOnVolumeBarAtVolume(volumeSet);
}

function shuffle() {
    exposeDetailControls();
    document.querySelector("#right-controls > div > paper-icon-button.shuffle.style-scope.ytmusic-player-bar").click();
}

function clickOnProgressBarAtTime(time) {
    [x, y] = calculateHorizontalProgressClickPosition(time);
    click(x, y);
}

function clickOnVolumeBarAtVolume(volume) {
    [x, y] = calculateHorizontalVolumeClickPosition(volume);
    click(x, y);
}

function calculateHorizontalProgressClickPosition(setTime) {
    let proportion = setTime / oldCurrentTime.maxTime;
    if (proportion < 0) {
        proportion = 0.01;
    } else if (proportion > 1) {
        proportion = 0.99;
    }

    let bodyRect = document.body.getBoundingClientRect();
    let elementRect = document.querySelector("#progress-bar").getBoundingClientRect();
    let positionY = elementRect.top - bodyRect.top;
    let width = document.querySelector("#progress-bar").getBoundingClientRect().width;
    let positionX = Math.floor(width * proportion);
    return [positionX, positionY];

}

function calculateHorizontalVolumeClickPosition(volume) {
    let proportion = volume / oldCurrentTime.maxTime;
    if (proportion < 0) {
        proportion = 0;
    } else if (proportion > 1) {
        proportion = 1;
    }

    let bodyRect = document.body.getBoundingClientRect();
    let elementRect = document.querySelector("#volume-slider").getBoundingClientRect();
    let positionY = elementRect.top - bodyRect.top;
    let width = document.querySelector("#volume-slider").getBoundingClientRect().width;
    let positionX = Math.floor(width * proportion) + document.querySelector("#volume-slider").getBoundingClientRect().x;
    return [positionX, positionY];

}

function click(x, y) {
    let ev = document.createEvent("MouseEvent");
    let el = document.elementFromPoint(x, y);
    ev.initMouseEvent(
        "click",
        true /* bubble */, true /* cancelable */,
        window, null,
        x, y, x, y, /* coordinates */
        false, false, false, false, /* modifier keys */
        0 /*left*/, null
    );
    el.dispatchEvent(ev);
}


function exposeDetailControls() {
    document.querySelector("#expanding-menu").setAttribute("aria-hidden", false);
    document.querySelector("#expanding-menu").setAttribute("style", "outline: none; opacity: 0; box-sizing: border-box; max-height: 32px; max-width: 300px; display: z-index: 103;");
    document.querySelector("#volume-slider").setAttribute("class", "volume-slider style-scope ytmusic-player-bar on-hover")
}

function fireQueueEvent() {

}