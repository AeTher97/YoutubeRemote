import React, {Component} from 'react';
import {View} from 'react-native';
import SelectedSongBar from './SelectedSongBar';
import ListElementComponent from "./ListElementComponent";

export default class Home extends Component {

    public constructor(props: {}) {
        super(props);
    }

    public render(): JSX.Element {
    return (
      <>
          <ListElementComponent pomidorNumber={50}/>
          <View style={{height: '100%', justifyContent: 'flex-end'}}>
              <SelectedSongBar/>
              <View style={{height: '10%', backgroundColor: "#1d1d1d"}}/>
          </View>
      </>
    );
  }
}

        