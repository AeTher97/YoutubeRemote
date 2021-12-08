package com.example.youtuberemoteandroid;

import android.util.Log;

import com.example.youtuberemoteandroid.enums.MessageType;
import com.example.youtuberemoteandroid.messages.client.ClientMessage;
import com.example.youtuberemoteandroid.messages.server.ConstrolsDetailsMessage;
import com.example.youtuberemoteandroid.messages.server.ControlSongMessage;
import com.example.youtuberemoteandroid.messages.server.ControlTimeMessage;
import com.example.youtuberemoteandroid.messages.server.QueueLenghtMessage;
import com.example.youtuberemoteandroid.messages.server.QueueMessage;
import com.example.youtuberemoteandroid.messages.server.TypeMessage;
import com.example.youtuberemoteandroid.utils.FetchImage;
import com.example.youtuberemoteandroid.utils.ImageViewApplet;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import lombok.Getter;

public class MediaWebSocketClient extends WebSocketClient {

    private final MainActivity mainActivity;
    private final Timer timer = new Timer();

    @Getter
    private ControlTimeMessage currentTimeState;

    public MediaWebSocketClient(URI serverUri, MainActivity context) {
        super(serverUri);
        this.mainActivity = context;
    }

    @Override
    public void send(String text) {
        if (!isOpen()) {
            return;
        }
        super.send(text);
    }


    public static String convertToHourString(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        if (hours != 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%2d:%02d", minutes, seconds);
        }
    }

    private void sendHeartBeat() {
        Log.i("Websocket", "Sending hearthbeat");
        this.send(ClientMessage.heartBeatMessage());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("Websocket", "Opened connection to server");
        this.send(ClientMessage.startMessage(UUID.randomUUID().toString()));
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartBeat();
            }
        }, 30000, 30000);
    }

    @Override
    public void onMessage(String message) {
        Log.i("Weboscket", "Message: " + message);
        try {
            TypeMessage typeMessage = new Gson().fromJson(message, TypeMessage.class);
            if (typeMessage.getMessageType() == null) {
                return;
            }
            if (typeMessage.getMessageType().equals(MessageType.CONTROLS_TIME)) {
                ControlTimeMessage controlTimeMessage = new Gson().fromJson(message, ControlTimeMessage.class);
                currentTimeState = controlTimeMessage;
                mainActivity.runOnUiThread(() -> {
                    mainActivity.setTimeInfo(controlTimeMessage.getContent());
                });
            } else if (typeMessage.getMessageType().equals(MessageType.CONTROLS_SONG)) {
                ControlSongMessage controlSongMessage = new Gson().fromJson(message, ControlSongMessage.class);
                new Thread(new FetchImage(controlSongMessage.getContent().getImgSrcLarge(), (image) -> {
                    int[] colors = ImageViewApplet.getColorsFromImage(image);
                    mainActivity.runOnUiThread(() -> {
                        mainActivity.setColors(colors[0], colors[1]);
                        mainActivity.setSongInfo(controlSongMessage.getContent());
                        mainActivity.setImage(image);
                    });
                })).start();
            } else if (typeMessage.getMessageType().equals(MessageType.CONTROLS_DETAILS)) {
                ConstrolsDetailsMessage constrolsDetailsMessage = new Gson().fromJson(message, ConstrolsDetailsMessage.class);
                mainActivity.runOnUiThread(() -> {
                    mainActivity.setDetailedInfo(constrolsDetailsMessage.getContent());
                });
            } else if (typeMessage.getMessageType().equals(MessageType.QUEUE)) {
                QueueMessage queueMessage = new Gson().fromJson(message, QueueMessage.class);
                mainActivity.runOnUiThread(() -> {
                    mainActivity.setQueue(queueMessage.getContent());
                });
            } else if (typeMessage.getMessageType().equals(MessageType.QUEUE_LENGTH)) {
                QueueLenghtMessage queueMessage = new Gson().fromJson(message, QueueLenghtMessage.class);
                if (queueMessage.getContent() != 0) {
                    mainActivity.runOnUiThread(() -> {
                        mainActivity.setQueueLength(queueMessage.getContent());
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("Websocket","Closed scheduling reconnection");
        mainActivity.connectWithReconnection();
        timer.cancel();
    }

    @Override
    public void onError(Exception ex) {
        Log.e("Websocket", "failed to connect");
        ex.printStackTrace();
    }
}
