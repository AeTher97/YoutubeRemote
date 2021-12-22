package com.example.youtuberemoteandroid.adapters;

import com.example.youtuberemoteandroid.enums.SearchType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRecyclerItem {

    private Type type;
    private String name;
    private SearchType searchType;
    private int sectionIndex;
    private int itemIndex;

    public enum Type {
        HEADER,ENTRY,MORE
    }
}
