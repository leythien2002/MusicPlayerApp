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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListmusicFragment extends Fragment {
    private View mView;
    private RecyclerView rcvMusic;
    private MusicAdapter musicAdapter;
    private ArrayList<Music> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_listmusic,container,false);

        list = new ArrayList<>();
        getListMusic();
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

//        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu",R.raw.music1));
//        list.add(new Music(R.drawable.music2,"Bone","Imagine Dragons",R.raw.music2));
//        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu2",R.raw.music1));
//        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu3",R.raw.music2));
//        list.add(new Music(R.drawable.music1,"Anh nho ra","Vu4",R.raw.music2));
        DatabaseReference database= FirebaseDatabase.getInstance().getReference("music/song");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music=snapshot.getValue(Music.class);
                if(music!=null){
                    list.add(music);
                }
                musicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        });

    }

}
