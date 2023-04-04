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
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlaying extends AppCompatActivity {
    private ImageButton btnPlay,btnPause;
    private ImageButton btnNext,btnPrevious,btnRepeat,btnRandom;
    private SeekBar seekBar;
    private CircleImageView circleImageView;
    private TextView tvMusicName,tvAuthor,tvTimeTotal,tvTimeRun;
    private MediaPlayer mediaPlayer;
    private Animation animation;
    private Music music;
    private boolean isPlaying;
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
//        btnPause=findViewById(R.id.btnPause);
        tvMusicName=findViewById(R.id.textViewtenbaihatplaynhac);
        tvAuthor=findViewById(R.id.textViewtencasiplaynhac);
        tvTimeRun=findViewById(R.id.textViewruntime);
        tvTimeTotal=findViewById(R.id.textViewtimetotal);
        seekBar=findViewById(R.id.seekBartime);
        circleImageView=findViewById(R.id.circle_MusicImage);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            return;
        }
        music= (Music) bundle.get("MusicObject");
        tvMusicName.setText(music.getMusicName());
        tvAuthor.setText(music.getMusicAuthor());
        //set Image for ImageView (disk play)
        Bitmap img= BitmapFactory.decodeResource(getResources(),music.getMusicId());
//        circleImageView.setImageResource(music.getResource());
        circleImageView.setImageBitmap(img);
        //control music
        animation= AnimationUtils.loadAnimation(this,R.anim.rotation);
        mediaPlayer=MediaPlayer.create(this,music.getResource());
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        String duration= millisecondsToString(mediaPlayer.getDuration());
        tvTimeTotal.setText(duration);
        seekBar.setMax(mediaPlayer.getDuration());

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
                    handleAction(MyService.ACTION_PAUSE);
                }
                else{
                    sendActionToService(MyService.ACTION_RESUME);
                    handleAction(MyService.ACTION_RESUME);
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
        new Thread(new Runnable() {
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
        }).start();
    }

//    private void playMusic(){
//        if(mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
//            btnPlay.setImageResource(R.drawable.ic_play_white);
//            circleImageView.clearAnimation();
//
//        }else{
//            mediaPlayer.start();
//            btnPlay.setImageResource(R.drawable.pause);
//            circleImageView.startAnimation(animation);
//        }
//    }
//    private void clickStartService() {
//        Intent i=new Intent(this,MyService.class);
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("song",music);
//        i.putExtras(bundle);
//        startService(i);
//
//    }

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
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer=null;

                }
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
                Intent i =new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case MyService.ACTION_RESUME:
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                circleImageView.startAnimation(animation);

                isPlaying=true;
                break;
        }
    }
    private void sendActionToService(int action){
        Intent i=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song",music);
        i.putExtras(bundle);
        i.putExtra("action_music_service",action);//cai nay lien quan toi receiver .
        startService(i);
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