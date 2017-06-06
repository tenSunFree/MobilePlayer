package com.tensun.mobileplayer6;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tensun.mobileplayer6.domain.VideoItem;
import com.tensun.mobileplayer6.domain.VideoListAdapter;

import java.util.ArrayList;

/**
 * @author tenSun
 *
 * Q: 在影片功能中, 新增一個切換成橫屏全屏的功能
 * A:
 *
 * Q: 當點選全螢幕功能的時候, 可以切換成橫屏全屏, 如果再次點選全螢幕功能或者返回上一頁時, 返回非全屏時的狀態
 * A: 開啟影片時, 分成第一階段和第二階段
 *     第一階段: 影片為豎頻非全屏, 下方可以有影片相關資訊介紹, 如果選擇全螢幕功能, 則進入第二階段
 *     第二階段: 影片為橫屏且全屏, 如果再次選擇全螢幕功能或者返回上一頁, 則回到第一階段
 *
 * Q: 如何做成跟Youtube播放影片一樣?
 * A:
 *
 * Q: 把原本的SplashActivity替換成SplashFragment
 * A:
 */

public class VideoListActivity extends AppCompatActivity {

    private ListView lv_videolist;
    private TextView lv_videolist_novideo;
    private String number = "777";
    private TextView test;

    // 視頻列表
    private ArrayList<VideoItem> videoItems;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (videoItems.size() > 0) {
                        lv_videolist.setAdapter(new VideoListAdapter(VideoListActivity.this, videoItems));

                        // Log.v("tensuntest", "handleMessage-顯示在ListView中");
                        // 有視頻信息
                        // lv_videolist_novideo.setVisibility(View.GONE);
                        // Log.v("tensuntest", "handleMessage - case 0");
                        // 顯示在ListView中
                    } else {
                        lv_videolist_novideo.setVisibility(View.VISIBLE);  // 無視頻信息
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tensuntest", "VideoListActivity - onCreate()");
        // Log.v("tensuntest", "VideoListActivity - onCreate() - ");
        // Log.v("tensuntest", stringForTime(1000000));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        initView();
        getData();
        setListener();

        // number = stringForTime(2999000);
        // Log.v("tensuntest", "VideoListActivity - onCreate() - " + number);
    }

    private void setListener() {
        lv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                VideoItem item = videoItems.get(position);  // 根據位置得到對應的數據
                Intent intent = new Intent(VideoListActivity.this, com.tensun.mobileplayer6.VideoPlayerActivity.class);
                intent.setData(Uri.parse(item.getData()));
                startActivity(intent);

                // intent.putExtra("TOSTRING", item.toString());
                // intent.setAction(Intent.ACTION_VIEW);
                // intent.setDataAndType(Uri.parse(item.getData()), "video/*");
                // Intent intent = new Intent();
                // intent.setAction(Intent.ACTION_VIEW);
                // intent.setDataAndType(Uri.parse(item.getData()), "video/*");
                // startActivity(intent);
            }
        });
    }

    // 得到手機的視頻
    private void getData() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                videoItems = new ArrayList<VideoItem>();

                String[] projection = new String[] {
                        MediaStore.Files.FileColumns.DISPLAY_NAME,
                        MediaStore.Files.FileColumns.DATA,
                        MediaStore.Files.FileColumns.SIZE
                };

                // String[] projection = {
                //         MediaStore.Video.Media.DISPLAY_NAME,  // 視頻的名稱
                //         MediaStore.Video.Media.DURATION,  // 視頻的時長
                //         MediaStore.Video.Media.SIZE,  // 視頻的大小
                //         MediaStore.Video.Media.DATA  // 視頻在sdCard下的絕對路徑
                // };

                // 視頻的路徑
                // Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                // 讀取手機裡面所有的 一般類型視頻
                Cursor cursor = getContentResolver().query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,       // 數據源
                        projection,
                        null,
                        null,
                        null
                );

                // 讀取手機裡面所有的 rmvb類型視頻
                Cursor cursor2 = getContentResolver().query(
                        Uri.parse("content://media/external/file"),             // 數據源
                        projection,
                        MediaStore.Files.FileColumns.DATA + " like ?",        // 條件為文件類型
                        new String[]{"%.rmvb"},                                // 類型為rmvb
                        null                                                   // 默認排序
                );

                // Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

                  while (cursor.moveToNext()) {
                      VideoItem item = new VideoItem();

                      int titleindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                      int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                      int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

                      String name = cursor.getString(titleindex);  // 視頻名稱
                      item.setName(name);
                      long size = cursor.getLong(sizeindex);      // 視頻大小
                      item.setSize(size);
                      String data = cursor.getString(dataindex);  // 視頻播放地址
                      item.setData(data);

                      // String name = cursor.getString(0);  // 視頻名稱
                      // item.setName(name);
                      // long duration = cursor.getLong(1);  // 視頻時長
                      // item.setDuration(duration);
                      // long size = cursor.getLong(2);      // 視頻大小
                      // item.setSize(size);
                      // String data = cursor.getString(3);  // 視頻播放地址
                      // item.setData(data);
                      Log.v("tenSunTest", "getData() - " + data);

                      // 放入視頻列表中
                      videoItems.add(item);
                  }

                while (cursor2.moveToNext()) {
                    VideoItem item = new VideoItem();

                    int titleindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                    int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

                    String name = cursor2.getString(titleindex);  // 視頻名稱
                    item.setName(name);
                    long size = cursor2.getLong(sizeindex);      // 視頻大小
                    item.setSize(size);
                    String data = cursor2.getString(dataindex);  // 視頻播放地址
                    item.setData(data);

                    // String name = cursor.getString(0);  // 視頻名稱
                    // item.setName(name);
                    // long duration = cursor.getLong(1);  // 視頻時長
                    // item.setDuration(duration);
                    // long size = cursor.getLong(2);      // 視頻大小
                    // item.setSize(size);
                    // String data = cursor.getString(3);  // 視頻播放地址
                    // item.setData(data);
                    Log.v("tenSunTest", "getData() - " + data);

                    // 放入視頻列表中
                    videoItems.add(item);
                }

                // if (cursor != null) {
                //     if (cursor.moveToFirst()) {
                //         VideoItem item = new VideoItem();
