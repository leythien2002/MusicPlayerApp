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
        Intent iService=new Intent(context,MyService.class);
        iService.putExtra("action_music_service",actionMusic);
        iService.putExtra("song",song);
        context.startService(iService);
    }
}
