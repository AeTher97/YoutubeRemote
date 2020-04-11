import React, { Component } from 'react';
import { View, Image, ImageBackground } from 'react-native';
import storageService, { Storage } from '../../services/StorageService';
import Subscription from '../../utils/Subscription';

class CurrentSongState {
  imageSource: string;
}

export default class CurrentSong extends Component<{}, CurrentSongState> {

  private storageSubscription: Subscription;

  public constructor(props: {}) {
      super(props);
      this.state = this.createStateFromStorage(storageService.storage);
      this.storageSubscription = storageService.subscribe(storage => this.setState(this.createStateFromStorage(storage)));
  }

  public componentWillUnmount(): void {
      this.storageSubscription.unsubscribe();
  }

  public render(): JSX.Element {
    return (
      <>
        <View style={{height: '40%', backgroundColor: "#1d1d1d", justifyContent: 'center'}}>
          <ImageBackground source={{uri: this.state.imageSource}} style={{aspectRatio: 1, width:'100%'}}>
            <View style={{flex: 1, backgroundColor: 'rgba(0,0,0,0.5)'}}/>
          </ImageBackground>
        </View>
      </>
    );
  }

  createStateFromStorage(storage: Storage): CurrentSongState {
    return {
      imageSource: storage.selectedSong.largeImageSource
    }
  }
}

        