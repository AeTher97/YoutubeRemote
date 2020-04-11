import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { Actions } from 'react-native-router-flux';

export default class NextSongs extends Component {

  public constructor(props: {}) {
    super(props);
  }

  public render(): JSX.Element {
    return (
      <>
        <View style={{height: '70%', backgroundColor: "#2d2d2d"}}/>
      </>
    );
  }
}

        