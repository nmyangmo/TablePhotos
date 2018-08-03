package com.yangmo.tablephotos.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class PhotoChangeService extends Service {

    private Timer timer;
    private TimerTask task;
    private int count;

    public PhotoChangeService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Intent intent = new Intent();
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                intent.putExtra("count", ++count);
                sendBroadcast(intent);
            }
        };
        timer.schedule(task, 1000, 1000);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
