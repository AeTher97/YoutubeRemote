const clickItem = (sectionTarget, itemTarget) => {
    const sections = document.evaluate('//*[@id="contents"]/ytmusic-shelf-renderer', document, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
    let sectionNode = sections.iterateNext();

    const searchResult = [];
    let sectionIndex = 0;
    let clicked = false;

    while (sectionNode) {

        const items = document.evaluate('.//ytmusic-responsive-list-item-renderer', sectionNode, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
        let itemNode = items.iterateNext();

        let itemIndex = 0;

        while (itemNode) {
            if (itemNode.children[0].tagName !== 'A') {
                if (sectionIndex === sectionTarget && itemTarget === itemIndex) {
                    const clickable = document.evaluate('.//ytmusic-play-button-renderer',itemNode,null,XPathResult.ORDERED_NODE_ITERATOR_TYPE).iterateNext();
                    console.log(clickable)
                    clickable.click();
                    clicked = true;
                }
            }
            if(clicked){
                break;
            }
            itemIndex++;
            itemNode = items.iterateNext();
        }
        if(clicked){
            break;
        }
        sectionIndex++;

        sectionNode = sections.iterateNext();
    }

    return searchResult;
}

const scanContent = () => {
    const sections = document.evaluate('//*[@id="contents"]/ytmusic-shelf-renderer', document, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
    let sectionNode = sections.iterateNext();

    const searchResult = [];

    while (sectionNode) {
        const sectionTitle = document.evaluate('.//h2/yt-formatted-string[1]', sectionNode, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
        let section = {
            name: sectionTitle.iterateNext().innerText,
            items: []
        }

        const items = document.evaluate('.//ytmusic-responsive-list-item-renderer', sectionNode, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
        let itemNode = items.iterateNext();

        while (itemNode) {
            console.log(itemNode);
            let item = {}

            const titles = document.evaluate('(.//yt-formatted-string)[1]', itemNode, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);
            let titleNode = titles.iterateNext();
            item.title = titleNode.innerText;

            if (itemNode.children[0].tagName === 'A') {
                if (itemNode.children[0].getAttribute('href').startsWith('channel')) {
                    item.type = 'ARTIST';
                } else if (itemNode.children[0].getAttribute('href').startsWith('playlist')) {
                    item.type = 'PLAYLIST';
                } else if (itemNode.children[0].getAttribute('href').startsWith('browse')) {
                    item.type = 'ALBUM';
                }
            } else {
                item.type = 'SONG'
            }

            section.items.push(item);
            itemNode = items.iterateNext();
        }

        searchResult.push(section);

        sectionNode = sections.iterateNext();
    }

    return searchResult;


}

const handleSearch = (message) => {
    switch (message.searchAction) {
        case 'SEARCH_PHRASE':
            console.log(message)
            const searchOpen = document.querySelector("#layout > ytmusic-nav-bar > div.center-content.style-scope.ytmusic-nav-bar > ytmusic-search-box > div > div.search-box.style-scope.ytmusic-search-box > tp-yt-paper-icon-button.search-icon.style-scope.ytmusic-search-box");
            searchOpen.click();
            const searchInput = document.querySelector("#layout > ytmusic-nav-bar > div.center-content.style-scope.ytmusic-nav-bar > ytmusic-search-box > div > div > input");
            searchInput.click();
            searchInput.value = message.phrase;
            searchInput.focus();
            const searchBox = document.querySelector("#layout > ytmusic-nav-bar > div.center-content.style-scope.ytmusic-nav-bar > ytmusic-search-box");
            const ke = new KeyboardEvent('keypress', {
                bubbles: true, cancelable: true, keyCode: 13
            });
            setTimeout(() => {
                searchBox.dispatchEvent(ke);

                setTimeout(() => {

                    const searchResult = scanContent();
                    if (socket.readyState === 1) {
                        socket.send(getStateMessage('SEARCH_RESULT', searchResult))
                    }

                }, 1000)
            }, 100);
            break;
        case 'PLAY_SONG':
            clickItem(message.section, message.element)

    }


}


setTimeout(scanContent, 35000);