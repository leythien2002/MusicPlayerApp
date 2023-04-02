package com.example.project2_mediaplayer;

import static com.example.project2_mediaplayer.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            Music music= (Music) bundle.get("song");
            if(music!=null){
                startMusic(music);
                sendNotification(music);
            }

        }

        return START_NOT_STICKY;
    }

    private void startMusic(Music music) {
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(getApplicationContext(),music.getResource());
        }
        mediaPlayer.start();

    }

    private void sendNotification(Music music) {
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //set layout for nofitication
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),music.getMusicId());
        RemoteViews remoteView=new RemoteViews(getPackageName(),R.layout.general_notification);
        remoteView.setTextViewText(R.id.title,music.getMusicName());
        remoteView.setTextViewText(R.id.author,music.getMusicAuthor());
        remoteView.setImageViewBitmap(R.id.imgMusic,bitmap);
        //check status music
        remoteView.setImageViewResource(R.id.action_play_pause,R.drawable.pause);

        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.author)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteView)

                .build();
        startForeground(1,notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
