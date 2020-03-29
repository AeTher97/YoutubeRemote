function executeQueueControlCommand(message) {
    switch (message.action) {
        case 'MOVE':
            move();
            break;
        case 'REMOVE' :
            remove();
            break;

    }
}

function move() {
    // #TODO implement move command
}

function remove() {

}