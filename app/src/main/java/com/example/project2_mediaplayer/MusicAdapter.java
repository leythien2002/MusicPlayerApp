package com.example.project2_mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private Context context;
    static public ArrayList<Music> mListmusic;

    private int favorite;
    private DatabaseReference mDatabase;



    public MusicAdapter(Context context, ArrayList<Music> mListmusic) {
        this.context = context;
        this.mListmusic = mListmusic;
    }
    public MusicAdapter(){}

    public  void setData(ArrayList<Music> list){
        this.mListmusic =list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_song,parent,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return new MusicViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = mListmusic.get(position);
        if(music == null){
            return;
        }
        Picasso.with(context).load(music.getSongimage()).into(holder.imgMusicImage);

        holder.tvMusicAuthor.setText(music.getAuthorName());
        holder.tvMusicName.setText(music.getSongTitle());
        if (music.getFavorite()==0){
            holder.imgFavorite.setImageResource(R.drawable.no_favorite);
        }else {
            holder.imgFavorite.setImageResource(R.drawable.favorite);
        }


        int index=holder.getAdapterPosition();

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyService.mediaPlayer != null){
                    MyService.mediaPlayer .release();
                    MyService.mediaPlayer  = null;
                }
                startPlaying(music,index);

            }

        });
        holder.constraintLayout.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (music.getFavorite()==0){
                    holder.imgFavorite.setImageResource(R.drawable.favorite);
                    wirteFavorite(music, music.getFavorite());
                }
                else {
                    holder.imgFavorite.setImageResource(R.drawable.no_favorite);
                    wirteFavorite(music, music.getFavorite());
                }
            }
        });
    }

    public void wirteFavorite(Music music, int fav) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music/song");
        if (fav ==0){
//            mDatabase.child("music/song").child(music.getSongTitle()).child("favorite").setValue("1");
            music.setFavorite(1);
        }
        else {
            music.setFavorite(0);
//            mDatabase.child("music/song").child(music.getSongTitle()).child("favorite").setValue("0");
        }
        myRef.child(String.valueOf(music.getSongID())).updateChildren(music.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Update favorite",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void startPlaying(Music music, int index) {
        Intent i=new Intent(context,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_music",music);
        bundle.putInt("index",index);
        bundle.putBoolean("Favorite",false);


        i.putExtras(bundle);
        context.startService(i);

        Intent i1=new Intent(context,MusicPlaying.class);
        Bundle bundle2=new Bundle();

        bundle2.putInt("index",index);
        bundle2.putSerializable("object_music",music);
        bundle2.putBoolean("isPlaying",true);
        i1.putExtras(bundle2);
        context.startActivity(i1);
        MainActivity.layoutMusic.setVisibility(View.VISIBLE);

    }



    @Override
    public int getItemCount() {
        if (mListmusic!=null){
            return mListmusic.size();
        }
        return 0;
    }



    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout constraintLayout;
        private TextView tvMusicName;
        private TextView tvMusicAuthor;
        private ImageView imgMusicImage;
        private ImageView imgFavorite;

            public MusicViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout=itemView.findViewById(R.id.item_layout);
                tvMusicAuthor = itemView.findViewById(R.id.music_author);
                tvMusicName = itemView.findViewById(R.id.music_name);
                imgMusicImage = itemView.findViewById(R.id.music_image);
                imgFavorite = itemView.findViewById(R.id.favorite);
            }
    }
}
