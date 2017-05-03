package edu.auburn.smart.ui;

import android.app.Application;

import org.xutils.x;

/**
 * Author: Gary
 * Time: 17/1/13
 */

public class SmartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