//
                //         int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                //         int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                //         int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
//
                //         do {
                //             String name = cursor.getString(0);  // 視頻名稱
                //             item.setName(name);
                //             long size = cursor.getLong(1);      // 視頻大小
                //             item.setSize(size);
                //             String data = cursor.getString(2);  // 視頻播放地址
                //             item.setData(data);
                //             videoItems.add(item);  // 放入視頻列表中
                //         } while (cursor.moveToNext());
                //     }
                // }

                cursor.close();  // 關閉
                cursor2.close();  // 關閉

                handler.sendEmptyMessage(0);  // 發消息到主線程, 顯示數據

                // Log.v("tensuntest", "VideoListActivity - getData() - end");
            }
        }.start();
    }

    // 初始化View
    public void initView() {
        lv_videolist = (ListView) findViewById(R.id.lv_videolist);
        lv_videolist_novideo = (TextView) findViewById(R.id.lv_videolist_novideo);
        test = (TextView) findViewById(R.id.test);

        // adapter = new VideoListAdapter(this, videoItems);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("tensuntest", "VideoListActivity - onDestroy()");
    }

    // public class Utils {
//
    //     private StringBuilder mFormatBuilder;
    //     private Formatter mFormatter;
    //     private Number number;
//
    //     public Utils() {
    //         // 轉換成字符串的時間
    //         mFormatBuilder = new StringBuilder();
    //         mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    //     }
//
    //     public String stringForTime(int timeMs) {
    //         int totalSeconds = timeMs / 1000;
    //         int seconds = totalSeconds % 60;
    //         int minutes = (totalSeconds / 60) % 60;
    //         int hours = totalSeconds / 3600;
    //         mFormatBuilder.setLength(0);
//
    //         return mFormatter.format("%d:%d:%d", hours, minutes, seconds).toString();
//
    //         // long minutes = (totalSeconds / 60) % 60;
    //         // if (hours > 0) {
    //         //     return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
    //         // } else {
    //         //     return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
    //         // }
    //     }
    // }

    // class VideoListAdapter extends BaseAdapter {
//
    //     // 返回總條數
    //     @Override
    //     public int getCount() {
    //         return videoItems.size();
    //     }
//
    //     @Override
    //     public View getView(int position, View view, ViewGroup viewGroup) {
    //         TextView textView = new TextView(VideoListActivity.this);
    //         textView.setText(videoItems.get(position).toString());
    //         textView.setTextSize(20);
    //         textView.setTextColor(Color.WHITE);
    //         return textView;
    //     }
//
    //     @Override
    //     public Object getItem(int i) {
    //         return null;
    //     }
//
    //     @Override
    //     public long getItemId(int i) {
    //         return 0;
    //     }
    // }
}
