package com.example.project2_mediaplayer.fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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
        mView = inflater.inflate(R.layout.fragment_listmusic, container, false);

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


    private List<Music> getListMusic() {


      DatabaseReference database = FirebaseDatabase.getInstance().getReference("music/songs");


    //   DatabaseReference database = FirebaseDatabase.getInstance().getReference("music/song");

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
//                Music tmp=snapshot.getValue(Music.class);
//                if(tmp ==null||list==null||list.isEmpty()){
//                    return;
//                }
//                for (int i=0;i<list.size();i++){
//                    //cai nay phai can id
//                    if(tmp.getSongLink()==list.get(i).getSongLink()){
//                        list.set(i,tmp);
//                    }
//                }
//                musicAdapter.notifyDataSetChanged();
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

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("music/songs");
//        Query query = ref.orderByChild("authorName").equalTo("Hoàng Dũng");
//
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Handle the retrieved data here
//                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                    Music music = childSnapshot.getValue(Music.class);
//                    if (music != null) {
//                        list.add(music);
//                    }
//                    musicAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle the error
//                Log.e("loaddata", "Error retrieving data", databaseError.toException());
//            }
//        };
//        query.addListenerForSingleValueEvent(eventListener);


    }

}
