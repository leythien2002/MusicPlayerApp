package com.example.project2_mediaplayer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2_mediaplayer.FavoriteAdapter;
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

public class SettingFragment extends Fragment {
    private View mView;
    private RecyclerView rcvMusic;
    private FavoriteAdapter favoriteAdapter;

    private ArrayList<Music> list;
    private int count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);


        list = new ArrayList<>();
        getListMusic();
        rcvMusic = mView.findViewById(R.id.rcv_music);

        favoriteAdapter = new FavoriteAdapter(getContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvMusic.setLayoutManager(layoutManager);
        favoriteAdapter.setData(list);
        rcvMusic.setAdapter(favoriteAdapter);
        return mView;
    }

    private void getListMusic() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("music/song");

        Query query = ref.orderByChild("favorite").equalTo(1);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    count++;
                    Music music = snapshot.getValue(Music.class);
                    if (music != null) {
                        list.add(music);
                    }
                    favoriteAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if (music == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (music.getSongID().equals(list.get(i).getSongID())) {
                        list.set(i, music);
                        break;
                    }
                }
                favoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Music music = snapshot.getValue(Music.class);
                if (music == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (music.getSongID().equals(list.get(i).getSongID())) {
                        list.remove(list.get(i));
                        break;
                    }
                }
                favoriteAdapter.notifyDataSetChanged();
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
