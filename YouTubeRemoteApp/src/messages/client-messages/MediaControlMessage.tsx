import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

enum MediaControlAction {
    PLAY = 'PLAY',
    PAUSE = 'PAUSE',
    NEXT = 'NEXT',
    PREVIOUS = 'PREVIOUS',
    SET_TIME = 'SET_TIME',
    SHUFFLE = 'SHUFFLE',
    VOLUME = 'VOLUME',
    REPEAT = 'REPEAT'
}

abstract class MediaControlMessage implements IMessage {
    messageType: MessageType = MessageType.MEDIA_CONTROL;
    action: MediaControlAction;

    constructor(action: MediaControlAction) {
        this.action = action;
    }
}

export class PlaySongMessage extends MediaControlMessage {
    constructor() {
        super(MediaControlAction.PLAY);
    }
}

export class PauseSongMessage extends MediaControlMessage {
    constructor() {
        super(MediaControlAction.PAUSE);
    }
}

export class PreviousSongMessage extends MediaControlMessage {
    constructor() {
        super(MediaControlAction.PREVIOUS);
    }
}

export class NextSongMessage extends MediaControlMessage {
    constructor() {
        super(MediaControlAction.NEXT);
    }
}

export class SetTimeMessage extends MediaControlMessage {
    timeSet: number;

    constructor(timeSet: number) {
        super(MediaControlAction.SET_TIME);
        this.timeSet = timeSet;
    }
}

export class ShuffleQueueMessage extends MediaControlMessage {
    constructor() {
        super(MediaControlAction.SHUFFLE);
    }
}

export class SetVolumeMessage extends MediaControlMessage {
    volumeSet: number;

    constructor(volumeSet: number) {
        super(MediaControlAction.VOLUME);
        this.volumeSet = volumeSet;
    }
}

export enum RepeatMode {
    REPEAT_OFF = 'REPEAT_OFF',
    REPEAT_ONE = 'REPEAT_ONE',
    REPEAT_ALL = 'REPEAT_ALL'
} 

export class RepeatQueueMessage extends MediaControlMessage {
    repeatSet: RepeatMode;

    constructor(repeatMode: RepeatMode) {
        super(MediaControlAction.REPEAT);
        this.repeatSet = repeatMode;
    }
}
