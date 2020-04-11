import IMessage from "../IMessage";
import { MessageType } from "../MessageType";


export default class HeartbeatMessage implements IMessage {
    messageType: MessageType = MessageType.HEART_BEAT;
}