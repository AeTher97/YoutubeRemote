import React, {Component} from "react";
import {Text, View} from "react-native";
import storageService, {Storage} from "../../services/StorageService";
import Subscription from "../../utils/Subscription";
import {HomeInfo} from "../../messages/server-messages/HomeMessage";
import ListElement from "./ListElement";

interface ListElementProps {
    pomidorNumber: number;
}


interface ListElementState {
    homeInfo: HomeInfo[]
}


export default class ListElementComponent extends Component<ListElementProps, ListElementState> {
    private storageSubscription: Subscription;

    public constructor(props: ListElementProps) {
        super(props);
        this.state = this.createStateFromStorage(storageService.storage);
        console.log(this.state);
        this.storageSubscription = storageService.subscribe(storage => this.onStorageChange(storage));
    }

    public render(): JSX.Element {

        if (this.state.homeInfo.length === 0) {
            return <></>;
        } else {
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
                    <ListElement
                        imageSource={this.state.homeInfo[0].content[0].imgSrc}
                        title={this.state.homeInfo[0].content[0].title}
                        subText={this.state.homeInfo[0].content[0].subText}
                        type={this.state.homeInfo[0].content[0].type}
                    />

                </View>
            );
        }
    }

    private onStorageChange(storage: Storage): void {
        this.setState(this.createStateFromStorage(storageService.storage));
    }

    private createStateFromStorage(storage: Storage): ListElementState {
        console.log(storage.homeInfo)
        return {
            homeInfo: storage.homeInfo
        };

    }

}