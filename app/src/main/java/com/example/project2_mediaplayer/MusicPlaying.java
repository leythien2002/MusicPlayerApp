package com.example.project2_mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MusicPlaying extends AppCompatActivity {
    private Button btnPlay,btnPause;
    private TextView tvMusicName;
    private Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing);

        initUi();
        initListener();
    }

    private void initUi() {
        btnPlay=findViewById(R.id.btnPlay);
        btnPause=findViewById(R.id.btnPause);
        tvMusicName=findViewById(R.id.tvMusicName);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            return;
        }
        music= (Music) bundle.get("MusicObject");
        tvMusicName.setText(music.getMusicName());

    }
    private void initListener(){
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStopService();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartService();
            }
        });
    }

    private void clickStartService() {
        Intent i=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song",music);
        i.putExtras(bundle);
        startService(i);
    }

    private void clickStopService() {
    }
}