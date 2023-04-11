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
    static public MediaPlayer mediaPlayer;
    private Animation animation;
    private Music music;
    private boolean isPlaying,isLoop,isRandom;
    //prev and next btn
    private ArrayList<Music> listSong;
    private int currentIndex;
    private Thread thread;
    private Runnable runnable;
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
                int action= (int) bundle.get("action");
                currentIndex= (int) bundle.get("index");
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
        currentIndex= (int) bundle.get("index");
        listSong=(ArrayList) bundle.getParcelableArrayList("ListSong");

        music=listSong.get(currentIndex);
        mediaPlayer=MediaPlayer.create(this,Uri.parse(music.getSongLink()));
        handleAction(MyService.ACTION_RESUME);
        sendActionToService(MyService.ACTION_RESUME);

        mediaPlayer.seekTo(0);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(isRandom){
                    //neu size la 50 thi no se random tu 0-49
                    currentIndex=rand.nextInt(listSong.size());
                    music=listSong.get(currentIndex);
                    sendActionToService(MyService.ACTION_NEXT);
                }
                else{
                    if(currentIndex<listSong.size()-1){
                        currentIndex++;
                        music=listSong.get(currentIndex);
                        sendActionToService(MyService.ACTION_NEXT);
                    }
                    else{
                        sendActionToService(MyService.ACTION_PAUSE);
                    }
                }

            }
        });
        String duration= millisecondsToString(mediaPlayer.getDuration());
        tvTimeTotal.setText(duration);
        seekBar.setMax(mediaPlayer.getDuration());


        tvMusicName.setText(music.getSongTitle());
        tvAuthor.setText(music.getAuthorName());
        //set Image for ImageView (disk play)

        Picasso.with(this).load(music.getSongimage()).into(circleImageView);
        //control music
        setRunnableForThread();



    }

    private void initListener(){

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mediaPlayer==null){
//                    clickStartService();
//                    handleAction(MyService.ACTION_RESUME);
//                }
                if(isPlaying){
                    sendActionToService(MyService.ACTION_PAUSE);
//                    handleAction(MyService.ACTION_PAUSE);
                }
                else{
                    sendActionToService(MyService.ACTION_RESUME);
//                    handleAction(MyService.ACTION_RESUME);
                }



            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRandom){
                    //neu size la 50 thi no se random tu 0-49
                    currentIndex=rand.nextInt(listSong.size());
                    music=listSong.get(currentIndex);
                    sendActionToService(MyService.ACTION_NEXT);
                }
                else{
                    if(currentIndex<listSong.size()-1){
                        currentIndex++;
                        music=listSong.get(currentIndex);
                        sendActionToService(MyService.ACTION_NEXT);
                    }
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex>0){
                    currentIndex--;
                }
                music=listSong.get(currentIndex);
                sendActionToService(MyService.ACTION_PREV);
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLoop){
                    mediaPlayer.setLooping(false);
                    isLoop=false;
                }
                else{
                    mediaPlayer.setLooping(true);
                    isLoop=true;
                }

            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRandom){
                    isRandom=false;
                }
                else{
                    isRandom=true;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser){
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
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
        setRunnableForThread();


    }


    private void clickStopService() {
    }

    private void handleAction(int action) {
        switch (action){
            //nen co 1 case nua de set layout visibility cho layout nho? khi play
            case MyService.ACTION_PAUSE:
                mediaPlayer.pause();
                btnPlay.setImageResource(R.drawable.ic_play_white);
                circleImageView.clearAnimation();
                isPlaying=false;
                break;
            case MyService.ACTION_CLEAR: //cai nut tat
                isPlaying=false;
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
                stopService();
                if(mediaPlayer!=null){
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer=null;
                    thread.interrupt();
                }
                MainActivity.layoutMusic.setVisibility(View.GONE);

                Intent i =new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case MyService.ACTION_RESUME:
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                circleImageView.startAnimation(animation);
                isPlaying=true;
                break;
            case MyService.ACTION_NEXT:
                //chi can interrupt no roi gan runnable moi :v ez
//                thread.interrupt();
                isPlaying=true;
                setCurrentSong();
                setRunnableForThread();
                sendSongToMain(currentIndex,music);

                break;
            case MyService.ACTION_PREV:

//                thread.interrupt();
                isPlaying=true;
                setCurrentSong();
                setRunnableForThread();
                sendSongToMain(currentIndex,music);
                break;



        }
    }
    private void sendActionToService(int action){
        Intent i=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song",music);
        bundle.putInt("index",currentIndex);
        bundle.putBoolean("checkPlay",isPlaying);
        bundle.putInt("sizeList",listSong.size());
        i.putExtras(bundle);
        i.putExtra("action_music_service",action);//cai nay licen quan toi receiver .
        startService(i);
    }
    private void sendSongToMain(int index,Music music){
        Intent i=new Intent("send_song");
        Bundle bundle=new Bundle();
        bundle.putInt("index",index);
        bundle.putSerializable("MusicObject",music);
        i.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
    private void stopService(){
        Intent i=new Intent(this,MyService.class);
        stopService(i);
    }
    private void setCurrentSong(){

        music=listSong.get(currentIndex);
        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        mediaPlayer=MediaPlayer.create(this,Uri.parse(music.getSongLink()));
        String duration= millisecondsToString(mediaPlayer.getDuration());
        tvTimeTotal.setText(duration);

        mediaPlayer.seekTo(0);
        //sua loi E/MediaPlayer: error (-38, 0)
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(isRandom){
                    //neu size la 50 thi no se random tu 0-49
                    currentIndex=rand.nextInt(listSong.size());
                    music=listSong.get(currentIndex);
                    sendActionToService(MyService.ACTION_NEXT);
                }
                else{
                    if(currentIndex<listSong.size()-1){
                        currentIndex++;
                        music=listSong.get(currentIndex);
                        sendActionToService(MyService.ACTION_NEXT);
                    }
                    else{
                        sendActionToService(MyService.ACTION_PAUSE);
                    }
                }
            }
        });

//        String duration= millisecondsToString(mediaPlayer.getDuration());
//        tvTimeTotal.setText(duration);
        seekBar.setMax(mediaPlayer.getDuration());

        tvMusicName.setText(music.getSongTitle());
        tvAuthor.setText(music.getAuthorName());

        Picasso.with(this).load(music.getSongimage()).into(circleImageView);


    }
    private void setRunnableForThread(){
        runnable=new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        try{
                            final double current=mediaPlayer.getCurrentPosition();
                            final String time= millisecondsToString((int)current);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTimeRun.setText(time);
                                    seekBar.setProgress((int)current);
                                }
                            });
                            Thread.sleep(500);

                        }catch (Exception e){

                        }

                    }


                }
            }
        };
        thread=new Thread(runnable);
        thread.start();
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