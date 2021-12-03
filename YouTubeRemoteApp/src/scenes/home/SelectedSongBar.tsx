import React, {Component} from 'react';
import {View} from 'react-native';

import storageService, {Storage} from '../../services/StorageService';
import SongInfo from './SongInfo';
import PlayButton from './PlayButton';
import Subscription from '../../utils/Subscription';

class SelectedSongBarState {
    public isSongSelected: boolean;
    public secondsPassed: number;
    public songLengthInSeconds: number;
}

export default class SelectedSongBar extends Component<{}, SelectedSongBarState> {
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

        this.state.isSongSelected ? <View style={{ height: 50, backgroundColor: 'transparent' }}>
            {this.getElementIfSongIsSelected()}
        </View> : <></>
        );
    }

    private createStateFromStorage(storage: Storage): SelectedSongBarState {
        return {
            isSongSelected: !!storage.selectedSong.title,
            secondsPassed: storage.selectedSong.secondsPassed,
            songLengthInSeconds: storage.selectedSong.songLengthInSeconds
        };
    }

    private onStorageChange(storage: Storage): void {
        this.setState(this.createStateFromStorage(storage));
    }

    private getElementIfSongIsSelected(): JSX.Element {
        if(!this.state.isSongSelected)
            return;
        return (
            <>
                <View style={{ height: '98%', flexDirection: 'row', alignItems: "center", justifyContent: 'flex-start', backgroundColor: "#1d1d1d"}}>
                    <SongInfo/>
                    <PlayButton/>
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
}
