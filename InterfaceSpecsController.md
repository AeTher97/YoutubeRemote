## Communication interface between server and clients

### Controllers

{
  "messageType": "START",
  "memberType" : "CONTROLLER" //{RECEIVER},
  "deviceName" : "my-phone"
}


{
  "messageType" : "MEDIA_CONTROL",
  "action" : "PLAY", //{PAUSE, NEXT, PREVIOUS, SET_TIME, SHUFFLE, VOLUME, REPEAT},
  "timeSet" : 125, //optional(required in case of set time)
  "volumeSet" : 60, //percentage optional(required in case of volume)
  "repeatSet" : "REPEAT_OFF" //optional(required in case of repeat){REPEAT_ONE,REPEAT_ALL}
}

{
  "messageType" : "QUEUE_CONTROL",
  "action" : "MOVE" //{REMOVE,PLAY_NEXT},
  "currentIndex": 12, //optional(required in case of MOVE)
  "targetIndex": 3 //
}


{
  "messageType" : "STOP",
  "deviceName" : "my-phone"
}

{
  "messageType" : "SET_RECEIVER",
  "deviceName" : "my-browser"
}

{
  "messageType" : "HEART_BEAT"
}


### Server

{
  "messageType" : "RECEIVERS",
  "receivers" : [{ "deviceName" : "my-browser-music" , "uuid" :  "lfsjljl3l24j3o4jij3"},{"deviceName" : "my-browser-youtube" , "uuid" :  "fldsjfldsjl"}]
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

{
  "messageType" : "CONTROLS_DETAILS",
  "content" : {volume: 69, repeatType: 'REPEAT_OFF', muted: false}//{REPEAT_ALL,REPEAT_ONE} }
}


{"messageType":"QUEUE",
"content":[{"index":0,"title":"Brothers In Arms","performer":"Dire Straits","imgSrc":"https://lh3.googleusercontent.com/c-rSp0l4FeZf4iiwSQhFeZ_3hNalG8Bgu-8WZCQDK0KE61OVu99a-_gOu-eifFuXCNI2AZeeLkFuyPWq-A=w60-h60-s-l90-rj"},
{"index":1,"title":"Ziggy Stardust","performer":"David Bowie","imgSrc":"https://lh3.googleusercontent.com/ldn2zd7THwOFCPWJkEc1IM7mL1HcaR6IEF9oUHc90VvkJYu7CHIsRpLg2ajQugWCB_hmsDcXMS4N4XMU=w60-h60-l90-rj","selected":true}]}

This one sends only queue updates e.g. if picture of the song changes or selected attribute changes.

{
  "messageType": "HOME",
  "content": [
    {
      "index": 0,
      "header": "Your favorites",
      "content": [
        {
          "title" : "Money for nothing",
          "performer": "Dire Straits",
          "imgSrc": "https://lh3.googleusercontent.com/c-rSp0l4FeZf4iiwSQhFeZ_3hNalG8Bgu-8WZCQDK0KE61OVu99a-_gOu-eifFuXCNI2AZeeLkFuyPWq-A=w60-h60-s-l90-rj",
          "type" : "SONG"
        }
      ]
    }
  ]
  
}