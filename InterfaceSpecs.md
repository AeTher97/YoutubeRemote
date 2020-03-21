## Communication interface between server and clients

### Clients

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


### Server

{
  "messageType" : "RECEIVERS",
  "receivers" : [{ "deviceName" : "my-browser-music" , "uuid" :  "lfsjljl3l24j3o4jij3"},{"deviceName" : "my-browser-youtube" , "uuid" :  "fldsjfldsjl"}]
}