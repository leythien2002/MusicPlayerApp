package com.example.project2_mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class Receiver extends BroadcastReceiver {
    //receiver dung de giao tiep giua forceground va notification

    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic=intent.getIntExtra("action_music",0);
         //using for change infor on notification
        Music song= (Music) intent.getExtras().get("object_music");
        int index= (int) intent.getExtras().get("index");
        int size= MusicAdapter.mListmusic.size();
        boolean isFav= (boolean) intent.getExtras().get("Favorite");
        boolean isPlaying= (boolean) intent.getExtras().get("checkPlay");
        Intent iService=new Intent(context,MyService.class);
        iService.putExtra("action_music_service",actionMusic);
        iService.putExtra("object_music",song);
        iService.putExtra("Favorite",isFav);
        iService.putExtra("checkPlay",isPlaying);
        iService.putExtra("index",index);
        iService.putExtra("sizeList",size);
        context.startService(iService);
    }
}
