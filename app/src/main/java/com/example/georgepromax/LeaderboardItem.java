package com.example.georgepromax;

public class LeaderboardItem {
    private String name,time; // שם, זמן
    private int imgItem; // תמונה שתוצג בצד השמות

    public LeaderboardItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImgItem() {
        return imgItem;
    }

    public void setImgItem(int imgItem) {
        this.imgItem = imgItem;
    }
}
