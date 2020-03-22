let oldQueue = {songs: []};
let oldCurrentSong = {title: '', performer: '', imgSrc: ''};
let oldCurrentTime = {
    playing: false,
    time: 0,
    maxTime: 0
};


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
        console.log('no song chosen');
        return {time: null, changed: false};
    }
}

function getSongState() {
    try {
        const songState = {title: '', performer: '', imgSrc: ''};
        let foundTitle = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > yt-formatted-string");
        let foundPerformer = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > div.content-info-wrapper.style-scope.ytmusic-player-bar > span > span.subtitle.style-scope.ytmusic-player-bar > yt-formatted-string:nth-child(1)");


        foundPerformer = foundPerformer = foundPerformer.title.split("•")[0];
        songState.performer = foundPerformer;
        songState.title = foundTitle.title;

        songState.imgSrc = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar > img").getAttribute('src');

        const changed = oldCurrentSong !== songState;
        oldCurrentSong = songState;

        return {songState: songState, changed: changed};
    } catch (e) {
        console.log('no song chosen');
        return {songState: null, changed: false};
    }


}

function getQueueState() {
    try {
       const queueState = getWholeQueue();
        let returnedState = {songs: []};

        let changed = false;

        for (let i = 0; i < queueState.songs.length; i++) {
            if (oldQueue.songs[i] !== undefined) {
                if (compareElements(oldQueue.songs[i], queueState.songs[i])) {
                    returnedState.songs.push(queueState.songs[i]);
                    changed = true;
                }
            }
        }

        if (oldQueue.songs.length === 0) {
            returnedState = queueState;
            changed = true;
        }
        oldQueue = queueState;

        return {queueState: returnedState, changed: changed};
    } catch (e) {
        console.log("Cannot find queue");
        return {queueState: null, changed: false};
    }
}

function getWholeQueue() {
    const queueState = {songs: []};
    const queue = document.querySelector("#queue").children[0].children;


    const strippedQueue = [];
    for (let i = 0; i < queue.length; i++) {
        const attribute = queue[i].getAttribute('play-button-state');
        let imgSrc = queue[i].children[1].children[0].children[0].getAttribute('src').startsWith('data') ? null : queue[i].children[1].children[0].children[0].getAttribute('src');
        if (attribute !== 'playing'
            && attribute !== 'loading'
            && attribute !== 'paused') {
            strippedQueue.push({
                index: i,
                title: queue[i].children[2].children[0].innerText,
                performer: queue[i].children[2].children[1].innerText,
                imgSrc: imgSrc
            })
        } else {
            strippedQueue.push({
                index: i,
                title: queue[i].children[2].children[0].innerText,
                performer: queue[i].children[2].children[1].innerText,
                imgSrc: imgSrc,
                selected: true
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

