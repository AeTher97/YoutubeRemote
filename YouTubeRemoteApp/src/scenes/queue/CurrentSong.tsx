import React, { Component } from 'react';
import { View, Text, ImageBackground } from 'react-native';
import storageService, { Storage } from '../../services/StorageService';
import Subscription from '../../utils/Subscription';

class CurrentSongState {
  imageSource: string;
  title: string;
  performer: string;
  secondsPassed: number;
  songLengthInSeconds: number;
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
        <View style={{height: '5%', backgroundColor: '#222'}}/>
        <View style={{height: '10%', backgroundColor: '#222', justifyContent: 'space-around', alignItems: 'center'}}>
          <Text style= {{
            fontWeight: 'bold',
            fontSize: 25,
            color:"#fff"}}>
            {this.state.title}
          </Text>
          <Text style= {{
            fontWeight: 'bold',
            fontSize: 15,
            color:"#aaa"}}>
            {this.state.performer}
          </Text>
        </View>
        <View style={{height: '35%', backgroundColor: '#222'}}/>
      </>
    );
  }

  private createStateFromStorage(storage: Storage): CurrentSongState {
    return {
      imageSource: storage.selectedSong.largeImageSource,
      title: storage.selectedSong.title,
      performer: storage.selectedSong.performer,
      secondsPassed: storage.selectedSong.secondsPassed,
      songLengthInSeconds: storage.selectedSong.songLengthInSeconds
    }
  }
}

        