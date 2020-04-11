import IMessage from "../IMessage";
import { MessageType } from "../MessageType";

export interface SongInQueue {
    index: number;
    title: string;
    performer: string;
    imgSrc: string;
    selected?: boolean;
}

export class QueueMessage implements IMessage {
    messageType: MessageType = MessageType.QUEUE;
    content: SongInQueue[];
}
