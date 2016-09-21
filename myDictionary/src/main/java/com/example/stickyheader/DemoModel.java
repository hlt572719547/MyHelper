package com.example.stickyheader;

/**
 * Created by Administrator on 2016/6/3.
 */
public class DemoModel {
    String title;
    String description;
    Class activityClass;

    public DemoModel(String title, String description, Class activityClass) {
        this.title = title;
        this.description = description;
        this.activityClass = activityClass;
    }
}
