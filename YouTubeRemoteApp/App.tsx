import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { Router, Scene } from 'react-native-router-flux'

import Home from './src/scenes/home/Home';
import Queue from './src/scenes/queue/Queue';

export default class HelloWorldApp extends Component {

  public constructor(props: {}) {
    super(props);
  }

  public render(): JSX.Element {
    return (
      <Router>
         <Scene key = "root">
            <Scene key = "home" component = {Home} hideNavBar={true} initial = {true} />
            <Scene key = "queue" component = {Queue} hideNavBar={true} />
         </Scene>
      </Router>
    );
  }
}
