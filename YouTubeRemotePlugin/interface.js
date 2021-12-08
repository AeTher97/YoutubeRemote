const initializeInterface = () => {

    console.log("Initializing interface");
    const container = document.createElement('div');
    container.id = 'ytr-container';
    container.classList.add('ytr-panel-wrapper');
    container.classList.add('ytr-column-start');


    const header = document.createElement('div');
    header.classList.add('ytr-column')

    const status = document.createElement('div');
    status.id = 'connection-status-indicator';
    status.classList.add('ytr-status')
    status.classList.add('ytr-inactive')

    const text = document.createTextNode('YtR');

    header.appendChild(text);
    header.appendChild(status);

    const content = document.createElement('div');
    content.id = 'ytr-content';
    content.classList.add('ytr-content');
    content.classList.add('ytr-hidden');
    content.classList.add('ytr-column');

    const textWrapper = document.createElement('div');
    textWrapper.id = 'connection-status-string'
    const connectionStatus = document.createTextNode('Not connected');
    textWrapper.appendChild(connectionStatus);

    const connectButton = document.createElement('button');
    connectButton.classList.add('ytr-connect-button');
    connectButton.textContent = "Connect";
    connectButton.id = 'connect-button';

    const xButton = document.createTextNode('X');
    const xButtonWrapper = document.createElement('div')
    xButtonWrapper.classList.add('ytr-x-button-wrapper');
    const xButtonContainer = document.createElement('div');
    xButtonContainer.classList.add('ytr-x-button-container');
    xButtonWrapper.appendChild(xButton);
    xButtonContainer.appendChild(xButtonWrapper);

    content.appendChild(xButtonContainer);
    content.appendChild(textWrapper);
    content.appendChild(connectButton);

    container.appendChild(header);

    xButtonWrapper.addEventListener('click', () => {
        content.classList.add('ytr-hidden');
    })

    container.addEventListener('click', () => {
        content.classList.remove('ytr-hidden');
    })

    window.addEventListener('click', function(e){
        if (!document.getElementById('ytr-content').contains(e.target)
            && !content.classList.contains('ytr-hidden') && !document.getElementById('ytr-container').contains(e.target)){
            content.classList.add('ytr-hidden');
        }
    });

    connectButton.addEventListener('click', () => {
        if (!connectionOpened) {
            initializeScripts();
        } else {
            socket.close();
        }
        content.classList.add('ytr-hidden');
    })
    body.appendChild(container);
    body.appendChild(content);

}

initializeInterface();
