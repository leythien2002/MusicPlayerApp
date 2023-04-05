package com.example.project2_mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    private List<Author> mListAuthor;
    private Context context;


    public  void setData(List<Author> list){
        this.mListAuthor =list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_author,parent,false);
        return new AuthorViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = mListAuthor.get(position);
        if(author == null){
            return;
        }
        AuthorViewHolder authorViewHolder = (AuthorViewHolder) holder;
        Picasso.with(context).load(author.getAuthorimage1()).into(holder.imgAuthorImage);
//        musicViewHolder.imgAuthorImage.setImageResource(author.getResourceId());
        authorViewHolder.tvAuthorName.setText(author.getAuthorname());
    }

    @Override
    public int getItemCount() {
        if (mListAuthor!=null){
            return mListAuthor.size();
        }
        return 0;
    }



    class AuthorViewHolder extends RecyclerView.ViewHolder{

    private TextView tvAuthorName;
    private ImageView imgAuthorImage;
        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.author_name);
            imgAuthorImage = itemView.findViewById(R.id.author_img);
        }
    }
}
