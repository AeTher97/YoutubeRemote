import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

export class Receiver {
    deviceName: string;
    uuid: string;
}

export default class ReceiversMessage implements IMessage {
    public messageType: MessageType = MessageType.RECEIVERS;
    public receivers: Receiver[];
}