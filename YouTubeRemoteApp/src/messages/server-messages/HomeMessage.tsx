import IMessage from "../IMessage";
import {MessageType} from "../MessageType";
import {RepeatMode} from "../client-messages/MediaControlMessage";

export class HomeInfo {
    index: number;
    header: RepeatMode;
    imgSrc: string;
    subHeader: string;
    content: [
        HomeItemInfo
    ]
}

export class HomeItemInfo {
    title: string;
    imgSrc: string;
    subText: string;
    type: ElementType;
}


export default class HomeMessage implements IMessage {
    messageType: MessageType = MessageType.HOME;
    content: HomeInfo[];
}

export enum ElementType {
    SQUARE = 'SQUARE',
    CIRCLE = 'CIRCLE',
}