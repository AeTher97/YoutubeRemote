package com.example.youtuberemoteandroid.messages.client;

import com.example.youtuberemoteandroid.enums.Action;
import com.example.youtuberemoteandroid.enums.MemberType;
import com.example.youtuberemoteandroid.enums.MessageType;
import com.example.youtuberemoteandroid.enums.SearchAction;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClientMessage {

    private static String toString(BasicMessage message) {
        return new Gson().toJson(message);
    }

    public static String heartBeatMessage() {
        return toString(new BasicMessage(MessageType.HEART_BEAT));
    }

    public static String startMessage(String id) {
        return toString(new StartMessage(id));
    }

    public static String mediaControlMessage(Action action, int startTime) {
        return toString(new MediaControl(action, startTime));
    }

    public static String mediaControlMessage(Action action) {
        return toString(new MediaControl(action));
    }

    public static String queueControlMessage(Action action,int targetIndex){
        return toString(new QueueControlMessage(action,targetIndex));
    }

    public static String queueControlMessage(Action action,int targetIndex,int currentIndex){
        return toString(new QueueControlMessage(action,targetIndex,currentIndex));
    }

    public static String searchControlMessage(String phrase,SearchAction searchAction){
        return toString(new SearchControlMessage(phrase,searchAction));
    }

    public static String searchControlMessage(SearchAction searchAction, int section, int element){
        return toString(new SearchControlMessage(searchAction,section,element));
    }


    @AllArgsConstructor
    @Data
    private static class BasicMessage {
        private MessageType messageType;
    }


    @Getter
    private static class StartMessage extends BasicMessage {
        private final MemberType memberType = MemberType.CONTROLLER;
        private String deviceName;

        public StartMessage(String deviceName) {
            super(MessageType.START);
            this.deviceName = deviceName;
        }
    }

    @Getter
    private static class MediaControl extends BasicMessage {

        public MediaControl(Action action, int timeSet) {
            super(MessageType.MEDIA_CONTROL);
            this.action = action;
            this.timeSet = timeSet;
        }

        public MediaControl(Action action) {
            super(MessageType.MEDIA_CONTROL);
            this.action = action;
        }

        private Action action;
        private int timeSet;
    }

    @Getter
    public static class QueueControlMessage extends BasicMessage {
        private Action action;
        private int targetIndex;
        private int currentIndex;

        public QueueControlMessage(Action action, int targetIndex) {
            super(MessageType.QUEUE_CONTROL);
            this.targetIndex = targetIndex;
            this.action = action;
        }

        public QueueControlMessage(Action action, int targetIndex, int currentIndex) {
            super(MessageType.QUEUE_CONTROL);
            this.action = action;
            this.targetIndex = targetIndex;
            this.currentIndex = currentIndex;
        }

    }

    @Getter
    public static class SearchControlMessage extends BasicMessage {

        private String phrase;
        private SearchAction searchAction;
        private int section;
        private int element;

        public SearchControlMessage(String phrase, SearchAction searchAction){
            super(MessageType.SEARCH_CONTROL);
            this.phrase = phrase;
            this.searchAction = searchAction;
        }

        public SearchControlMessage(SearchAction searchAction,int section, int element){
            super(MessageType.SEARCH_CONTROL);
            this.searchAction = searchAction;
            this.section = section;
            this.element = element;
        }
    }

}
