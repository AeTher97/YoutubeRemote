import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import TestComponent from './src/TestComponent';
import SelectedSongBar from './src/SelectedSongBar';

export default class HelloWorldApp extends Component {

  public constructor(props: {}) {
    super(props);
  }



  public render(): JSX.Element {
    return (
      <>
        <TestComponent pomidorNumber={1} />
        <View style={{height: '100%', justifyContent: 'flex-end'}}>
          <SelectedSongBar />
          <View style={{height: '10%', backgroundColor: "#1d1d1d"}}/>
        </View>
      </>
    );
  }
}
