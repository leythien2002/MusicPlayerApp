package com.example.project2_mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private Context context;

    private List<Music> mListmusic;


    public MusicAdapter(Context context, List<Music> mListmusic) {
        this.context = context;
        this.mListmusic = mListmusic;
    }

    public  void setData(List<Music> list){
        this.mListmusic =list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_song,parent,false);
        return new MusicViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = mListmusic.get(position);
        if(music == null){
            return;
        }
        Picasso.with(context).load(music.getSongimage()).into(holder.imgMusicImage);
//        holder.imgMusicImage.setImageResource(music.getSongimage());
        holder.tvMusicAuthor.setText(music.getAuthorName());
        holder.tvMusicName.setText(music.getSongTitle());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying(music);
            }
        });



    }
    private void startPlaying(Music music) {
        Intent i=new Intent(context,MusicPlaying.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("MusicObject",music);
        i.putExtras(bundle);
        context.startActivity(i);
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

            public MusicViewHolder(@NonNull View itemView) {
                super(itemView);

                constraintLayout=itemView.findViewById(R.id.item_layout);
                tvMusicAuthor = itemView.findViewById(R.id.music_author);
                tvMusicName = itemView.findViewById(R.id.music_name);
                imgMusicImage = itemView.findViewById(R.id.music_image);


            }
    }
}
