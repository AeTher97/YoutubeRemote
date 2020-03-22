let queueLastFired = new Date();
let titleLastFired = new Date();

let queueFired = false;
let titleFired = false;

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
    const queue = document.querySelector("#queue").children[0];
    createObserver(queue, config, queueLastFired, queueFired, threshold, callback);

}

function addSongObserver(callback, threshold) {
    const title = document.querySelector("#layout > ytmusic-player-bar > div.middle-controls.style-scope.ytmusic-player-bar");
    createObserver(title, config, titleLastFired, titleFired, threshold, callback);
}

function addTimeObserver(callback, threshold) {
    const time = document.querySelector("#left-controls");
    createObserver(time, config, titleLastFired, titleFired, threshold, callback);
}

function initializeObservers(threshold = 750) {
    addQueueObserver(() => {
        setTimeout(()=> {
            const queueStateWrapper = getQueueState();
            if(queueStateWrapper.changed) {
                console.log(queueStateWrapper.queueState.songs);
                emitState("QUEUE", queueStateWrapper.queueState);
            }
        } ,1000)
    }, threshold);


    addSongObserver(() => {
        console.log('title event');
        const songStateWrapper = getSongState();
        if(songStateWrapper === null){
            return;
        }
        if(songStateWrapper.changed) {
            emitState("CONTROLS_SONG", songStateWrapper.songState)
        }
    }, threshold);

    addTimeObserver(() => {
        console.log('time event');
        const timeStateWrapper = getTimeState();
        if(timeStateWrapper === null){
            return;
        }
        if(timeStateWrapper.changed) {
            emitState("CONTROLS_TIME", timeStateWrapper.time)
        }
    }, 50);
}

