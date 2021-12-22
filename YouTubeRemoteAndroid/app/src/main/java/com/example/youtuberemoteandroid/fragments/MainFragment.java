package com.example.youtuberemoteandroid.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.youtuberemoteandroid.MainActivity;
import com.example.youtuberemoteandroid.R;
import com.example.youtuberemoteandroid.messages.server.ReceiversMessage;

import java.util.List;
import java.util.stream.Collectors;

public class MainFragment extends MainActivityFragment {

    private View view;
    private List<ReceiversMessage.Receiver> receivers;

    public MainFragment(){
        super(R.layout.home_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        view.findViewById(R.id.search_button).setOnClickListener(l ->{
            if(mainActivity!=null) {
                mainActivity.getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, SearchFragment.class, null)
                        .commit();
            }
        });
        updateSpinner();
    }

    public void setReceivers(List<ReceiversMessage.Receiver> receivers) {
        this.receivers = receivers;
        updateSpinner();

    }

    private void updateSpinner() {
        if (view == null || receivers==null) {
            return;
        }
        Spinner spinner = view.findViewById(R.id.device_spinner);
        spinner.setAdapter(new ArrayAdapter<>(mainActivity, R.layout.spinner_item, receivers.stream()
                .map(ReceiversMessage.Receiver::getDeviceName).collect(Collectors.toList())));
    }

}
