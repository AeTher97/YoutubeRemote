import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

export class SongTimeInfo {
    playing: boolean;
    time: number;
    maxTime: number;
}

export default class ControlsTimeMessage implements IMessage {
    messageType: MessageType = MessageType.CONTROLS_TIME;
    content: SongTimeInfo;
}