package com.tensun.mobileplayer6;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

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
 *
 */

public class VideoListActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) findViewById(R.id.text);

        String selectMimeType = MediaStore.Files.FileColumns.MIME_TYPE
                + " = ?";
        String apkMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                "apk");
        String[] selectArgsApk = new String[]{ apkMimeType };

        Log.d("tenSunTset", apkMimeType);

        String p = new String();
        String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE };
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://media/external/file"), projection,
                MediaStore.Files.FileColumns.DATA + " like ?", new String[]{"%.mp4"}, null);
        //Cursor cursor1 = getContentResolver().query(
        //		Uri.parse("content://media/external/file"), projection,
        //		selectMimeType, selectArgsApk, null);
        //Log.d("APK", cursor1.getCount()+"");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns._ID);
                int dataindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int sizeindex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                do {
                    String id = cursor.getString(idindex);
                    String path = cursor.getString(dataindex);
                    String size = cursor.getString(sizeindex);
                    p += id + "-" + path + "-" + size + "\n";
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        //cursor1.close();
        textView.setText(p);

    }
}
