let queueLastFired = new Date();
let titleLastFired = new Date();
let timeLastFired = new Date();
let detailsLastFired = new Date();
let homeLastFired = new Date();

let queueFired = false;
let titleFired = false;
let timeFired = false;
let detailsFired = false;
let homeFired = false;

const config = {attributes: true, characterData: true, subtree: true, childList: true};

function createObserver(object, config, lastFired, hasFired, threshold, callback) {
    const observer = new MutationObserver(function (mutations) {
        const difference = new Date() - lastFired;
        lastFired = new Date();
        if (difference > threshold || hasFired === false) {
            callback();
            if (hasFired === false) {
                hasFired = true;
            }
        }
    });
    observer.observe(object, config);

}

function addQueueObserver(callback, threshold) {
    const queue = document.querySelector("#player-page > div > div.side-panel.modular.style-scope.ytmusic-player-page > ytmusic-tab-renderer>div>ytmusic-player-queue > div");
    createObserver(queue, config, queueLastFired, queueFired, threshold, callback);

}

function addSongObserver(callback, threshold) {
    const title = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar");
    createObserver(title, config, titleLastFired, titleFired, threshold, callback);
}

function addTimeObserver(callback, threshold) {
    const time = document.querySelector("#left-controls");
    createObserver(time, config, timeLastFired, timeFired, threshold, callback);
}

function addDetailsObserver(callback, threshold) {
    const details = document.querySelector("#right-controls");
    createObserver(details, config, detailsLastFired, detailsFired, threshold, callback);
}

function addHomeObserver(callback, threshold) {
    const homeObject = document.querySelector("#browse-page > ytmusic-section-list-renderer").children[1];
    createObserver(homeObject, config, homeLastFired, homeFired, threshold, callback);
}

function initializeObservers(threshold = 750) {
    addQueueObserver(() => {
        setTimeout(() => {
            const queueStateWrapper = getQueueState();
            if (queueStateWrapper.changed) {
                emitState("QUEUE", queueStateWrapper.queueState);
            }
        }, 1000)
    }, threshold);

    addSongObserver(() => {
        console.log('title event');
        const songStateWrapper = getSongState();
        if (songStateWrapper.changed) {
            emitState("CONTROLS_SONG", songStateWrapper.songState)
        }
    }, threshold);

    addTimeObserver(() => {
        setTimeout(() => {
            const timeStateWrapper = getTimeState();
            if (timeStateWrapper === null) {
                return;
            }
            if (timeStateWrapper.changed) {
                emitState("CONTROLS_TIME", timeStateWrapper.time)
            }
        }, 300)
    }, 200);

    addDetailsObserver(() => {
        console.log('Details event');
        setTimeout(() => {
            const detailsStateWrapper = getDetailsState();
            if (detailsStateWrapper === null) {
                return;
            }
            if (detailsStateWrapper.changed) {
                emitState("CONTROLS_DETAILS", detailsStateWrapper.detailsState)
            }
        }, 1000);
    }, threshold);

    // const interval = setInterval(() => {
    //     if(document.querySelector("#browse-page > ytmusic-section-list-renderer") !== null){
    //         clearInterval(interval);
    //         console.log("intrea");
    //         emitState("HOME", getHomeState());
    //         addHomeObserver(() => {
    //             console.log('Home event');
    //             setTimeout(() => {
    //                 emitState('HOME',getHomeState())
    //             },500)
    //         },threshold)
    //     }
    // },100)

}

