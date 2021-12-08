let oldQueue = {songs: []};
let oldCurrentSong = {title: '', performer: '', imgSrc: '', imgSrcLarge: ''};
let oldCurrentTime = {
    playing: false,
    time: 0,
    maxTime: 0
};
let oldDetailsState = {
    volume: 0,
    repeatType: "REPEAT_OFF"
};

let home = [];


function getTimeState() {
    try {
        let time = {
            playing: false,
            time: 0,
            maxTime: 0
        };
        const playButtonAttribute = document.getElementsByClassName('play-pause-button').valueOf()[0].children[0].children[0].children[0].children[0].getAttribute('d');
        const playingAttribute = ('M6 19h4V5H6v14zm8-14v14h4V5h-4z');
        time.playing = playingAttribute === playButtonAttribute;


        const foundTime = document.querySelector("#left-controls > span");
        const currentTime = foundTime.innerText.split("/")[0].trim();
        const maxTime = foundTime.innerText.split("/")[1].trim();

        const a = currentTime.split(':'); // split it at the colons
        const b = maxTime.split(':'); // spl
        const currentSeconds = (+a[0]) * 60 + (+a[1]);
        const maxSeconds = (+b[0]) * 60 + (+b[1]);

        time.time = currentSeconds;
        time.maxTime = maxSeconds;

        const changed = oldCurrentTime !== time;
        oldCurrentTime = time;
        return {time: time, changed: changed};

    } catch (e) {
        console.log('No song chosen');
        return {time: null, changed: false};
    }
}

function getSongState() {
    try {
        const songState = {title: '', performer: '', imgSrc: '', imgSrcLarge: ''};
        console.log("Getting song state");
        let foundTitle = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > yt-formatted-string");
        let foundPerformer = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > span > span.subtitle.style-scope.ytmusic-player-bar > yt-formatted-string:nth-child(1)");


        foundPerformer = foundPerformer = foundPerformer.title.split("•")[0];
        songState.performer = foundPerformer;
        songState.title = foundTitle.title;

        songState.imgSrc = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div > img").getAttribute('src');
        songState.imgSrcLarge = document.querySelector("#player > div > yt-img-shadow >img").getAttribute('src');


        const changed = oldCurrentSong !== songState;
        oldCurrentSong = songState;

        return {songState: songState, changed: changed};
    } catch (e) {
        return {songState: null, changed: false};
    }


}

function getQueueState() {
    try {
        const queueState = getWholeQueue();

        return {queueState: queueState, changed: true};
    } catch (e) {
        console.log("Cannot find queue");
        return {queueState: null, changed: false};
    }
}

function getDetailsState() {
    try {
        const detailsState = {volume: 0, repeatType: "REPEAT_OFF", muted: false};
        const repeatOneString = "M4.393 5.536C3.643 5.536 3 6.179 3 6.929V11h1.714V7.25H18.68L16.32 9.607l1.179 1.179 4.393-4.393L17.5 2l-1.179 1.179 2.358 2.357H4.393zM6.5 13.214l-4.393 4.393L6.5 22l1.179-1.179-2.358-2.357 14.286-.071c.75 0 1.393-.643 1.393-1.393v-4.286h-1.714v3.965L5.32 16.75l2.358-2.357L6.5 13.214z";
        const repeatOffString = "M3 6.929c0-.75.643-1.393 1.393-1.393h14.286L16.32 3.179 17.5 2l4.393 4.393-4.393 4.393-1.179-1.179L18.68 7.25H4.714V11H3V6.929zM2.107 17.607L6.5 13.214l1.179 1.179L5.32 16.75l13.965-.071v-3.965H21V17c0 .75-.643 1.393-1.393 1.393l-14.286.071 2.358 2.357L6.5 22l-4.393-4.393z";
        const repeatAllString = "M3 6.929c0-.75.643-1.393 1.393-1.393h14.286L16.32 3.179 17.5 2l4.393 4.393-4.393 4.393-1.179-1.179L18.68 7.25H4.714V11H3V6.929zM2.107 17.607L6.5 13.214l1.179 1.179L5.32 16.75l13.965-.071v-3.965H21V17c0 .75-.643 1.393-1.393 1.393l-14.286.071 2.358 2.357L6.5 22l-4.393-4.393z";
        const mutedString = "M16.5 12c0-1.77-1.02-3.29-2.5-4.03v2.21l2.45 2.45c.03-.2.05-.41.05-.63zm2.5 0c0 .94-.2 1.82-.54 2.64l1.51 1.51C20.63 14.91 21 13.5 21 12c0-4.28-2.99-7.86-7-8.77v2.06c2.89.86 5 3.54 5 6.71zM4.27 3L3 4.27 7.73 9H3v6h4l5 5v-6.73l4.25 4.25c-.67.52-1.42.93-2.25 1.18v2.06c1.38-.31 2.63-.95 3.69-1.81L19.73 21 21 19.73l-9-9L4.27 3zM12 4L9.91 6.09 12 8.18V4z";
        const notMutedString = "M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.05c1.48-.73 2.5-2.25 2.5-4.02zM14 3.23v2.06c2.89.86 5 3.54 5 6.71s-2.11 5.85-5 6.71v2.06c4.01-.91 7-4.49 7-8.77s-2.99-7.86-7-8.77z";

        const repeatAttribute = document.querySelector("#right-controls > div > tp-yt-paper-icon-button.repeat.style-scope.ytmusic-player-bar").children[0].children[0].children[0].children[0].getAttribute('d');

        if (repeatAttribute === repeatOffString && document.querySelector("#right-controls > div > tp-yt-paper-icon-button.repeat.style-scope.ytmusic-player-bar").getAttribute('aria-label') !== "Repeat all") {
            detailsState.repeatType = "REPEAT_OFF"
        } else if (repeatAttribute === repeatAllString) {
            detailsState.repeatType = "REPEAT_ALL"
        } else if (repeatAttribute === repeatOneString) {
            detailsState.repeatType = "REPEAT_ONE"
        } else {
            console.log("Cannot get repeat state");
        }

        detailsState.volume = parseInt(document.querySelector("#volume-slider").getAttribute('aria-valuenow'));

        const muteAttribute = document.querySelector("#right-controls > div > tp-yt-paper-icon-button.repeat.style-scope.ytmusic-player-bar").children[0].children[0].children[0].children[0].getAttribute('d');

        if (muteAttribute === mutedString) {
            detailsState.muted = true;
        } else if (muteAttribute === notMutedString) {
            detailsState.muted = false;
        }

        let changed = false;

        if (oldDetailsState !== detailsState) {
            changed = true;
            oldDetailsState = detailsState;
        }

        return {changed: changed, detailsState: detailsState}
    } catch (e) {
        console.error("Cannot find details " + e.message);
        return null;
    }

}

