import React, { Component } from 'react';
import { Text, View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native';

import { Actions } from 'react-native-router-flux';
import Subscription from '../../utils/Subscription';
import storageService, { Storage } from '../../services/StorageService';

class SongInfoState {
    public imageSource: string;
    public title: string;
    public performer: string;
    public songLengthInSeconds: number;
}

export default class SongInfo extends Component<{}, SongInfoState> {
    private styles: any = StyleSheet.create({
        title: {
            fontWeight: 'bold',
            fontSize: 15,
            color:"#fff"
        },
        performerText: {
            fontWeight: 'bold',
            fontSize: 15,
            color:"#bfbfbf"
        }
    });

    private storageSubscription: Subscription;

    public constructor(props: {}) {
        super(props);
        this.state = this.createStateFromStorage(storageService.storage);
        this.storageSubscription = storageService.subscribe(storage => this.onStorageChange(storage));
    }

    public componentWillUnmount(): void {
        this.storageSubscription.unsubscribe();
    }

    public render(): JSX.Element {
        return (
            <TouchableWithoutFeedback onPress={this.goToQueueView}>
                <View style={{flexDirection: 'row', width: '80%'}}>
                    <Image source={{uri: this.state.imageSource}} style={{aspectRatio: 1, height:'100%'}}></Image>
                    <View style={{ width: 10 }}>
                    </View>
                    <View style={{justifyContent: 'space-around'}}>
                        <Text style={this.styles.title}>{this.state.title}</Text>
                        <Text style={this.styles.performerText}>{this.state.performer} · {this.getFormattedSongLength()}</Text>
                    </View>
                </View>
            </TouchableWithoutFeedback>
        );
    }

    private createStateFromStorage(storage: Storage): SongInfoState {
        return {
            imageSource: storage.selectedSong.imageSource,
            title: storage.selectedSong.title,
            performer: storage.selectedSong.performer,
            songLengthInSeconds: storage.selectedSong.songLengthInSeconds
        };
    }
    
    private onStorageChange(storage: Storage): void {
        this.setState(this.createStateFromStorage(storage));
    }

    private goToQueueView(): void {
        Actions.jump('queue');
    }

    private getFormattedSongLength(): string {
        const minutes = Math.floor(this.state.songLengthInSeconds / 60);
        const seconds = this.state.songLengthInSeconds - (minutes * 60);
        const secondsString = seconds < 10 ? '0' + seconds : seconds;
        return minutes + ':' + secondsString;
    }
}
