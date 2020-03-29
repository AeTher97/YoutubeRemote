import React, { Component } from 'react';
import { Text, View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native';

import { Actions } from 'react-native-router-flux';

import messageService, { MessageSubscription } from '../../services/MessageService';
import { MessageType } from '../../messages/MessageType';
import ControlsSongMessage from '../../messages/server-messages/ControlsSongMessage';
import ControlsTimeMessage from '../../messages/server-messages/ControlsTimeMessage';
import { Icon } from 'react-native-elements';

interface SongInfoProps {
    title: string,
    performer: string,
    imageSource: string,
    songLengthInSeconds: number
}

export default class SongInfo extends Component<SongInfoProps> {
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

    public constructor(props: SongInfoProps) {
        super(props);
    }

    public render(): JSX.Element {
        return (
            <TouchableWithoutFeedback onPress={this.goToQueueView}>
                <View style={{flexDirection: 'row', width: '80%'}}>
                    <Image source={{uri: this.props.imageSource}} style={{aspectRatio: 1, height:'100%'}}></Image>
                    <View style={{ width: 10 }}>
                    </View>
                    <View style={{justifyContent: 'space-around'}}>
                        <Text style={this.styles.title}>{this.props.title}</Text>
                        <Text style={this.styles.performerText}>{this.props.performer} · {this.getFormattedSongLength()}</Text>
                    </View>
                </View>
            </TouchableWithoutFeedback>
        );
    }

    private goToQueueView(): void {
        console.log('goToQueueView');
        Actions.queue();
    }

    private getFormattedSongLength(): string {
        const minutes = Math.floor(this.props.songLengthInSeconds / 60);
        const seconds = this.props.songLengthInSeconds - (minutes * 60);
        const secondsString = seconds < 10 ? '0' + seconds : seconds;
        return minutes + ':' + secondsString;
    }
}
