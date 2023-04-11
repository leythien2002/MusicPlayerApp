package com.example.project2_mediaplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListMusicByAuthor extends AppCompatActivity {
    private RecyclerView rcvMusic;
    private MusicAdapter musicAdapter;

    private ArrayList<Music> list;
    private TextView author_name;
    private Author author_selected;
    private ImageView img_author;
//    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle=intent.getExtras();
//            if(bundle==null){
//                return;
//            }
//            else{
//                Author action= (Author) bundle.get("AuthorObject");
//            }
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_music_byauthor);
        img_author = findViewById(R.id.img_author);
        author_name = findViewById(R.id.author_name_title);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
            if(bundle==null){
                System.out.println("bundle null");
                return;
            }
            else{
                author_selected= (Author) bundle.get("AuthorObject");
                String name_author_selected = author_selected.getAuthorname();
                author_name.setText(name_author_selected);
                Picasso.with(this).load(author_selected.getAuthorimage()).into(this.img_author);

            }
        list = new ArrayList<>();

        getListMusic();
        rcvMusic = findViewById(R.id.rcv_musicbyauthor);
        musicAdapter =new MusicAdapter(this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcvMusic.setLayoutManager(layoutManager);


        musicAdapter.setData(list);

        rcvMusic.setAdapter(musicAdapter);

        }

    private List<Music> getListMusic() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("music/song");
        Query query = ref.orderByChild("authorName").equalTo(author_selected.getAuthorname());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Handle the retrieved data here
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Music music = childSnapshot.getValue(Music.class);
                    if (music != null) {
                        list.add(music);
                        System.out.println(music.getSongTitle());
                    }
                    musicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                Log.e("loaddata", "Error retrieving data", databaseError.toException());
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
        return list;

    }

}