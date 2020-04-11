import IMessage from "../IMessage";
import { MessageType } from "../MessageType";


export default class SetReceiverMessage implements IMessage {
    messageType: MessageType = MessageType.SET_RECEIVER;
    deviceName: string;

    constructor(deviceName: string) {
        this.deviceName = deviceName;
    }
}