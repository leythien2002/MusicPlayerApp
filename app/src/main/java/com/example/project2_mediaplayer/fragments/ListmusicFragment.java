package com.example.project2_mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2_mediaplayer.Music;
import com.example.project2_mediaplayer.MusicAdapter;
import com.example.project2_mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

public class ListmusicFragment extends Fragment {
    private View mView;
    private RecyclerView rcvMusic;
    private MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_listmusic,container,false);

        rcvMusic = mView.findViewById(R.id.rcv_music);

        musicAdapter =new MusicAdapter(getContext(),getListMusic());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvMusic.setLayoutManager(layoutManager);

        musicAdapter.setData(getListMusic());
        rcvMusic.setAdapter(musicAdapter);


        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Music> getListMusic() {
        List<Music> list = new ArrayList<>();
        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu",R.raw.music1));
        list.add(new Music(R.drawable.music2,"Bone","Imagine Dragons",R.raw.music2));
        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu2",R.raw.music1));
        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu3",R.raw.music2));
        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu4",R.raw.music2));
        return list;
    }
}
