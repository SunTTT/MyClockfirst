package com.suntt.myclockfirst;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class AlarmSoundActivity extends Activity {
private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound);
        mp = MediaPlayer.create(this,R.raw.beyond);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
        mp.release();
    }
}
