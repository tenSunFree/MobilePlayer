package com.tensun.mobileplayer6;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tensun.mobileplayer6.utils.Utils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * @author tenSun
 *
 * Q:  如何使Activity不要顯示標題欄和狀態欄
 * A: extend Activity → android:theme="@android:style/...No...Fullscreen"
 *
 * Q: 想讓自定義的播放器可以被調用
 * A: AndroidManifest → Activity → intent-filter → <action android:name="android.intent.action.VIEW" /> , 必要
 *                                                                           → <category android:name="android.intent.category.DEFAULT" /> , 必要
 *                                                                           → <data android:mimeType="video/*" /> , 增加獨特性
 *
 * Q: 使用Vitamio作為解碼器
 * A: Import Module... → gradle.properties 把參數賦值 → Rebuild Project
 *     import → io.vov.vitamio.MediaPlayer, io.vov.vitamio.widget.VideoView
 *
 * Q: 對於自定義播放器, 已經很熟悉了嗎?
 * A: 還沒有完全熟悉, 找時間再來把它精進
 */
public class VideoPlayerActivity extends Activity implements View.OnClickListener {

    protected static final int PROGRESS = 2017;  // 更新進度的消息
    private VideoView videoView;
    private Uri uri;  // 視頻播放地址
    private TextView tv_current_time;
    private TextView tv_duration;
    private SeekBar seekbar;
    private ImageButton btn_play_pause;
    private Utils utils;
    private boolean isDestroy = false;
    private boolean isProgressChanged = false;
    private boolean isSave = false;
    private static boolean isTouch = false;
    private View control_player;
    private Integer number = null;
    private boolean isRepeat = false;
    private boolean isRepeat2 = false;
    private int i2;
    private int i3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btn_play_pause.setImageResource(R.mipmap.ic_media_play);
                    break;
                case 1:
                    btn_play_pause.setImageResource(R.mipmap.ic_media_pause);
                    break;
                case 2:
                    Log.v("tensuntest", "VideoPlayerActivity - case 2 - " + isTouch);
                    control_player.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Log.v("tensuntest", "VideoPlayerActivity - case 2 - " + isTouch);
                    if (isTouch = true) {
                        handler.sendEmptyMessage(3);
                    }
                    break;
                case 4:
                    Log.v("tensuntest", "VideoPlayerActivity - case 4 - " + isTouch);
                    if (isTouch == false) {
                        control_player.setVisibility(View.GONE);
                    }
                    break;
                case PROGRESS:
                    if (isProgressChanged == false) {
                        // 得到當前的播放進度
                        int currentPosition = (int) videoView.getCurrentPosition();
                        tv_current_time.setText(utils.stringForTime(currentPosition));
                        // 得到視頻的總長度
                        int duration = (int) videoView.getDuration();
                        tv_duration.setText(utils.stringForTime(duration));

                        seekbar.setProgress((int) videoView.getCurrentPosition());
                    }

                    isProgressChanged = false;

                    seekbar.setMax((int) videoView.getDuration());

                    if (isDestroy == false) {
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    }

                    break;

