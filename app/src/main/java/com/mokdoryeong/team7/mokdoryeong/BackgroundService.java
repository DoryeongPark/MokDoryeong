package com.mokdoryeong.team7.mokdoryeong;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class BackgroundService extends Service {

    private WindowManager wm;

    private PitchCalculator pc;
    private WidgetView widget;

    private CervicalDataCreator cdc;

    private WidgetThread widgetThread;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(BackgroundService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Toast.makeText(BackgroundService.this, "Background Service", Toast.LENGTH_SHORT).show();
        }
    };

    private BroadcastReceiver windowStateReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                pc.turnOn();
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                pc.turnOff();
            }
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
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        widget = new WidgetView(this, wm);

        //For pitch angle
        pc = new PitchCalculator((SensorManager)getSystemService(SENSOR_SERVICE), wm);
        pc.registerPitchAngleListener(new PitchCalculator.PitchAngleListener() {
            @Override
            public void onPitchAngleCalculated(float pitchAngle, boolean isStanding) {
                //Handling sensor data
                Log.d("Sensor", String.valueOf(pitchAngle + " " + isStanding));
                widget.update(pitchAngle);
                cdc.update(pitchAngle);
            }
        });

        registerReceiver(windowStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(windowStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        //To send sensor data for CervicalDataCreator
        cdc = new CervicalDataCreator(getApplicationContext());

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

        unregisterReceiver(windowStateReceiver);
        pc.turnOff();
    }
}
