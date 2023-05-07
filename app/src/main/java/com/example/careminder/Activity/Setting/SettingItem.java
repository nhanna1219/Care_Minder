package com.example.careminder.Activity.Setting;

public class SettingItem {
    private String title;
    private Integer  iconResId;

    public SettingItem(String title, Integer iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }
    public int getIconResId() {
        return iconResId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
