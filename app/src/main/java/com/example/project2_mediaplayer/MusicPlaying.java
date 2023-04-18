package com.example.project2_mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlaying extends AppCompatActivity {
    private ImageButton btnPlay,btnPause;
    private ImageButton btnNext,btnPrevious,btnRepeat,btnRandom;
    private SeekBar seekBar;
    private CircleImageView circleImageView;
    private TextView tvMusicName,tvAuthor,tvTimeTotal,tvTimeRun;
//    static public MediaPlayer mediaPlayer;
    private Animation animation;
    private Music music;
    private boolean isPlaying;
    static boolean isRandom=false;
    static boolean isLoop=false;
    //prev and next btn
    private ArrayList<Music> listSong;
    private int currentIndex;

    private boolean isFav;


    private Random rand=new Random();


    //get Broadcast receiver. Cai nay dung de giao tiep giua activity va forceground
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            if(bundle==null){
                return;
            }
            else{
                music= (Music) bundle.get("object_music");
                currentIndex= (int) bundle.get("index");

                isFav = bundle.getBoolean("Favorite");

                MainActivity.indexMain=currentIndex;
                int action= (int) bundle.get("action");
                isPlaying= (boolean) bundle.get("checkPlaying");
                handleAction(action);
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_music_playing);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("send_data"));
        setContentView(R.layout.music_playing);
        initUi();
        initListener();
    }

    private void initUi() {
        btnPlay=findViewById(R.id.imageButtonplaypause);
        btnNext=findViewById(R.id.imageButtonnext);
        btnPrevious=findViewById(R.id.imageButtonprevious);
        btnRepeat=findViewById(R.id.imageButtonlap);
        btnRandom=findViewById(R.id.imageButtontron);

        tvMusicName=findViewById(R.id.textViewtenbaihatplaynhac);
        tvAuthor=findViewById(R.id.textViewtencasiplaynhac);
        tvTimeRun=findViewById(R.id.textViewruntime);
        tvTimeTotal=findViewById(R.id.textViewtimetotal);
        seekBar=findViewById(R.id.seekBartime);
        circleImageView=findViewById(R.id.circle_MusicImage);
        //animation for circle
        animation= AnimationUtils.loadAnimation(this,R.anim.rotation);
        //get bundle
        //create media
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            return;
        }

        isFav = bundle.getBoolean("Favorite");

        music= (Music) bundle.getSerializable("object_music");
        currentIndex= (int) bundle.get("index");
        MainActivity.indexMain=currentIndex;
        handleAction(MyService.ACTION_RESUME);
        setRunnableMusic();




    }

    private void initListener() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mediaPlayer==null){
//                    clickStartService();
//                    handleAction(MyService.ACTION_RESUME);
//                }
                if (isPlaying) {
                    sendActionToService(MyService.ACTION_PAUSE);
//                    handleAction(MyService.ACTION_PAUSE);
                } else {
                    sendActionToService(MyService.ACTION_RESUME);
//                    handleAction(MyService.ACTION_RESUME);
                }


            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendActionToService(MyService.ACTION_NEXT);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendActionToService(MyService.ACTION_PREV);
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoop) {
                    MyService.mediaPlayer.setLooping(false);
                    isLoop = false;
                } else {
                    MyService.mediaPlayer.setLooping(true);
                    isLoop = true;
                }

            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRandom) {
                    isRandom = false;
                } else {
                    isRandom = true;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    MyService.mediaPlayer.seekTo(progress);
                    seekBar.setProgress((int) MyService.mediaPlayer.getCurrentPosition());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //AsyncTask ?
//        setRunnableForThread();
    }
    private void handleAction(int action) {
        switch (action){
            //nen co 1 case nua de set layout visibility cho layout nho? khi play

            case MyService.ACTION_PAUSE:
                showInfoMusic();
                btnPlay.setImageResource(R.drawable.ic_play);
                circleImageView.clearAnimation();
                isPlaying=false;
                sendSongToMain();
                break;
            case MyService.ACTION_CLEAR: //cai nut tat
                isPlaying=false;
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
                stopService();
                MainActivity.layoutMusic.setVisibility(View.GONE);
                Intent i =new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case MyService.ACTION_RESUME:
                showInfoMusic();
                btnPlay.setImageResource(R.drawable.ic_pause);
                circleImageView.startAnimation(animation);
                isPlaying=true;
                sendSongToMain();
                break;
            case MyService.ACTION_NEXT:
                //chi can interrupt no roi gan runnable moi :v ez
                showInfoMusic();
                sendSongToMain();
                break;
            case MyService.ACTION_PREV:
                showInfoMusic();
                sendSongToMain();
                break;




        }
    }
    private void showInfoMusic(){
        tvMusicName.setText(music.getSongTitle());
        tvAuthor.setText(music.getAuthorName());
//        //set Image for ImageView (disk play)
//
        Picasso.with(this).load(music.getSongimage()).into(circleImageView);
        String duration= millisecondsToString(MyService.mediaPlayer.getDuration());
        tvTimeTotal.setText(duration);
        if(MyService.mediaPlayer!=null){
            seekBar.setProgress(MyService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(MyService.mediaPlayer.getDuration());

        }
        //set durration
        final double current=MyService.mediaPlayer.getCurrentPosition();
        final String time= millisecondsToString((int)current);
        tvTimeRun.setText(time);
        seekBar.setProgress((int)current);


    }
    private void setRunnableMusic(){
        final Handler handler=new Handler();
        MusicPlaying.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(MyService.mediaPlayer!=null){
                    showInfoMusic();
                }
                handler.postDelayed(this, 1000);
            }

        });
    }

    private void sendActionToService(int action){
        Intent i=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();

        bundle.putBoolean("Favorite",isFav);


        bundle.putSerializable("object_music",music);
        bundle.putInt("index",currentIndex);

        bundle.putBoolean("checkPlay",isPlaying);
//        bundle.putInt("sizeList",listSong.size());
        i.putExtras(bundle);
        i.putExtra("action_music_service",action);//cai nay licen quan toi receiver .
        startService(i);
    }
    private void sendSongToMain(){
        Intent i=new Intent("send_signal");
        Bundle bundle=new Bundle();

        bundle.putBoolean("Favorite",isFav);


        bundle.putBoolean("checkChange",true);
        bundle.putBoolean("isPlaying",isPlaying);
        i.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
    private void stopService(){
        Intent i=new Intent(this,MyService.class);
        stopService(i);
    }

    private String millisecondsToString(int time){
        String elapsedTime="";
        int minutes=time/1000/60;
        int seconds=time/1000%60;
        elapsedTime=minutes+":";
        if(seconds<10){
            elapsedTime+="0";
        }
        elapsedTime+=seconds;
        return elapsedTime;
    }
}