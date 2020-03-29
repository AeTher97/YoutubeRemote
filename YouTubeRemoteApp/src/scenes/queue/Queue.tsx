import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { Actions } from 'react-native-router-flux';

export default class Queue extends Component {

  public constructor(props: {}) {
    super(props);
  }

  public render(): JSX.Element {
    return (
      <>
      
        <View style={{height: '100%'}}>
          <View style={{height: '30%', backgroundColor: "#1d1d1d"}}/>
        </View>
      </>
    );
  }
}

        