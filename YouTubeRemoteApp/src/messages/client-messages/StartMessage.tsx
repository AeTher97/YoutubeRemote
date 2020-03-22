import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

enum MemberType {
    CONTROLLER = 'CONTROLLER',
    RECEIVER = 'RECEIVER'
}

export default class StartMessage implements IMessage {
    messageType: MessageType = MessageType.START;
    memberType: MemberType = MemberType.CONTROLLER;
    deviceName: string;

    constructor(deviceName: string) {
        this.deviceName = deviceName;
    }
}