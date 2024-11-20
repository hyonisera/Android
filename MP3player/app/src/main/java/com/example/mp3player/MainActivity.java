package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listViewMP3;
    Button btnPlay, btnPause, btnStop;
    TextView tvMP3, tvTime;
    ProgressBar pbMP3;

    ArrayList<String> mp3List;
    String selectedMP3, fileName, extName;

    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    MediaPlayer mPlayer;

    int position = 0;
    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 MP3 플레이어");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mp3List = new ArrayList<String>();

        File[] listFiles = new File(mp3Path).listFiles();

        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals((String) "mp3"))
                mp3List.add(fileName);
        }

        init();
        initData();
        initLr();

//        listViewMP3 = (ListView) findViewById(R.id.listViewMP3);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
//        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        listViewMP3.setAdapter(adapter);
//        listViewMP3.setItemChecked(0, true);
//
//        listViewMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                selectedMP3 = mp3List.get(arg2);
//            }
//        });
//
//        selectedMP3 = mp3List.get(0);
//
//        btnPlay = (Button) findViewById(R.id.btnPlay);
//        btnStop = (Button) findViewById(R.id.btnStop);
//        tvMP3 = (TextView) findViewById(R.id.tvMP3);
//        pbMP3 = (ProgressBar) findViewById(R.id.pbMP3);
//
//        btnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    mPlayer = new MediaPlayer();
//                    mPlayer.setDataSource(mp3Path + selectedMP3);
//                    mPlayer.prepare();
//                    mPlayer.start();
//                    btnPlay.setClickable(false);
//                    btnStop.setClickable(true);
//                    tvMP3.setText("실행 중인 음악: " + selectedMP3);
//                    pbMP3.setVisibility(View.VISIBLE);
//                } catch (IOException e) {
//
//                }
//            }
//        });
//
//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mPlayer.stop();
//                mPlayer.reset();
//                btnPlay.setClickable(true);
//                btnStop.setClickable(false);
//                tvMP3.setText("실행 중인 음악: ");
//                pbMP3.setVisibility(View.INVISIBLE);
//            }
//        });
//
//        btnStop.setClickable(false);
    }

    public void init() {
        listViewMP3 = findViewById(R.id.listViewMP3);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        tvMP3 = findViewById(R.id.tvMP3);
        tvTime = findViewById(R.id.tvTime);
        pbMP3 = findViewById(R.id.pbMP3);
    }

    public void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);
    }

    public void initLr() {
        listViewMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMP3 = mp3List.get(i);
            }
        });

        selectedMP3 = mp3List.get(0);

        btnPlay.setOnClickListener(view -> {
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(mp3Path + selectedMP3);
                mPlayer.prepare();
                mPlayer.start();
                btnPlay.setClickable(false);
                btnPause.setClickable(true);
                btnStop.setClickable(true);
                tvMP3.setText("실행 중인 음악 : " + selectedMP3);
                pbMP3.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        if(mPlayer == null) {
                            return;
                        }
                        pbMP3.setMax(mPlayer.getDuration());
                        while (mPlayer.isPlaying()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbMP3.setProgress(mPlayer.getCurrentPosition());
                                    tvTime.setText("진행 시간 : " + timeFormat.format(mPlayer.getCurrentPosition()));
                                }
                            });
                            SystemClock.sleep(200);
                        }
                    }
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnPause.setOnClickListener(view -> {
            if(mPlayer.isPlaying()) {
                mPlayer.pause();
                position = mPlayer.getCurrentPosition();
                btnPause.setText("이어듣기");
                btnPause.setClickable(true);
                btnStop.setClickable(true);
                pbMP3.setVisibility(View.INVISIBLE);
            }
            else {
                mPlayer.seekTo(position);
                mPlayer.start();
                btnPause.setText("일시정지");
                pbMP3.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        if(mPlayer == null) {
                            return;
                        }
                        pbMP3.setMax(mPlayer.getDuration());
                        while (mPlayer.isPlaying()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbMP3.setProgress(mPlayer.getCurrentPosition());
                                    tvTime.setText("진행 시간 : " + timeFormat.format(mPlayer.getCurrentPosition()));
                                }
                            });
                            SystemClock.sleep(200);
                        }
                    }
                }.start();
            }
        });

        btnStop.setOnClickListener(view -> {
            mPlayer.stop();
            mPlayer.reset();
            btnPlay.setClickable(true);
            btnPause.setClickable(false);
            btnStop.setClickable(false);
            tvMP3.setText("실행 중인 음악 : ");
            pbMP3.setProgress(0);
            pbMP3.setVisibility(View.INVISIBLE);
            tvTime.setText("진행 시간 : ");
        });

        btnStop.setClickable(false);
    }
}