import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

export class SongInfo {
    title: string;
    performer: string;
    imgSrc: string;
    imgSrcLarge: string;
}

export default class ControlsSongMessage implements IMessage {
    messageType: MessageType = MessageType.CONTROLS_SONG;
    content: SongInfo;
}