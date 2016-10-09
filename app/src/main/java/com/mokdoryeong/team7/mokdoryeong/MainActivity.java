package com.mokdoryeong.team7.mokdoryeong;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity  {

    private static final String OpenCVTag = "OpenCV";
    private static final String SensorTag = "Sensor";

    private PitchCalculator pc;

    static{
        if(OpenCVLoader.initDebug())
            Log.d(OpenCVTag, "OpenCV successfully loaded");
        else
            Log.d(OpenCVTag, "OpenCV not loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pc = new PitchCalculator((SensorManager)getSystemService(SENSOR_SERVICE));
        pc.registerPitchAngleListener(new PitchCalculator.PitchAngleListener() {
            @Override
            public void onPitchAngleCalculated(float pitchAngle, boolean isStanding) {
                Log.d(SensorTag, String.valueOf(pitchAngle + " " + isStanding));
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        pc.turnOn();
    }

    @Override
    protected void onPause(){
        super.onPause();
        pc.turnOff();
    }

}
