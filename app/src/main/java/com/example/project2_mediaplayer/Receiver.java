package com.example.project2_mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Receiver extends BroadcastReceiver {
    //receiver dung de giao tiep giua forceground va notification

    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic=intent.getIntExtra("action_music",0);

        Music song= (Music) intent.getExtras().get("song");
        int index= (int) intent.getExtras().get("index");
        int size= (int) intent.getExtras().get("sizeList");
        Intent iService=new Intent(context,MyService.class);
        iService.putExtra("action_music_service",actionMusic);
        iService.putExtra("song",song);
        iService.putExtra("index",index);
        iService.putExtra("sizeList",size);
        context.startService(iService);
    }
}
