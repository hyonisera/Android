package com.example.mp3player_ex;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mPlayer;
    SeekBar pbMP3;
    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbMP3 = (SeekBar) findViewById(R.id.pbMP3);
//        mPlayer = MediaPlayer.create(this, R.raw.song1);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch1.isChecked() == true) {
                    mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song1);
                    mPlayer.start();
                    makeThread();
                } else
                    mPlayer.stop();
            }
        });

        pbMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void makeThread() {
        new Thread() {
            @Override
            public void run() {
                while (mPlayer.isPlaying()) {
                    pbMP3.setMax(mPlayer.getDuration());
                    pbMP3.setProgress(mPlayer.getCurrentPosition());
                    SystemClock.sleep(100);
                }
                pbMP3.setProgress(0);
            }
        }.start();
    }
}