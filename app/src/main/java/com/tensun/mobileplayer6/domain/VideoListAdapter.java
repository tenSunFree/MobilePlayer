package com.tensun.mobileplayer6.domain;

import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tensun.mobileplayer6.R;
import com.tensun.mobileplayer6.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/30.
 */
public class VideoListAdapter extends BaseAdapter {


    public VideoListAdapter(Context context, ArrayList<VideoItem> videoItems) {
        this.context = context;
        this.videoItems = videoItems;
        utils = new Utils();
    }

    private Context context;
    private ArrayList<VideoItem> videoItems;
    private Utils utils;

    // 返回總條數
    @Override
    public int getCount() {
        return videoItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Log.v("tensuntest", "VideoListAdapter - getView()");

        View view1;

        // 有歷史緩存就用歷史的
        ViewHolder holder;
        if (convertView != null) {
            Log.v("tensuntest", "VideoListAdapter - getView() - convertView != null");
            view1 = convertView;  // 目的: 讓已存在的物件, 不要再次創建
            holder = (ViewHolder) view1.getTag();
        } else {
            Log.v("tensuntest", "VideoListAdapter - getView() - convertView == null");
            view1 = View.inflate(context, R.layout.videolist_item, null);  // 布局文件實例化

            /** 查找View的ID也是消耗資源的, 當創建View的時候, 把查找的放在一個容器(類) **/
            holder = new ViewHolder();
            holder.tv_videolist_name = (TextView) view1.findViewById(R.id.tv_videolist_name);
            // holder.tv_videolist_duration = (TextView) view1.findViewById(R.id.tv_videolist_duration);
            holder.tv_videolist_size = (TextView) view1.findViewById(R.id.tv_videolist_size);
            view1.setTag(holder);

        }

        holder.tv_videolist_name.setText(videoItems.get(position).getName());
        // holder.tv_videolist_duration.setText(utils.stringForTime((int) videoItems.get(position).getDuration()));
        holder.tv_videolist_size.setText(Formatter.formatFileSize(context, videoItems.get(position).getSize()));

        return view1;

        // TextView tv_videolist_name = (TextView) view1.findViewById(R.id.tv_videolist_name);
        // TextView tv_videolist_duration = (TextView) view1.findViewById(R.id.tv_videolist_duration);
        // TextView tv_videolist_size = (TextView) view1.findViewById(R.id.tv_videolist_size);

        // tv_videolist_name.setText(videoItems.get(position).getName());
        // tv_videolist_duration.setText(utils.stringForTime((int) videoItems.get(position).getDuration()));
        // tv_videolist_size.setText(Formatter.formatFileSize(context, videoItems.get(position).getSize()));
        // tv_videolist_duration.setText("" + videoItems.get(position).getDuration());
        // tv_videolist_size.setText("" + videoItems.get(position).getSize());
        // TextView textView = new TextView(context);
        // textView.setText(videoItems.get(position).toString());
        // textView.setTextSize(17);
        // textView.setTextColor(Color.WHITE);
    }

    class ViewHolder {
        TextView tv_videolist_name;
        // TextView tv_videolist_duration;
        TextView tv_videolist_size;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
