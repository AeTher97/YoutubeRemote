import {ElementType} from "../../messages/server-messages/HomeMessage";
import React, {Component} from "react";
import {Image, StyleSheet, Text, View} from 'react-native';

interface ListElementProps {
    imageSource: string;
    title: string;
    subText: string;
    type: ElementType
}


export default class ListElement extends Component<ListElementProps, {}> {

    private styles: any = StyleSheet.create({});


    public render(): JSX.Element {
        return (
            <View style={{flexDirection: 'row', width: '30%'}}>
                <Image source={{uri: this.props.imageSource}} style={{aspectRatio: 1, height: '50%'}}/>
                <View style={{width: 10}}>
                </View>
                <View style={{justifyContent: 'space-around'}}>
                    <Text style={this.styles.title}>{this.props.title}</Text>
                    <Text style={this.styles.performerText}>{this.props.subText}</Text>
                </View>
            </View>
        )
    }


}