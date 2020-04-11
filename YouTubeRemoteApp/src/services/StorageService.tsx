import messageService from './MessageService';
import StartMessage from '../messages/client-messages/StartMessage';
import ReceiversMessage, { Receiver } from '../messages/server-messages/ReceiversMessage';
import { MessageType } from '../messages/MessageType';
import ControlsTimeMessage from '../messages/server-messages/ControlsTimeMessage';
import ControlsSongMessage from '../messages/server-messages/ControlsSongMessage';
import React from 'react';
import Subscription from '../utils/Subscription';

class SongInfo {
    songLengthInSeconds: number;
    secondsPassed: number;
    title: string;
    performer: string;
    imageSource: string;
    playing: boolean;
}

export class Storage {
    public availableReceivers: Receiver[] = [];
    public selectedSong: SongInfo = new SongInfo();
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
        this.onChange();
    }

    private getHexId(): string {
        return Number(Math.floor(Math.random() * 15000000 + 0x111111)).toString(16);
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

    public constructor() {
        messageService.subscribe<ReceiversMessage>(MessageType.RECEIVERS, msg => this.handleReceiversMessage(msg));
        messageService.subscribe<ControlsSongMessage>(MessageType.CONTROLS_SONG, msg => this.handleSongInformation(msg));
        messageService.subscribe<ControlsTimeMessage>(MessageType.CONTROLS_TIME, msg => this.handleTimeSongInformation(msg));

        messageService.sendMessage(new StartMessage('Phone:' + this.getHexId()));
    }
}

let storageService = new StorageService();;
export default storageService;