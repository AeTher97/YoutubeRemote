import React, {Component} from 'react';
import {Text, View} from 'react-native';



export default class TopBar extends Component<{}, {}> {

    public constructor(props: {}) {
        super(props);
    }


    public render(): JSX.Element {
        return (
            <View style={{
                height: 85,
                backgroundColor: "#1d1d1d",
                flexDirection: 'column',
                justifyContent: 'flex-end'
            }}>
                <Text style={{
                    fontWeight: 'bold',
                    fontSize: 20,
                    color: "#fff",
                    padding: 10
                }}>YTRemote</Text>
            </View>
        );
    }


}
