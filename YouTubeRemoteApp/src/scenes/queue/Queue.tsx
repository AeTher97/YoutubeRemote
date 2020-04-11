import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import CurrentSong from './CurrentSong';
import NextSongs from './NextSongs';

export default class Queue extends Component {
  public constructor(props: {}) {
    super(props);
  }

  public render(): JSX.Element {
    return (
      <>
        <CurrentSong/>
        <NextSongs/>
      </>
    );
  }
}

        