## Communication interface between server and clients

### Controllers

{
  "messageType": "START",
  "memberType" : "CONTROLLER" //{RECEIVER},
  "deviceName" : "my-phone"
}

{
  "messageType" : "MEDIA_CONTROL",
  "action" : "PLAY" //{PAUSE, NEXT, PREVIOUS}
}

{
  "messageType" : "STOP",
  "deviceName" : "my-phone"
}

{
  "messageType" : "SET_RECEIVER",
  "deviceName" : "my-browser"
}

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
  "messageType" : "CONTROLS",
  "content" : {playing: false, time: 0, maxTime: 0}
}

{
  "messageType" : "SET_RECEIVER",
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