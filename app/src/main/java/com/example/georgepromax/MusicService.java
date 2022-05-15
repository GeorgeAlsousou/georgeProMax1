package com.example.georgepromax;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer; // נגן המוזיקה

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//מקשרת בין האפליקציה לבין הסרביס
        return null;
    }

    @Override
    public void onCreate() { //מה קורה כשיוצרים את האובייקט של סרביס
        mediaPlayer= MediaPlayer.create(this, R.raw.music_for_game00);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // מה קורה כשמפעילים את הסרביס
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() { // מה קורה כשמכבים את הסרביס
        mediaPlayer.stop();
        super.onDestroy();
    }
}