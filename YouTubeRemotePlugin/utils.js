let oldSize;
const body = document.querySelector("body");

const ID =  ()=> {
    return '_' + Math.random().toString(36).substr(2, 9);
};

function scrollToBottom() {
    let wrapperHeight = document.querySelector("#browse-page > ytmusic-section-list-renderer").children[1];
    oldSize = wrapperHeight.getBoundingClientRect().height;
    window.scrollTo(0, oldSize + 4000);


    const scrollInterval = setInterval(() => {
        const innerWrapperHeight = document.querySelector("#browse-page > ytmusic-section-list-renderer").children[1];
        window.scrollTo(0, oldSize + 4000);
        if (oldSize === innerWrapperHeight.getBoundingClientRect().height) {
            clearInterval(scrollInterval);

            const buttons = document.querySelectorAll("#next-items-button");

            let counter=0;

            const interval = setInterval(()=> {
                for (const button of buttons) {
                    button.click();
                }
                counter++;
                if(counter===4){
                    window.scrollTo(0, 0);
                    clearInterval(interval);
                    setTimeout(() => {
                        emitState("HOME", getHomeState())
                    }, 200)
                }
            },200);


        } else {
            oldSize = innerWrapperHeight.getBoundingClientRect().height;
        }

    }, 1000)
}