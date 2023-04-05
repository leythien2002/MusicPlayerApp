package com.example.project2_mediaplayer;

import static com.example.project2_mediaplayer.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MyService extends Service {
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_NEXT=5;
    public static final int ACTION_PREV=6;


    private Context context;

    private Music music;
    private boolean isPlaying;
    private int index,size;


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
        size= (int) intent.getExtras().get("sizeList");
        int action = intent.getIntExtra("action_music_service", 0);
        if (bundle != null) {
            music = (Music) bundle.get("song");
            if (music != null) {
//                startMusic(music);
                if(action!=ACTION_CLEAR){
                    index= (int) bundle.get("index");
                    sendNotification(music);
                }

            }
        }
        handleActionMusic(action);

        return START_NOT_STICKY;
    }

    //    private void startMusic(Music music) {
//        if (mediaPlayer == null) {
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), music.getResource());
//        }
//        mediaPlayer.start();
//        isPlaying=true;
//
//    }
    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();//????
                sendActionToActivity(ACTION_CLEAR);
                break;
            case ACTION_NEXT:
                isPlaying=false;
                sendActionToActivity(ACTION_NEXT);
                sendNotification(music);
                break;
            case ACTION_PREV:
                isPlaying=false;
                sendActionToActivity(ACTION_PREV);
                sendNotification(music);
                break;
        }
    }

    private void pauseMusic() {
//        if(mediaPlayer!=null&&isPlaying){
        if (isPlaying) {
//            mediaPlayer.pause();
            isPlaying = false;
            sendActionToActivity(ACTION_PAUSE);
            sendNotification(music);
        }
    }

    private void resumeMusic() {
//        if(mediaPlayer!=null&&isPlaying){
        if (!isPlaying) {
//            mediaPlayer.start();
            isPlaying = true;
            sendActionToActivity(ACTION_RESUME);
            sendNotification(music);
        }
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent i = new Intent(this, Receiver.class);
        if(action==ACTION_NEXT&&index<size){
            index++;
        }
        if(action==ACTION_PREV&&index>0){
            index--;
        }
        i.putExtra("action_music", action);
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", music);
        bundle.putInt("index",index);
        bundle.putInt("sizeList",size);
        i.putExtras(bundle);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void sendNotification(Music music) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.que);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
        //set action pause and resume
        NotificationCompat.Action action2;
        if (isPlaying) {
            action2 = new NotificationCompat.Action.Builder(R.drawable.pause, "Pause", getPendingIntent(this, ACTION_PAUSE)).build();
        } else {
            action2 = new NotificationCompat.Action.Builder(R.drawable.ic_play_white, "Resume", getPendingIntent(this, ACTION_RESUME)).build();
        }
        context = this;
        Picasso.with(this).load(music.getSongimage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_music)
                        .setSubText("MediaApp")
                        .setContentTitle(music.getSongTitle())
                        .setContentText(music.getAuthorName())
                        .setLargeIcon(bitmap)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        // Add media control buttons that invoke intents in your media service
                        .addAction(R.drawable.ic_skip_previous_white_24dp, "Previous", getPendingIntent(context, ACTION_PREV)) // #0
                        .addAction(action2)  // #1
                        .addAction(R.drawable.ic_skip_next_white_24dp, "Next", getPendingIntent(context, ACTION_NEXT))     // #2
                        //swipe to clear notification event
                        .setDeleteIntent(getPendingIntent(context, ACTION_CLEAR))
                        // Apply the media style template
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(1 /* #1: pause button */)
                                .setMediaSession(mediaSessionCompat.getSessionToken()));
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                }
                notificationManager.notify(1, builder.build());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_music)
//                .setSubText("MediaApp")
//                .setContentTitle(music.getSongTitle())
//                .setContentText(music.getAuthorName())
//                .setLargeIcon(bitmap)
//                // Show controls on lock screen even when user hides sensitive content.
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                // Add media control buttons that invoke intents in your media service
//                .addAction(R.drawable.ic_skip_previous_white_24dp, "Previous", null) // #0
//                .addAction(action2)  // #1
//                .addAction(R.drawable.ic_skip_next_white_24dp, "Next", null)     // #2
//                //swipe to clear notification event
//                .setDeleteIntent(getPendingIntent(this,ACTION_CLEAR))
//                // Apply the media style template
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mediaSessionCompat.getSessionToken()))
//                .build();
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        //check permission
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // ??
//        }
//        managerCompat.notify(1, notification); //id 1 la nó sẽ đè lên nhau, muốn nhiều notification thì đổi cách khác.

    }

    private void sendActionToActivity(int action){
        Intent i=new Intent("send_data");
        Bundle bundle=new Bundle();
        bundle.putInt("action",action);
        bundle.putInt("index",index);
        bundle.putBoolean("checkPlaying",isPlaying);
        i.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mediaPlayer!=null){
//            mediaPlayer.release();
//            mediaPlayer=null;
//        }
        sendActionToActivity(ACTION_CLEAR);
    }
}
