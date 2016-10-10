package com.mokdoryeong.team7.mokdoryeong;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundService extends Service {

    private WindowManager wm;

    private int widgetWidth = 100;
    private int widgetHeight = 100;

    private int widgetPosX = 0;
    private int widgetPosY = 0;

    private PitchCalculator pc;
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

        //For widget
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        widget = new WidgetView(this);
        initiateWidget();

        //For pitch angle
        pc = new PitchCalculator(wm, (SensorManager)getSystemService(SENSOR_SERVICE));
        pc.registerPitchAngleListener(new PitchCalculator.PitchAngleListener() {
            @Override
            public void onPitchAngleCalculated(float pitchAngle, boolean isStanding) {
                //Handling sensor data
                Log.d("Sensor", String.valueOf(pitchAngle + " " + isStanding));
                widget.update(pitchAngle);
            }
        });

        pc.turnOn();
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

        pc.turnOff();
    }

    private void initiateWidget(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.width = widgetWidth;
        params.height = widgetHeight;
        params.x = widgetPosX;
        params.y = widgetPosY;
        wm.addView(widget, params);
    }

    private void setWidgetSize(int w, int h){
        widgetWidth = w;
        widgetHeight = h;

        wm.removeViewImmediate(widget);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.width = widgetWidth;
        params.height = widgetHeight;
        params.x = widgetPosX;
        params.y = widgetPosY;
        wm.addView(widget, params);
    }

    private void setWidgetPos(int x, int y){
        widgetPosX = x;
        widgetPosY = y;

        wm.removeViewImmediate(widget);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.width = widgetWidth;
        params.height = widgetHeight;
        params.x = widgetPosX;
        params.y = widgetPosY;
        wm.addView(widget, params);
    }
}
