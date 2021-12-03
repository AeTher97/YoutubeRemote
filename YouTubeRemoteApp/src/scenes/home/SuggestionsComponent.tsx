import React, {Component} from "react";
import {ScrollView, View} from "react-native";
import storageService, {Storage} from "../../services/StorageService";
import Subscription from "../../utils/Subscription";
import {HomeInfo} from "../../messages/server-messages/HomeMessage";
import ListElement from "./ListElement";
import SongListComponent from "./SongListComponent";
import SongInfo from "./SongInfo";


interface ListElementState {
    homeInfo: HomeInfo[]
}


export default class SuggestionsComponent extends Component<{}, ListElementState> {
    private storageSubscription: Subscription;

    public constructor(props: {}) {
        super(props);
        this.state = this.createStateFromStorage(storageService.storage);
        console.log(this.state);
        this.storageSubscription = storageService.subscribe(storage => this.onStorageChange(storage));
    }

    public componentWillUnmount(): void {
        this.storageSubscription.unsubscribe();
    }

    public render(): JSX.Element {

        if (this.state.homeInfo.length === 0) {
            return <View style={{backgroundColor: '#000', flex: 1}}/>;
        } else {
            return (
                <View style={{
                    width: '100%',
                    justifyContent: "flex-start",
                    alignItems: "center",
                    backgroundColor: "#000",
                    flex: 1,
                    overflow: 'hidden'
                }}>
                    <ScrollView
                        scrollEventThrottle={200}
                        decelerationRate="fast"
                    >
                    {this.state.homeInfo.map((item,index) =>
                        <SongListComponent content={item.content} header={item.header}  key={index} />
                        )}
                    </ScrollView>

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