import IMessage from "../messages/IMessage";
import { MessageType } from "../messages/MessageType";

export class MessageSubscription {
    private onUnsubscribe: () => void;
    private subscribed: boolean = true;

    public unsubscribe(): void {
        if(this.subscribed) {
            this.onUnsubscribe();
            this.subscribed = false;
        }
    }

    public constructor(onUnsubscribe: () => void) {
        this.onUnsubscribe = onUnsubscribe;
    }
}

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
        console.log(ev.data);
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

    public subscribe<T extends IMessage>(messageType: MessageType, callback: (msg: T) => void): MessageSubscription {
        const subscriptionCallback = new SubscriptionCallback(messageType, callback);
        this.subscriptionCallbacks.push(subscriptionCallback);
        return new MessageSubscription(() => {
            this.subscriptionCallbacks = this.subscriptionCallbacks.filter(s => s !== subscriptionCallback);
        });
    }

    public constructor(webSocketUrl: string, protocols?: string | string[]) {
        this.webSocket = new WebSocket(webSocketUrl, protocols);
        this.webSocket.onopen = () => this.sendQueuedMessages();
        this.webSocket.onclose = () => console.log('connection closed');
        this.webSocket.onmessage = ev => this.handleMessage(ev);
    }
}

const messageService = new MessageService('wss://ytremote.ngrok.io/remote', 'remote');

export default messageService;