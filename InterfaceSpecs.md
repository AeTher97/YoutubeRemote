## Communication interface between server and clients

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