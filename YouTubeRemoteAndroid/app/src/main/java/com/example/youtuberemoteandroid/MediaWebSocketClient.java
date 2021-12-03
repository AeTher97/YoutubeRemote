package com.example.youtuberemoteandroid;

import android.util.Log;
import android.widget.TextView;

import com.example.youtuberemoteandroid.messages.ControlSongMessage;
import com.example.youtuberemoteandroid.messages.ControlTimeMessage;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class MediaWebSocketClient extends WebSocketClient {

    private MainActivity mainActivity;
    private Timer timer = new Timer();

    public MediaWebSocketClient(URI serverUri,MainActivity context) {
        super(serverUri);
        this.mainActivity = context;
    }

    public static String convertToHourString(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        if(hours!=0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private void sendHeartBeat(){
        Log.i("Websocket","Sending hearthbeat");
        this.send("{\n" +
                "  \"messageType\" : \"HEART_BEAT\"\n" +
                "}");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("Websocket", "Opened connection to server");
        this.send("{\n" +
                "  \"messageType\": \"START\",\n" +
                "  \"memberType\" : \"CONTROLLER\",\n" +
                "  \"deviceName\" : \"my-phone\"\n" +
                "}\n");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartBeat();
            }
        },30000,30000);
    }

    @Override
    public void onMessage(String message) {
        Log.i("Weboscket", "Message: " + message);
        mainActivity.runOnUiThread(() ->{
            try {
//                ControlTimeMessage controlTimeMessage = new Gson().fromJson(message, ControlTimeMessage.class);
//                TextView song = mainActivity.findViewById(R.id.song);
//                TextView time = mainActivity.findViewById(R.id.time);
//                if (controlTimeMessage.getMessageType().equals(MessageType.CONTROLS_TIME)) {
//                    time.setText(convertToHourString(controlTimeMessage.getContent().getTime()) + "/" + convertToHourString(controlTimeMessage.getContent().getMaxTime()));
//                } else if (controlTimeMessage.getMessageType().equals(MessageType.CONTROLS_SONG)){
//                    ControlSongMessage controlSongMessage = new Gson().fromJson(message,ControlSongMessage.class);
//                    song.setText(controlSongMessage.getContent().getTitle());
//                }
            } catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
