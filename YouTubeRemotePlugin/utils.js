let oldSize;


function scrollToBottom() {
    let wrapperHeight = document.querySelector("#browse-page > ytmusic-section-list-renderer").children[1];
    oldSize = wrapperHeight.getBoundingClientRect().height;
    window.scrollTo(0, oldSize + 4000);


    const scrollInterval = setInterval(() => {
        const innerWrapperHeight = document.querySelector("#browse-page > ytmusic-section-list-renderer").children[1];
        window.scrollTo(0, oldSize + 4000);
        if (oldSize === innerWrapperHeight.getBoundingClientRect().height) {
            window.scrollTo(0, 0);
            clearInterval(scrollInterval);
        } else {
            oldSize = innerWrapperHeight.getBoundingClientRect().height;
        }

    }, 1000)
}