function getHomeState() {
    const allBars = document.querySelector("#contents").children;


    let content = [];

    for (let i = 0; i < allBars.length; i++) {
        let barObject = {
            index: i,
            header: '',
            content: []
        };
        if (allBars[i].children[0].children[1].children[1] !== undefined) {

            barObject.header = allBars[i].children[0].children[1].children[1].children[0].innerHTML;

            let elements = allBars[i].children[1].children[1].children[0].children;


            for (let j = 0; j < elements.length; j++) {

                let elementObject = {
                    title: '',
                    subText: '',
                    imgSrc: '',
                    type: ''
                };
                if (elements[j] !== undefined) {


                    if (elements[j].children[0].children[0].getAttribute('thumbnail-crop_') === "MUSIC_THUMBNAIL_CROP_UNSPECIFIED") {
                        elementObject.type = "SQUARE"
                    } else {
                        elementObject.type = "CIRCLE";
                    }
                    try {
                        elementObject.imgSrc = elements[j].children[0].children[0].children[0].children[0].getAttribute('src');

                        elementObject.title = elements[j].children[1].children[0].innerText;
                        elementObject.subText = elements[j].children[1].children[1].children[1].innerText;
                    } catch (e) {
                        console.log("xd")
                    }
                    barObject.content.push(elementObject);
                }
            }
            content.push(barObject);
        }

    }

    home = content;
    console.log(content)
    return content;
}

function getWholeQueue() {
    const queueState = {songs: []};
    const queue = document.querySelector("#player-page > div > div.side-panel.modular.style-scope.ytmusic-player-page > ytmusic-tab-renderer>div>ytmusic-player-queue > div").children;

    const strippedQueue = [];
    for (let i = 0; i < queue.length; i++) {
        const attribute = queue[i].getAttribute('play-button-state');
        let imgSrc = queue[i].children[1].children[0].children[0].getAttribute('src').startsWith('data') ? null : queue[i].children[1].children[0].children[0].getAttribute('src');
        const performer = queue[i].children[2].children[1].children[1].innerText;
        if (attribute !== 'playing'
            && attribute !== 'loading'
            && attribute !== 'paused') {
            strippedQueue.push({
                index: i,
                title: queue[i].children[2].children[0].innerHTML,
                performer: performer,
                imgSrc: imgSrc,
                time: queue[i].children[5].innerText
            })
        } else {
            strippedQueue.push({
                index: i,
                title: queue[i].children[2].children[0].innerHTML,
                performer: performer,
                imgSrc: imgSrc,
                selected: true,
                time: queue[i].children[5].innerText
            })
        }
    }
    queueState.songs = strippedQueue;
    return queueState;
}

function compareElements(oldElement, element) {

    let returned = false;
    if (oldElement.title !== element.title
        || oldElement.performer !== element.performer
        || oldElement.selected !== element.selected) {
        returned = true
    }
    if (oldElement.imgSrc === null && element.imgSrc !== null) {
        returned = true;
    }
    return returned;
}

