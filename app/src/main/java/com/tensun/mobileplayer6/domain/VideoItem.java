package com.tensun.mobileplayer6.domain;

/**
 * @author tenSun
 * 目的: 代表一個視頻的訊息
 */
public class VideoItem {

    private String name;
    private long duration;
    private long size;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // public long getDuration() {
    //     return duration;
    // }

    // public void setDuration(long duration) {
    //     this.duration = duration;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "影片名稱: " + name + '\'' +
                ",\nduration=" + duration +
                ",\nsize=" + size +
                "\n}";
    }

    // @Override
    // public String toString() {
    //     return "VideoItem{\n" +
    //             "data='" + data + '\'' +
    //             ",\nname='" + name + '\'' +
    //             ",\nduration=" + duration +
    //             ",\nsize=" + size +
    //             "\n}";
    // }
}
