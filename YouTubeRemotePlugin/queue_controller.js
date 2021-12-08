function executeQueueControlCommand(message) {
    switch (message.action) {
        case 'MOVE':
            move(message.currentIndex, message.targetIndex);
            break;
        case 'REMOVE' :
            remove(message.targetIndex);
            break;
        case 'PLAY_NEXT' :
            playNext(message.targetIndex);
            break;
        case 'PLAY':
            playFromQueue(message.targetIndex);
            break;

    }
}

function move(currentIndex, targetIndex) {
    console.log(currentIndex,targetIndex)
    const elementDrag = getQueue()[currentIndex];
    const elementDrop = document.querySelector("#player-page > div > div.side-panel.modular.style-scope.ytmusic-player-page > ytmusic-tab-renderer>div>ytmusic-player-queue ");
    const elementDropCoords = getQueue()[targetIndex];
    if (!elementDrag || !elementDrop) return false;

    // calculate positions
    let pos = elementDrag.getBoundingClientRect();
    const center1X = Math.floor((pos.left + pos.right) / 2);
    const center1Y = Math.floor((pos.top + pos.bottom) / 2);
    pos = elementDropCoords.getBoundingClientRect();
    const center2X = Math.floor((pos.left + pos.right) / 2);
    const center2Y = Math.floor((pos.top + pos.bottom) / 2);

    fireMouseEvent('mousemove', elementDrag, center1X, center1Y);
    fireMouseEvent('mouseenter', elementDrag, center1X, center1Y);
    fireMouseEvent('mouseover', elementDrag, center1X, center1Y);
    fireMouseEvent('mousedown', elementDrag, center1X, center1Y);

    fireMouseEvent('dragstart', elementDrag, center1X, center1Y);
    fireMouseEvent('drag', elementDrag, center1X, center1Y);
    fireMouseEvent('mousemove', elementDrag, center1X, center1Y);
    fireMouseEvent('drag', elementDrag, center2X, center2Y);
    fireMouseEvent('mousemove', elementDrop, center2X, center2Y);

    fireMouseEvent('mouseenter', elementDrop, center2X, center2Y);
    fireMouseEvent('dragenter', elementDrop, center2X, center2Y);
    fireMouseEvent('mouseover', elementDrop, center2X, center2Y);
    fireMouseEvent('dragover', elementDrop, center2X, center2Y);

    fireMouseEvent('drop', elementDrop, center2X, center2Y);
    fireMouseEvent('dragend', elementDrag, center2X, center2Y);
    fireMouseEvent('mouseup', elementDrag, center2X, center2Y);

}

function remove(targetIndex) {
    exposeElementMenu(targetIndex, clickRemoveOnPopup);
}


function playNext(targetIndex) {
    exposeElementMenu(targetIndex, clickPlayNextOnPopup);
}

const playFromQueue = (targetIndex) => {

    getQueue()[targetIndex].children[1].children[1].children[1].children[0].click();
}


function clickRemoveOnPopup() {
    for (let i = 0; i < getPopUp().length; i++) {
        try {
            if (getPopUp()[i].children[0].children[0].children[0].children[0].getAttribute("d") === "M7 11v2h10v-2H7zm5-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z") {
                console.log(i);
                getPopUp()[i].click();
            }
        } catch (e) {

        }
    }

}

function clickPlayNextOnPopup() {
    getPopUp()[3].click();
}

function exposeElementMenu(targetIndex, callback) {
    getQueue()[targetIndex].children[3].children[1].click()
    getQueue()[targetIndex].children[3].children[1].click()
    setTimeout(() => {
        callback()
    }, 300);
}

function getQueue() {
    return document.querySelector("#player-page > div > div.side-panel.modular.style-scope.ytmusic-player-page > ytmusic-tab-renderer>div>ytmusic-player-queue > div").children;
}

function getPopUp() {
    return document.querySelector("#contentWrapper > ytmusic-menu-popup-renderer").children[0].children;
}

function fireMouseEvent(type, elem, centerX, centerY) {
    const evt = document.createEvent('MouseEvents');
    evt.initMouseEvent(type, true, true, window, 1, 1, 1, centerX, centerY, false, false, false, false, 0, elem);
    elem.dispatchEvent(evt);
}