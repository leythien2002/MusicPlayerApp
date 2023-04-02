package com.example.project2_mediaplayer;

import static com.example.project2_mediaplayer.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private Music music;

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
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            music = (Music) bundle.get("song");
            if (music != null) {
                startMusic(music);
                sendNotification(music);
            }

        }

        return START_NOT_STICKY;
    }

    private void startMusic(Music music) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), music.getResource());
        }
        mediaPlayer.start();

    }

    private void sendNotification(Music music) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set layout for nofitication
//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),music.getMusicId());
//        RemoteViews remoteView=new RemoteViews(getPackageName(),R.layout.general_notification);
//        remoteView.setTextViewText(R.id.title,music.getMusicName());
//        remoteView.setTextViewText(R.id.author,music.getMusicAuthor());
//        remoteView.setImageViewBitmap(R.id.imgMusic,bitmap);
//        //check status music
//        remoteView.setImageViewResource(R.id.action_play_pause,R.drawable.pause);
//
//        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setSmallIcon(R.drawable.author)
//                .setContentIntent(pendingIntent)
//                .setCustomContentView(remoteView)
//
//                .build();
//        startForeground(1,notification);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.que);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setSubText("MediaApp")
                .setContentTitle(music.getMusicName())
                .setContentText(music.getMusicAuthor())
                .setLargeIcon(bitmap)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                // Add media control buttons that invoke intents in your media service
                .addAction(R.drawable.ic_skip_previous_white_24dp, "Previous", null) // #0
                .addAction(R.drawable.pause, "Pause", null)  // #1
                .addAction(R.drawable.ic_skip_next_white_24dp, "Next", null)     // #2
                // Apply the media style template
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        //check permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification); //id 1 la nó sẽ đè lên nhau, muốn nhiều notification thì đổi cách khác.

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
