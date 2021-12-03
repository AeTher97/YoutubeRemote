import React, {Component} from 'react';
import {View} from 'react-native';
import SelectedSongBar from './SelectedSongBar';
import SuggestionsComponent from "./SuggestionsComponent";
import TopBar from "./TopBar";
import {StatusBar} from 'react-native';


export default class Home extends Component {

    public constructor(props: {}) {
        super(props);
    }

    componentDidMount() {
        StatusBar.setBarStyle( 'light-content',true)
        StatusBar.setBackgroundColor("#1d1d1d")
    }

    public render(): JSX.Element {
    return (
      <>
          <View style={{height: '100%', justifyContent: 'flex-start', display: 'flex', flexDirection: 'column' }}>
              <TopBar />
              <SuggestionsComponent />
              <SelectedSongBar/>
              <View style={{height: 50, backgroundColor: "#1d1d1d", marginTop: 'auto'}}/>
          </View>
      </>
    );
  }
}

        