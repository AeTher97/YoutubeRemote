import IMessage from "./IMessage";
import { MessageType } from "./MessageType";

export enum MediaControlAction {
    PLAY = 'PLAY',
    PAUSE = 'PAUSE',
    NEXT = 'NEXT',
    PREVIOUS = 'PREVIOUS'
}

export default class MediaControlMessage implements IMessage {
    messageType: MessageType = MessageType.MEDIA_CONTROL;
    action: MediaControlAction;

    constructor(action: MediaControlAction) {
        this.action = action;
    }
}