import React, {Component} from 'react';
import {Text, View} from 'react-native';

interface SuggestionsProps {
    pomidorNumber: number;
}

interface PomidorState {
    testNumber: number;
}

export default class TestComponent extends Component<SuggestionsProps, PomidorState> {

    public constructor(props: SuggestionsProps) {
        super(props);
        this.init();
    }

    public render(): JSX.Element {
        return (
            <View style={{
                height: '100%',
                width: '100%',
                justifyContent: "center",
                alignItems: "center",
                position: 'absolute',
                backgroundColor: "#000"
            }}>
                <Text style={{color: "#fff"}}>TestComponent text. Pomidor: {this.props.pomidorNumber}</Text>
            </View>
        );
    }

    private init(): void {
        this.state = {testNumber: 0};
    }
}