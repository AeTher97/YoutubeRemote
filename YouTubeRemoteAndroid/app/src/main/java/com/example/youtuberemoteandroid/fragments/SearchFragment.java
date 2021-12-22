package com.example.youtuberemoteandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtuberemoteandroid.R;
import com.example.youtuberemoteandroid.adapters.SearchAdapter;
import com.example.youtuberemoteandroid.adapters.SearchRecyclerItem;
import com.example.youtuberemoteandroid.enums.SearchAction;
import com.example.youtuberemoteandroid.messages.client.ClientMessage;
import com.example.youtuberemoteandroid.messages.server.SearchResultMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends MainActivityFragment {

    private RecyclerView recyclerView;

    public SearchFragment() {
        super(R.layout.search_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.search_list);

        EditText searchBarInput = view.findViewById(R.id.search_bar_input);
        searchBarInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mainActivity.getMediaWebSocketClient().send(ClientMessage.searchControlMessage(v.getText().toString(), SearchAction.SEARCH_PHRASE));
                    handled = true;
                    InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(searchBarInput.getWindowToken(),0);
                }

                return handled;
            }
        });

        view.findViewById(R.id.back_arrow).setOnClickListener(l -> {
            mainActivity.getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, MainFragment.class, null)
                    .commit();
        });

    }



    public void updateSearchResults(List<SearchResultMessage.Section> results){
        List<SearchRecyclerItem> recyclerItems = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));


        AtomicInteger sectionIndex = new AtomicInteger();


        results.forEach(section -> {
            recyclerItems.add(SearchRecyclerItem.builder()
                    .type(SearchRecyclerItem.Type.HEADER)
                    .name(section.getName())
                    .build());
            AtomicInteger itemIndex = new AtomicInteger();

            section.getItems().forEach(searchItem -> {
                recyclerItems.add(SearchRecyclerItem.builder()
                        .type(SearchRecyclerItem.Type.ENTRY)
                        .name(searchItem.getTitle())
                        .sectionIndex(sectionIndex.get())
                        .itemIndex(itemIndex.get())
                        .build());
                itemIndex.getAndIncrement();
            });
            sectionIndex.getAndIncrement();
        });

        recyclerItems.add(SearchRecyclerItem.builder().type(SearchRecyclerItem.Type.HEADER).build());

        recyclerView.setAdapter(new SearchAdapter(recyclerItems,mainActivity));
    }
}
