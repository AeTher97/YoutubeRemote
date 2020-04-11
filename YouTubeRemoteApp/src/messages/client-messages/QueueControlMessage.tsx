import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

enum QueueControlAction {
    MOVE = 'MOVE',
    REMOVE = 'REMOVE',
}

abstract class QueueControlMessage implements IMessage {
    messageType: MessageType = MessageType.QUEUE_CONTROL;
    action: QueueControlAction;
    targetIndex: number;

    constructor(action: QueueControlAction, targetIndex: number) {
        this.action = action;
        this.targetIndex = targetIndex;
    }
}

export class MoveSongInQueueMessage extends QueueControlMessage {
    currentIndex: number;

    constructor(fromIndex: number, toIndex: number) {
        super(QueueControlAction.MOVE, toIndex);
        this.currentIndex = fromIndex;
    }
}

export class RemoveSongFromQueueMessage extends QueueControlMessage {
    constructor(songIndex: number) {
        super(QueueControlAction.REMOVE, songIndex);
    }
}