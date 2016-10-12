package com.mokdoryeong.team7.mokdoryeong;

import org.joda.time.DateTime;

/**
 * Created by park on 2016-10-11.
 */
public class CervicalData {

    private DateTime startTime;
    private DateTime finishTime;
    private float averageAngle;
    private float cervicalRiskIndex;

    public CervicalData(DateTime startTime, DateTime finishTime, float averageAngle, float cervicalRiskIndex){
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.averageAngle = averageAngle;
        this.cervicalRiskIndex = cervicalRiskIndex;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public float getCervicalRiskIndex() {
        return cervicalRiskIndex;
    }

    public void setCervicalRiskIndex(float cervicalRiskIndex) {
        this.cervicalRiskIndex = cervicalRiskIndex;
    }

    public DateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(DateTime finishTime) {
        this.finishTime = finishTime;
    }

    public float getAverageAngle() {
        return averageAngle;
    }

    public void setAverageAngle(float averageAngle) {
        this.averageAngle = averageAngle;
    }

    public boolean isValid(){
        if(finishTime.minus(startTime.getMillis()).getSecondOfMinute() < 20)
            return false;
        else
            return true;
    }

}