                // Log.v("tensuntest", "VideoPlayerActivity - " + currentPosition);
                // Log.v("tensuntest", "VideoPlayerActivity - " + duration);
//
                // double currentSeekbar = (double) currentPosition / (double) duration * 100;
                // int currentSeekbar2 = (int) currentSeekbar;
                // seekbar.setProgress(currentSeekbar2);
            }
        }
    };

    // Log.v("tensuntest", "VideoPlayerActivity - " + currentSeekbar2);
    // private String toString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tensuntest", "VideoPlayerActivity - onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initView();
        videoView.setVideoURI(uri);
        setListener();

        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        uri = getIntent().getData();  // 得到數據
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        utils = new Utils();
        control_player = findViewById(R.id.control_player);

        // toString = getIntent().getStringExtra("TOSTRING");
        // timer = new Timer();
    }

    private void setListener() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                handler.sendEmptyMessage(1);

                // 發送消息, 開始更新播放進程
                handler.sendEmptyMessage(PROGRESS);
            }
        });

        /** 設置播放完成的監聽 */
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();  // 退出播放器
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(VideoPlayerActivity.this, "片源異常", Toast.LENGTH_SHORT).show();
                finish();
                return true;  // false為默認
            }
        });

        btn_play_pause.setOnClickListener(this);

        // 設置控制視頻的控制面板
        // videoView.setMediaController(new MediaController(this));

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v("tensuntest", "VideoPlayerActivity - onTouch() - " + number);
                number = 5;
                handler.sendEmptyMessage(2);
                new Thread(new controlListener()).start();
                return true;
            }
        });

        control_player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch = true;
                handler.sendEmptyMessage(2);
                new Thread(new controlListener()).start();
                return true;
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    videoView.seekTo(i);
                    isTouch = true;
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(1);
                    new Thread(new controlListener()).start();
                    Log.v("tensuntest", "VideoPlayerActivity - setOnSeekBarChangeListener() - " + Thread.currentThread().getName() + i + b);

                    // isProgressChanged = true;
                    // tv_current_time.setText(utils.stringForTime(i));
                    // tv_duration.setText(utils.stringForTime(i));
                    // seekBar.setProgress(i);
                }

                // Log.v("tensuntest", "VideoPlayerActivity - setOnSeekBarChangeListener() - " + i + b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = true;
                new Thread(new controlListener2()).start();
            }
        });
    }

    class controlListener implements Runnable{
        @Override
        public void run() {
            Log.v("tensuntest", "VideoPlayerActivity - run() - " + number);

            if (isRepeat == false) {
                isRepeat = true;
                try {
                    Thread.sleep(1500);
                    if (isTouch == false) {
                        handler.sendEmptyMessage(4);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isRepeat = false;
            }

            // try {
            //     // isTouch = false;
            //     Thread.sleep(5000);
            //     isTouch = false;
            //     Log.v("tensuntest", "VideoPlayerActivity - run() - " + isTouch);
//
//
            //     if (isTouch == false) {
            //         Log.v("tensuntest", "VideoPlayerActivity - run() - if (isTouch = false) -" + isTouch);
            //         handler.sendEmptyMessage(4);
            //     } else {
            //         isTouch = true;
            //         Thread.sleep(5000);
            //         run();
            //     }
//
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
    }

    class controlListener2 implements Runnable{
        @Override
        public void run() {
            Log.v("tensuntest", "VideoPlayerActivity - controlListener2() - " + isRepeat);

            if (isRepeat2 == false) {
                isRepeat2 = true;
                try {
                    Thread.sleep(1500);
                    isTouch = false;
                    if (isTouch == true) {
                        Thread.sleep(1500);
                        isTouch = false;
                    }
                    Thread.sleep(1500);
                    if (isTouch == true) {
                        Thread.sleep(1500);
                        isTouch = false;
                    }
                    handler.sendEmptyMessage(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isRepeat2 = false;
            }

            // if (number == null) {
            //     try {
            //         number = 50;
            //         while (number > 0) {
            //             Thread.sleep(100);
            //             handler.sendEmptyMessage(2);
            //             number--;
            //         }
            //         number = null;
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //     }
            // }

            // try {
            //     // isTouch = false;
            //     Thread.sleep(5000);
            //     isTouch = false;
            //     Log.v("tensuntest", "VideoPlayerActivity - run() - " + isTouch);
//
//
            //     if (isTouch == false) {
            //         Log.v("tensuntest", "VideoPlayerActivity - run() - if (isTouch = false) -" + isTouch);
            //         handler.sendEmptyMessage(4);
            //     } else {
            //         isTouch = true;
            //         Thread.sleep(5000);
            //         run();
            //     }
//
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_pause:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    handler.sendEmptyMessage(0);
                } else {
                    videoView.start();
                    handler.sendEmptyMessage(1);
                }
                break;
        }

        // if (view == btn_play_pause) {
        //     Log.v("tensuntest", "VideoPlayerActivity - onClick() - btn_play_pause");
        //     if (videoView.isPlaying()) {
        //         videoView.pause();
        //     } else {
        //         videoView.start();
        //     }
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        finish();

        // timer.cancel();
    }

    // public void Toast(String str, int i) {
    //     i2 = i;
    //     switch (i2) {
    //         case 0:
    //             i3 = Toast.LENGTH_SHORT;
    //             break;
    //         case 1:
    //             i3 = Toast.LENGTH_LONG;
    //             break;
    //     }
//
    //     Toast.makeText(this, str, i).show();
    //
    //     return  ;
    //
    // }

    // private Timer timer;

    // private Handler handler = new Handler() {
    //     @Override
    //     public void handleMessage(Message msg) {
    //         super.handleMessage(msg);
    //         switch (msg.what) {
    //             case 0:
    //                 Log.v("tensuntest", "VideoPlayerActivity - handleMessage()");
    //                 videoView.start();
    //                 getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    //                 break;
    //         }
    //     }
    // };
    // public void timerTask() {
    //     timer.schedule(new TimerTask() {
    //         @Override
    //         public void run() {
    //             Log.v("tensuntest", "VideoPlayerActivity - timerTask() - run()");
    //             handler.sendEmptyMessage(0);
    //         }
    //     }, 3000, 3000);
    // }
}
