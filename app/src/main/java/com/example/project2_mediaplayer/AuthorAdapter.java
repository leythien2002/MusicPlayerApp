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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    private List<Author> mListAuthor;
    private Context context;

    public AuthorAdapter(List<Author> lst, Context context){
        this.mListAuthor = lst;
        this.context = context;
    }

    public AuthorAdapter() {
    }

    public void setData(List<Author> list) {
        this.mListAuthor = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = mListAuthor.get(position);
        if (author == null) {
            return;
        }
        AuthorViewHolder authorViewHolder = (AuthorViewHolder) holder;
        Picasso.with(context).load(author.getAuthorimage()).into(holder.imgAuthorImage);
//        musicViewHolder.imgAuthorImage.setImageResource(author.getResourceId());
        authorViewHolder.tvAuthorName.setText(author.getAuthorname());

        authorViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListMusicByAuthor.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("AuthorObject", author);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListAuthor != null) {
            return mListAuthor.size();
        }
        return 0;
    }


    class AuthorViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        private TextView tvAuthorName;
        private ImageView imgAuthorImage;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_author_layout);
            tvAuthorName = itemView.findViewById(R.id.author_name);
            imgAuthorImage = itemView.findViewById(R.id.author_img);
        }
    }
}
