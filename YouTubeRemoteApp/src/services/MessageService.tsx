import IMessage from "../messages/IMessage";
import { MessageType } from "../messages/MessageType";
import HeartbeatMessage from "../messages/client-messages/HeartbeatMessage";
import Subscription from "../utils/Subscription";
import StartMessage from "../messages/client-messages/StartMessage";

class SubscriptionCallback {
    public messageType: MessageType;
    public callback: (msg: IMessage) => void;

    public constructor(messageType: MessageType, callback: (msg: IMessage) => void) {
        this.messageType = messageType;
        this.callback = callback;
    }
}

export class MessageService {
    private webSocket: WebSocket;

    private subscriptionCallbacks: SubscriptionCallback[] = [];
    private messagesQueued: IMessage[] = [];

    private handleMessage(ev: MessageEvent): void {
        const message: IMessage = JSON.parse(ev.data);
        this.subscriptionCallbacks.forEach(c => {
            if(c.messageType === message.messageType)
                c.callback(message);
        });
    }

    private sendQueuedMessages(): void {
        const messagesToSend = this.messagesQueued;
        this.messagesQueued = [];
        messagesToSend.forEach(msg => this.sendMessage(msg));
    }

    public sendMessage(message: IMessage): void {
        if(this.webSocket && this.webSocket.readyState === 1) {
            this.webSocket.send(JSON.stringify(message));
        } else {
            this.messagesQueued.push(message);
        }
    }

    private sendHeartbeat(): any {
        this.sendMessage(new HeartbeatMessage());
        setTimeout(() => this.sendHeartbeat(), 30000);
    }

    private getHexId(): string {
        return Number(Math.floor(Math.random() * 15000000 + 0x111111)).toString(16);
    }

    public subscribe<T extends IMessage>(messageType: MessageType, callback: (msg: T) => void): Subscription {
        const subscriptionCallback = new SubscriptionCallback(messageType, callback);
        this.subscriptionCallbacks.push(subscriptionCallback);
        return new Subscription(() => {
            this.subscriptionCallbacks = this.subscriptionCallbacks.filter(s => s !== subscriptionCallback);
        });
    }

    public constructor(webSocketUrl: string, protocols?: string | string[]) {
        this.webSocket = new WebSocket(webSocketUrl, protocols);
        this.webSocket.onopen = () => this.sendQueuedMessages();
        this.webSocket.onclose = () => console.log('connection closed');
        this.webSocket.onmessage = ev => this.handleMessage(ev);

        this.sendMessage(new StartMessage('Phone:' + this.getHexId()));
        this.sendHeartbeat();
    }
}

const messageService = new MessageService('wss://ytremote.ngrok.io/remote', 'remote');

export default messageService;