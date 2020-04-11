import IMessage from "../IMessage";
import { MessageType } from "../MessageType";
import { RepeatMode } from "../client-messages/MediaControlMessage";

export class ControlsDetails {
    volume: number;
    repeatType: RepeatMode;
    muted: boolean;
}

export default class ControlsDetailsMessage implements IMessage {
    messageType: MessageType = MessageType.CONTROLS_DETAILS;
    content: ControlsDetails;
}
