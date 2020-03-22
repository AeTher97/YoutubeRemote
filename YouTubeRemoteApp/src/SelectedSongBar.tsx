import React, { Component } from 'react';
import { Text, View, StyleSheet, Image } from 'react-native';

import messageService, { MessageSubscription } from './services/MessageService';
import { MessageType } from './messages/MessageType';
import ControlsSongMessage from './messages/server-messages/ControlsSongMessage';
import ControlsTimeMessage from './messages/server-messages/ControlsTimeMessage';

interface SelectedSongBarState {
    songLengthInSeconds: number,
    secondsPassed: number,
    title: string,
    performer: string,
    isSongSelected: boolean,
    imageSource: string
}

export default class SelectedSongBar extends Component<{}, SelectedSongBarState> {

    private controlsSongSubscription: MessageSubscription;
    private controlsTimeSubscription
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

    private getElementIfSongIsSelected(): JSX.Element {
        if(!this.state.isSongSelected)
            return (<></>);
        return (
            <>
                <View style={{ height: '98%', flexDirection: 'row', backgroundColor: "#1d1d1d"}}>
                    <Image source={{uri: this.state.imageSource}} style={{aspectRatio: 1, height:'100%'}}></Image>
                    <View style={{ width: 10 }}>
                    </View>
                    <View style={{justifyContent: 'space-around'}}>
                        <Text style={this.styles.title}>{this.state.title}</Text>
                        <Text style={this.styles.performerText}>{this.state.performer} · {this.getFormattedSongLength()}</Text>
                    </View>
                </View>
                <View style={{ height: '2%', flexDirection: 'row', backgroundColor: "#9e9e9e"}}>
                    <View style= {{ width: this.getProgressBarPercentage() + '%', backgroundColor: '#fff'}}/>
                </View>
            </>
        );
    }

    private getProgressBarPercentage(): number {
        if(this.state.songLengthInSeconds !== 0)
            return 100 * this.state.secondsPassed / this.state.songLengthInSeconds;
        return 0;
    }

    private getFormattedSongLength(): string {
        const minutes = Math.floor(this.state.songLengthInSeconds / 60);
        const seconds = this.state.songLengthInSeconds - (minutes * 60);
        const secondsString = seconds < 10 ? '0' + seconds : seconds;
        return minutes + ':' + secondsString;
    }

    private handleTimeSongInformation(msg: ControlsTimeMessage): void {
        this.setState(() => {
            return {
                songLengthInSeconds: msg.content.maxTime,
                secondsPassed: msg.content.time,
                isSongSelected: msg.content.playing
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
            isSongSelected: false,
            imageSource: ''
        };

        this.controlsSongSubscription = messageService.subscribe<ControlsSongMessage>(MessageType.CONTROLS_SONG, msg => this.handleSongInformation(msg));
        this.controlsSongSubscription = messageService.subscribe<ControlsTimeMessage>(MessageType.CONTROLS_TIME, msg => this.handleTimeSongInformation(msg));
    }
}
