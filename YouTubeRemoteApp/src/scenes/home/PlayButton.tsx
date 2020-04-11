import React, { Component } from 'react';

import { Icon } from 'react-native-elements';

import messageService from '../../services/MessageService';
import MediaControlMessage, { MediaControlAction } from '../../messages/client-messages/MediaControlMessage';
import storageService from '../../services/StorageService';
import Subscription from '../../utils/Subscription';

class PlayButtonState {
    isSongPlaying: boolean
}

export default class PlayButton extends Component<{}, PlayButtonState> {
    private storageSubscription: Subscription;

    public constructor(props: {}) {
        super(props);
        this.state = {isSongPlaying: storageService.storage.selectedSong.playing};
        this.storageSubscription = storageService.subscribe(storage => this.setState({isSongPlaying: storage.selectedSong.playing}));
    }

    public componentWillUnmount(): void {
        this.storageSubscription.unsubscribe();
    }

    public render(): JSX.Element {
        return (
            <Icon 
                name={this.getCurrentIconName()}
                type='material'
                color='#fff'
                underlayColor='#0000'
                onPress={() => this.togglePlaying()}
                size={30}/>
        );
    }

    private togglePlaying(): void {
        if(this.state.isSongPlaying)
            messageService.sendMessage(new MediaControlMessage(MediaControlAction.PAUSE));
        else
            messageService.sendMessage(new MediaControlMessage(MediaControlAction.PLAY));
    }

    private getCurrentIconName(): string {
        return this.state.isSongPlaying ? 'pause' : 'play-arrow';
    }
}
