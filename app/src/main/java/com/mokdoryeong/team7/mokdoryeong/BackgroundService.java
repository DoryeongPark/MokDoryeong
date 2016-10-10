package com.mokdoryeong.team7.mokdoryeong;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundService extends Service {

    private WidgetView widget;

    private WidgetThread widgetThread;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(BackgroundService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Toast.makeText(BackgroundService.this, "Background Service", Toast.LENGTH_SHORT).show();
        }
    };

    public BackgroundService() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        widget = new WidgetView(this);
        //Setting for top window
        WindowManager.LayoutParams params
                = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.width = 100;
        params.height = 100;

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(widget, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        widgetThread = new WidgetThread(handler);
        widgetThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        widgetThread.abort();
        widgetThread = null;

        if(widget != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(widget);
            widget = null;
        }
    }


}
