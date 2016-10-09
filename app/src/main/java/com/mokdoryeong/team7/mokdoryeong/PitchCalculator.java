package com.mokdoryeong.team7.mokdoryeong;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by park on 2016-10-09.
 */
public class PitchCalculator implements SensorEventListener {

    private SensorManager sm;

    private Sensor accSensor;
    private Sensor magSensor;

    private float[] accData;
    private float[] magData;
    private float[] rotation = new float[9];
    private float[] result = new float[3];

    private PitchAngleListener pitchAngleListener;

    interface PitchAngleListener{
        public void onPitchAngleCalculated(float pitchAngle, float stand);
    }

    public PitchCalculator(SensorManager sm){

        this.sm = sm;
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accData = event.values.clone();

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magData = event.values.clone();

        calculatePitch();

    }

    private void calculatePitch(){

        if(accData != null && magData != null){
            SensorManager.getRotationMatrix(rotation, null, accData, magData);
            SensorManager.getOrientation(rotation, result);

            pitchAngleListener.onPitchAngleCalculated(result[1], result[2]);
        }

    }

    public void turnOn(){
        sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void turnOff(){
        sm.unregisterListener(this);
    }



    public void registerPitchAngleListener(PitchAngleListener pitchAngleListener){
        this.pitchAngleListener = pitchAngleListener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Not using accuracy sensor */
    }

}
