package com.example.ojjj.project;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;

public class MusicService extends Service {
    MediaPlayer player;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    public void onCreate(){
    }
    public void onDestroy(){
        if(player!=null)
            player.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int num = intent.getIntExtra("MUSIC",0);
        if(num == 0) {
            player = null;
        }
        else if(num == 1){
            player = MediaPlayer.create(this,R.raw.bad_guy);
            player.setLooping(false);
            player.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
