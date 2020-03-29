import React, { Component } from 'react';
import { Text, View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native';

import { Actions } from 'react-native-router-flux';

import messageService, { MessageSubscription } from '../../services/MessageService';
import { MessageType } from '../../messages/MessageType';
import ControlsSongMessage from '../../messages/server-messages/ControlsSongMessage';
import ControlsTimeMessage from '../../messages/server-messages/ControlsTimeMessage';
import { Icon } from 'react-native-elements';

class PlayButtonProps {
    playing: boolean;
    onClick: () => void;
}

export default class PlayButton extends Component<PlayButtonProps> {

    public constructor(props: PlayButtonProps) {
        super(props);
    }

    public render(): JSX.Element {
        return (
            <Icon 
                        name={this.getCurrentIconName()}
                        type='material'
                        color='#fff'
                        underlayColor='#0000'
                        onPress={this.props.onClick}
                        size={30}/>
        );
    }

    private getCurrentIconName(): string {
        return this.props.playing ? 'pause' : 'play-arrow';
    }
}
