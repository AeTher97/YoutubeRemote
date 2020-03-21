import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default class HelloWorldApp extends Component {
  render(): JSX.Element {
    return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text>Hello, world!</Text>
      </View>
    );
  }
}
