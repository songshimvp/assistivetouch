package com.bumblebee.remindeasy.service;

import com.bumblebee.remindeasy.widgets.EasyTouchView;
import com.bumblebee.remindeasy.widgets.EasyTouchView.ServiceListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuxiliaryService extends Service implements ServiceListener {
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        // 创建service时一个 实例化一个TableShowView对象并且调用他的fun()方法把它注册到windowManager上
        super.onCreate();
        new EasyTouchView(this, this).initTouchViewEvent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIntent = intent;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void OnCloseService(boolean isClose) {
        stopService(mIntent);
    }
}