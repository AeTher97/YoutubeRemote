package com.example.youtuberemoteandroid.fragments;

import androidx.fragment.app.Fragment;

import com.example.youtuberemoteandroid.MainActivity;

public class MainActivityFragment extends Fragment {

    protected MainActivity mainActivity;

    public MainActivityFragment(int layout){
        super(layout);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
