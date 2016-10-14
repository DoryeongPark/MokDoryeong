package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Created by park on 2016-10-12.
 */
public class CervicalDataCreator {

    private long counter = 0L;

    private final int DATA_FILTER_COUNT = 3;
    private int dataFilter = 0;

    private float averageAngle = 0.0f;
    private float cervicalRiskIndex = 0.0f;

    private DataHeartBeat dataHeartBeat = null;

    private CervicalDataManager cervicalDataManager;

    public CervicalDataCreator(Context context){
        cervicalDataManager = new CervicalDataManager(context);
    }

    public void update(float pitchAngle){

        if(dataFilter == DATA_FILTER_COUNT){
            //Start or continue HeartBeat
            if(dataHeartBeat == null || !dataHeartBeat.isAlive()) {
                dataHeartBeat = new DataHeartBeat(this);
                dataHeartBeat.start();
                Log.d("DataHeartBeat", "DataHeartBeat started");
            }else{
                dataHeartBeat.continueHeartBeat();
            }

            dataFilter = 0;
            ++counter;

            //Calculate average angle
            averageAngle = (averageAngle * (counter - 1) + pitchAngle) / counter;

            //Calculate cervical risk index
            calculateCervicalRiskIndex();

        }else{
            ++dataFilter;
            return;
        }
    }

    public void onHeartBeatFinished(DateTime startTime, DateTime finishTime){
        Log.d("DataHeartBeat", "DataHeartBeat finished");
        if(finishTime.minus(startTime.getMillis()).getMillis() < 20000) {
            resetMembers();
            return;
        }
        cervicalDataManager.insert(new CervicalData(startTime,
                                            finishTime,
                                            averageAngle,
                                            cervicalRiskIndex));
        resetMembers();
    }

    private void calculateCervicalRiskIndex(){
        cervicalRiskIndex = 0.0f;
    }

    private void resetMembers(){
        dataFilter = 0;
        counter = 0L;
        averageAngle = 0.0f;
        cervicalRiskIndex = 0.0f;
    }
}
