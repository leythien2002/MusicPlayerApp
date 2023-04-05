package com.example.project2_mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2_mediaplayer.Author;
import com.example.project2_mediaplayer.AuthorAdapter;
import com.example.project2_mediaplayer.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AuthorFragment extends Fragment {
    private View mView;
    RecyclerView rcvAuthor;
    private AuthorAdapter authorAdapter;
    private List<Author> lstAuthor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_author,container,false);

        rcvAuthor = mView.findViewById(R.id.rcv_author);

        authorAdapter =new AuthorAdapter();
        lstAuthor = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvAuthor.setLayoutManager(layoutManager);

        authorAdapter.setData(getListAuthor());
        rcvAuthor.setAdapter(authorAdapter);
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Author> getListAuthor() {
        DatabaseReference database= FirebaseDatabase.getInstance().getReference("music/author");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Author author = snapshot.getValue(Author.class);
                if(author!=null){
                    lstAuthor.add(author);
                }
                authorAdapter.notifyDataSetChanged();
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

        return lstAuthor;
    }
}
