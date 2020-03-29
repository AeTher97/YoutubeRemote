import React, { Component } from 'react';
import { Text, View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native';

import { Actions } from 'react-native-router-flux';

import messageService, { MessageSubscription, MessageService } from '../../services/MessageService';
import { MessageType } from '../../messages/MessageType';
import ControlsSongMessage from '../../messages/server-messages/ControlsSongMessage';
import ControlsTimeMessage from '../../messages/server-messages/ControlsTimeMessage';
import { Icon } from 'react-native-elements';
import SongInfo from './SongInfo';
import PlayButton from './PlayButton';
import MediaControlMessage, { MediaControlAction } from '../../messages/client-messages/MediaControlMessage';

interface SelectedSongBarState {
    songLengthInSeconds: number,
    secondsPassed: number,
    title: string,
    performer: string,
    imageSource: string,
    playing: boolean
}

export default class SelectedSongBar extends Component<{}, SelectedSongBarState> {

    private controlsSongSubscription: MessageSubscription;
    private controlsTimeSubscription: MessageSubscription;
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

    public constructor(props: {}) {
        super(props);
        this.init();
    }

    public render(): JSX.Element {
        return (
        <View style={{ height: '10%', backgroundColor: 'transparent' }}>
            {this.getElementIfSongIsSelected()}
        </View>
        );
    }

    private togglePlaying(): void {
        if(this.state.playing)
            messageService.sendMessage(new MediaControlMessage(MediaControlAction.PAUSE));
        messageService.sendMessage(new MediaControlMessage(MediaControlAction.PLAY));
    }

    private onProgressClick(): void {
        console.log('onProgressClick');
    }

    private isSongSelected(): boolean {
        return !!this.state.title;
    }

    private getElementIfSongIsSelected(): JSX.Element {
        if(!this.isSongSelected())
            return (<></>);
        return (
            <>
                <View style={{ height: '98%', flexDirection: 'row', alignItems: "center", backgroundColor: "#1d1d1d"}}>
                    <SongInfo title={this.state.title} performer={this.state.performer} songLengthInSeconds={this.state.songLengthInSeconds} imageSource={this.state.imageSource}/>
                    <PlayButton playing={this.state.playing} onClick={() => this.togglePlaying()}/>
                </View>
                <TouchableWithoutFeedback onPress={this.onProgressClick}>
                    <View style={{ height: '2%', flexDirection: 'row', backgroundColor: "#9e9e9e"}}>
                        <View style= {{ width: this.getProgressBarPercentage() + '%', backgroundColor: '#fff'}}/>
                    </View>
                </TouchableWithoutFeedback>
            </>
        );
    }

    private getProgressBarPercentage(): number {
        if(this.state.songLengthInSeconds !== 0)
            return 100 * this.state.secondsPassed / this.state.songLengthInSeconds;
        return 0;
    }

    private handleTimeSongInformation(msg: ControlsTimeMessage): void {
        this.setState(() => {
            return {
                songLengthInSeconds: msg.content.maxTime,
                secondsPassed: msg.content.time,
                playing: msg.content.playing
            }
        })
    }

    private handleSongInformation(msg: ControlsSongMessage): void {
        this.setState(() => {
            return {
                title: msg.content.title,
                performer: msg.content.performer,
                imageSource: msg.content.imgSrc
            }
        })
    }

    public componentWillUnmount(): void {
        this.controlsSongSubscription.unsubscribe();
        this.controlsTimeSubscription.unsubscribe();
    }

    private init(): void {
        this.state = {
            songLengthInSeconds: 0,
            secondsPassed: 0,
            title: '',
            performer: '',
            imageSource: '',
            playing: false
        }

        this.controlsSongSubscription = messageService.subscribe<ControlsSongMessage>(MessageType.CONTROLS_SONG, msg => this.handleSongInformation(msg));
        this.controlsTimeSubscription = messageService.subscribe<ControlsTimeMessage>(MessageType.CONTROLS_TIME, msg => this.handleTimeSongInformation(msg));
    }
}
