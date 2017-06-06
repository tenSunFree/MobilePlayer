package com.tensun.mobileplayer6;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * @author tenSun
 * 啟動頁面
 * 目的: 啟動此App時, 第一個畫面
 *
 * Q: 從class修改物件名稱, 並且同步該物件的其他名稱
 * A: 反白該物件 → Refactor → Rename
 *
 * Q: 讓圖片產生透明度變化
 * A: ImageView imageView → Animation animation
 * res → anim → 建立動畫內容資源
 *  AnimationUtils.loadAnimation(this, R.anim.動畫內容資源)
 * startAnimation(animation)
 *
 * Q: 怎樣樣算是好的開頭動畫?
 * A: 慢慢地旋轉漸漸顯示, 最後來個放大ending pose
 *
 * Q: 不要顯示標題欄
 * A: AndroidManifest → theme → NoActionBar
 *
 * Q: 從啟動畫面跳至主畫面, 主畫面的顏色應該怎麼安排, 可以有質感的呈現?
 * A:
 *
 * Q: 當手指觸碰螢幕 會調用onTouchEvent(), 會執行多次內容 導致VideoListActivity被創建多次 產生多個VideoListActivity
 * A: 方法1: AndroidManifest → android:launchMode="singleTop"
 *     方法2: boolean isVideoListActivityOpen = false → if (isVideoListActivityOpen  == false), isVideoListActivityOpen = true
 *
 * Q: ListView顯示的是所有類型的視頻
 * A:
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private Animation animation;
    private Intent intent;
    private boolean isVideoListActivityOpen = false;  // 是否啟動了VideoListActivity
    private Sound3 sound3;
    private boolean isDestroy = false;

    // private boolean nextGo = false;
    // private ProgressBar loadingProgressBar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    imageView.startAnimation(animation);
                    break;
                case 2:
                    if (isVideoListActivityOpen  == false) {
                        isVideoListActivityOpen = true;
                        sound3.recyle();
                        startActivity(intent);
                        finish();
                    }
                    break;

                // nextGo = true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tensuntest", "SplashActivity - onCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // 設置隱藏標題欄
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        new Thread(new anim1()).start();
        startVideoList();

        new Thread(new music2()).start();

        // setProgressBarVisibility(true);
        // new Thread(new loading()).start();
        // requestWindowFeature(Window.FEATURE_PROGRESS);
    }

    class music2 implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if (isDestroy == false) {
                    sound3 = new Sound3(SplashActivity.this);
                    sound3.setMusicSt(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startVideoList() {
        new Thread(new intent()).start();

        // Log.v("tensuntest", "SplashActivity - startVideoList() - nextGo: " + nextGo);
        // if (nextGo == true) {
        //
        // }
    }

    public void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim1);
        animation.setFillAfter(true);
        intent = new Intent(this, com.tensun.mobileplayer6.VideoListActivity.class);

        // loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
    }

    class anim1 implements Runnable {

        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    }

    class intent implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3300);
                if (isDestroy == false) {
                    handler.sendEmptyMessage(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isVideoListActivityOpen  == false) {
            isVideoListActivityOpen = true;
            isDestroy = true;
            if (sound3 != null) {
                sound3.recyle();
            }
            startActivity(intent);
            finish();
        }
        return false;

        // nextGo = false;
        // Thread.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVideoListActivityOpen = true;
        Log.v("tensuntest", "SplashActivity - onDestroy()");
        isDestroy = true;
        if (sound3 != null) {
            sound3.recyle();
        }
    }

    // class loading implements Runnable {

   //     @Override
   //     public void run() {
   //         for (int i=0; i<10; i++) {
   //             try {
   //                 Thread.sleep(150);
   //                 handler.sendEmptyMessage(0);
   //             } catch (InterruptedException e) {
   //                 e.printStackTrace();
   //             }
   //         }
   //     }
   // }

    // // 自由落體
    // class verticalRun implements Runnable {
//
    //     @Override
    //     public void run() {
    //         handler.sendEmptyMessage(1);
    //     }
    // }
}
