import React, {Component} from "react";
import {ScrollView, View, Text} from "react-native";
import storageService, {Storage} from "../../services/StorageService";
import Subscription from "../../utils/Subscription";
import {ElementType, HomeInfo} from "../../messages/server-messages/HomeMessage";
import ListElement from "./ListElement";


interface SongListComponentProps {
    header: string;
    content: [
        {
            "title": string,
            "imgSrc": string,
            "subText": string,
            "type": ElementType
        }
    ]
}


export default class SongListComponent extends Component<SongListComponentProps, {}> {

    public constructor(props: SongListComponentProps) {
        super(props);

    }


    public render(): JSX.Element {

        if (this.props.content.length < 1) {
            return <View style={{backgroundColor: '#000', flex: 1}}/>;
        } else {
            return (
                <View style={{
                    width: '100%',
                    justifyContent: "flex-start",
                    alignItems: "center",
                    backgroundColor: "#000",
                    flex: 1,
                    marginBottom: 45,
                    marginTop: 5
                }}>
                    <View style={{paddingLeft: 10}}>
                        <Text style={{color: 'white', fontSize: 27, fontWeight: 'bold'}}>{this.props.header}</Text>
                        <ScrollView
                            horizontal={true}
                            showsHorizontalScrollIndicator={false}
                            scrollEventThrottle={200}
                            decelerationRate={'normal'}
                        >
                            {
                                this.props.content.map((item,index) =>
                                    <ListElement
                                        imageSource={item.imgSrc}
                                        title={item.title}
                                        subText={item.subText}
                                        type={item.type}
                                        key = {index}
                                    />
                                )
                            }

                        </ScrollView>
                    </View>
                </View>
            );
        }
    }



}