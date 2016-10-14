package com.mokdoryeong.team7.mokdoryeong;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by park on 2016-10-11.
 */
public class CervicalData implements Parcelable {

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

    //Parcel part
    public CervicalData(Parcel in){
        String[] data= new String[4];
        in.readStringArray(data);
        this.startTime = new DateTime((Long.parseLong(data[0])));
        this.finishTime = new DateTime((Long.parseLong(data[1])));
        this.averageAngle = Float.parseFloat(data[2]);
        this.cervicalRiskIndex = Float.parseFloat(data[3]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(startTime.getMillis()),
                                            String.valueOf(finishTime.getMillis()),
                                            String.valueOf(averageAngle),
                                            String.valueOf(cervicalRiskIndex)});
    }

    public static final Parcelable.Creator<CervicalData> CREATOR= new Parcelable.Creator<CervicalData>() {
        @Override
        public CervicalData createFromParcel(Parcel source) {
            return new CervicalData(source);  //using parcelable constructor
        }
        @Override
        public CervicalData[] newArray(int size) {
            return new CervicalData[size];
        }
    };

}
