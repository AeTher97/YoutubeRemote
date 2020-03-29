import React, { Component } from 'react';
import { Text, View } from 'react-native';

import messageService from '../../services/MessageService';
import StartMessage from '../../messages/client-messages/StartMessage';
import { MessageType } from '../../messages/MessageType';
import ReceiversMessage from '../../messages/server-messages/ReceiversMessage';

interface PomidorProps
{
    pomidorNumber: number;
}

interface PomidorState {
    testNumber: number;
}

export default class TestComponent extends Component<PomidorProps, PomidorState> {

    public constructor(props: PomidorProps) {
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
            backgroundColor: "#000"}}>
            <Text style={{color:"#fff"}}>TestComponent text. Pomidor: {this.props.pomidorNumber}</Text>
        </View>
        );
    }

    private handleReceiversMessage(msg: ReceiversMessage): void {
        console.log('got receivers: ' + msg.receivers);
    }

    private getHexId(): string {
        return Number(Math.random() * 15000000 + 0x111111).toString(16);
    }

    private init(): void {
        this.state = {testNumber: 0};
        messageService.subscribe(MessageType.RECEIVERS, this.handleReceiversMessage);
        messageService.sendMessage(new StartMessage('Phone:' + this.getHexId()));
    }
}