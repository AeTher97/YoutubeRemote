import messageService from './MessageService';
import ReceiversMessage, {Receiver} from '../messages/server-messages/ReceiversMessage';
import {MessageType} from '../messages/MessageType';
import ControlsTimeMessage from '../messages/server-messages/ControlsTimeMessage';
import ControlsSongMessage from '../messages/server-messages/ControlsSongMessage';
import Subscription from '../utils/Subscription';
import {QueueMessage, SongInQueue} from '../messages/server-messages/QueueMessage';
import HomeMessage, {HomeInfo} from "../messages/server-messages/HomeMessage";

class SongInfo {
    songLengthInSeconds: number;
    secondsPassed: number;
    title: string;
    performer: string;
    imageSource: string;
    largeImageSource: string;
    playing: boolean;
}


export class Storage {
    public availableReceivers: Receiver[] = [];
    public selectedSong: SongInfo = new SongInfo();
    public queue: SongInQueue[] = [];
    public homeInfo: HomeInfo[] = [];
}

export class StorageService {
    public storage: Storage = new Storage();

    private subscriptionCallbacks: ((storage: Storage) => void)[] = [];

    private handleReceiversMessage(msg: ReceiversMessage): void {
        this.storage.availableReceivers = msg.receivers;
        this.onChange();
    }

    private handleTimeSongInformation(msg: ControlsTimeMessage): void {
        this.storage.selectedSong.songLengthInSeconds = msg.content.maxTime;
        this.storage.selectedSong.secondsPassed = msg.content.time;
        this.storage.selectedSong.playing = msg.content.playing;
        this.onChange();
    }

    private handleSongInformation(msg: ControlsSongMessage): void {
        this.storage.selectedSong.title = msg.content.title;
        this.storage.selectedSong.performer = msg.content.performer;
        this.storage.selectedSong.imageSource = msg.content.imgSrc;
        this.storage.selectedSong.largeImageSource = msg.content.imgSrcLarge;
        this.onChange();
    }


    private handleQueueInformation(msg: QueueMessage): void {
        this.storage.queue = msg.content;
        this.onChange();
    }

    public constructor() {
        messageService.subscribe<ReceiversMessage>(MessageType.RECEIVERS, msg => this.handleReceiversMessage(msg));
        messageService.subscribe<ControlsSongMessage>(MessageType.CONTROLS_SONG, msg => this.handleSongInformation(msg));
        messageService.subscribe<ControlsTimeMessage>(MessageType.CONTROLS_TIME, msg => this.handleTimeSongInformation(msg));
        messageService.subscribe<QueueMessage>(MessageType.QUEUE, msg => this.handleQueueInformation(msg));
        messageService.subscribe<HomeMessage>(MessageType.HOME, msg => this.handleHomeInformation(msg));
    }

    private onChange(): void {
        this.subscriptionCallbacks.forEach(c => {
            c(this.storage);
        });
    }

    public subscribe(callback: (storage: Storage) => void): Subscription {
        this.subscriptionCallbacks.push(callback);
        return new Subscription(() => {
            this.subscriptionCallbacks = this.subscriptionCallbacks.filter(s => s !== callback);
        });
    }

    private handleHomeInformation(msg: HomeMessage): void {
        this.storage.homeInfo = msg.content;
        this.onChange();
    }
}

let storageService = new StorageService();

export default storageService;