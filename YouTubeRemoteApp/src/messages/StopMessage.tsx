import IMessage from "./IMessage";
import { MessageType } from "./MessageType";


export default class StopMessage implements IMessage {
    messageType: MessageType = MessageType.STOP;
    deviceName: string;

    constructor(deviceName: string) {
        this.deviceName = deviceName;
    }
}