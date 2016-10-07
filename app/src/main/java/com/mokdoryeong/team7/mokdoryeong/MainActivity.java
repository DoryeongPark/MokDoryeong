package com.mokdoryeong.team7.mokdoryeong;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.hardware.SensorEventListener;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String OpenCVTag = "OpenCV";
    private static final String SensorTag = "Sensor";

    private SensorManager sm;
    private Sensor orientationSensor;

    static{
        if(OpenCVLoader.initDebug())
            Log.d(OpenCVTag, "Opencv successfuly loaded");
        else
            Log.d(OpenCVTag, "Opencv not loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        orientationSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sm.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Not using accuracy sensor */
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /* Only using gyro sensor - Print x, y, z */
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            Log.d(SensorTag, String.valueOf(event.values[0]) + " " +
                                            event.values[1] + " " +
                                            event.values[2]);

        }
    }
}
