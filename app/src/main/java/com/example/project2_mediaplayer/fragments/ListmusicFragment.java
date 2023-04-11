package com.example.project2_mediaplayer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2_mediaplayer.Music;
import com.example.project2_mediaplayer.MusicAdapter;
import com.example.project2_mediaplayer.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ListmusicFragment extends Fragment {
    private View mView;
    private RecyclerView rcvMusic;
    private MusicAdapter musicAdapter;

    private ArrayList<Music> list;
    private TextView tvNumberSong;
    private  int count;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_listmusic, container, false);

        list = new ArrayList<>();

        getListMusic();
        tvNumberSong = mView.findViewById(R.id.numberSong);


        rcvMusic = mView.findViewById(R.id.rcv_music);

        musicAdapter =new MusicAdapter(getContext(),list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvMusic.setLayoutManager(layoutManager);


        musicAdapter.setData(list);

        rcvMusic.setAdapter(musicAdapter);


        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void getListMusic() {


        DatabaseReference database = FirebaseDatabase.getInstance().getReference("music/song");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    count++;
//                    System.out.println(count);
                    Music music=snapshot.getValue(Music.class);
                    if(music!=null){
                        list.add(music);
                        tvNumberSong.setText(String.valueOf(count));
                    }
                    musicAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music=snapshot.getValue(Music.class);
                if (music == null|| list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if(music.getSongID().equals(list.get(i).getSongID())){
                        list.set(i,music);
                    }
                }
                musicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}

}
