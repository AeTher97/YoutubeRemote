
export default class Subscription {
    private onUnsubscribe: () => void;
    private subscribed: boolean = true;

    public unsubscribe(): void {
        if(this.subscribed) {
            this.onUnsubscribe();
            this.subscribed = false;
        }
    }

    public constructor(onUnsubscribe: () => void) {
        this.onUnsubscribe = onUnsubscribe;
    }
}