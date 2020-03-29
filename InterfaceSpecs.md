## Communication interface between server and clients

### Controllers

{
  "messageType": "START",
  "memberType" : "CONTROLLER" //{RECEIVER},
  "deviceName" : "my-phone"
}


{
  "messageType" : "MEDIA_CONTROL",
  "action" : "PLAY" //{PAUSE, NEXT, PREVIOUS},
  "timeSet" : 125
}

{
  "messageType" : "QUEUE_CONTROL",
  "action" : "MOVE" //{REMOVE,NEXT, PREVIOUS},
  "currentIndex": 12,
  "targetIndex": 3
}


{
  "messageType" : "STOP",
  "deviceName" : "my-phone"
}

{
  "messageType" : "SET_RECEIVER",
  "deviceName" : "my-browser"
}

{"messageType":"QUEUE",
"content":[{"index":0,"title":"Brothers In Arms","performer":"Dire Straits","imgSrc":"https://lh3.googleusercontent.com/c-rSp0l4FeZf4iiwSQhFeZ_3hNalG8Bgu-8WZCQDK0KE61OVu99a-_gOu-eifFuXCNI2AZeeLkFuyPWq-A=w60-h60-s-l90-rj"},
{"index":1,"title":"Ziggy Stardust","performer":"David Bowie","imgSrc":"https://lh3.googleusercontent.com/ldn2zd7THwOFCPWJkEc1IM7mL1HcaR6IEF9oUHc90VvkJYu7CHIsRpLg2ajQugWCB_hmsDcXMS4N4XMU=w60-h60-l90-rj","selected":true}]}

This one sends only queue updates e.g. if picture of the song changes or selected attribute changes.


### Receivers

{
  "messageType": "START",
  "memberType" : "CONTROLLER" //{RECEIVER},
  "deviceName" : "my-phone"
}

{
  "messageType" : "CONTROLS_TIME",
  "content" :  {playing: false, time: 0, maxTime: 0}
}

{
  "messageType" : "CONTROLS_SONG",
  "content" : {title: '', performer: '', imgSrc: ''}
}

{
  "messageType" : "STOP",
  "deviceName" : "my-phone"
}


### Server

{
  "messageType" : "RECEIVERS",
  "receivers" : [{ "deviceName" : "my-browser-music" , "uuid" :  "lfsjljl3l24j3o4jij3"},{"deviceName" : "my-browser-youtube" , "uuid" :  "fldsjfldsjl"}]
}

{
  "messageType" : "MEDIA_CONTROL",
  "action" : "PLAY" //{PAUSE, NEXT, PREVIOUS},
  "timeSet" : 125
}

{
  "messageType" : "QUEUE_CONTROL",
  "action" : "MOVE" //{REMOVE,NEXT, PREVIOUS},
  "currentIndex": 12,
  "targetIndex": 3
}

{
  "messageType" : "CURRENT_RECEIVER",
  "deviceName" : "my-browser"
}

{
  "messageType" : "CONTROLS_TIME",
  "content" :  {playing: false, time: 0, maxTime: 0}
}

{
  "messageType" : "CONTROLS_SONG",
  "content" : {title: '', performer: '', imgSrc: ''}
}


{"messageType":"QUEUE",
"content":[{"index":0,"title":"Brothers In Arms","performer":"Dire Straits","imgSrc":"https://lh3.googleusercontent.com/c-rSp0l4FeZf4iiwSQhFeZ_3hNalG8Bgu-8WZCQDK0KE61OVu99a-_gOu-eifFuXCNI2AZeeLkFuyPWq-A=w60-h60-s-l90-rj"},
{"index":1,"title":"Ziggy Stardust","performer":"David Bowie","imgSrc":"https://lh3.googleusercontent.com/ldn2zd7THwOFCPWJkEc1IM7mL1HcaR6IEF9oUHc90VvkJYu7CHIsRpLg2ajQugWCB_hmsDcXMS4N4XMU=w60-h60-l90-rj","selected":true}]}

This one sends only queue updates e.g. if picture of the song changes or selected attribute changes.