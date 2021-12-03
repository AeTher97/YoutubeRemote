import {ElementType} from "../../messages/server-messages/HomeMessage";
import React, {Component} from "react";
import {Image, StyleSheet, Text, TouchableNativeFeedback, TouchableWithoutFeedback, View} from 'react-native';

interface ListElementProps {
    imageSource: string;
    title: string;
    subText: string;
    type: ElementType
}


export default class ListElement extends Component<ListElementProps, {}> {


    private styles: any = StyleSheet.create({
        title: {
            fontWeight: 'bold',
            fontSize: 15,
            color: "#fff",
            textAlign: 'center'
        },
        performerText: {
            fontWeight: 'bold',
            fontSize: 15,
            color: "#bfbfbf",
            textAlign: 'center'

        },
        circle: {
            aspectRatio: 1,
            width: '90%',
            borderRadius: 500
        },
        square: {
            aspectRatio: 1,
            width: '90%',
            alignItems: 'center'
        }
    });

    private style: any;

    constructor(props) {
        super(props);
        if (this.props.type === ElementType.CIRCLE) {
            this.style = this.styles.circle;
        } else {
            this.style = this.styles.square;
        }

    }


    public render(): JSX.Element {
        return (
            <TouchableNativeFeedback
                onPress={() => {
                    console.log('xd')
                }}
                useForeground={true}
                background={TouchableNativeFeedback.Ripple('#3c3c3c', false)}>

                <View style={{
                    width: 150,
                    alignSelf: 'flex-start',
                    alignItems: 'center',
                    paddingTop: 10,
                    paddingBottom: 10,
                    marginLeft: 5
                }}>
                    <Image source={{uri: this.props.imageSource}} style={this.style}/>
                    <View style={{}}>
                        <Text style={this.styles.title} numberOfLines={2}>{this.props.title}</Text>
                        <Text style={this.styles.performerText} numberOfLines={2}>{this.props.subText}</Text>
                    </View>
                </View>
            </TouchableNativeFeedback>

        )
    }


}