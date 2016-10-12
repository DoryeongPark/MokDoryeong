package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.database.Cursor;

import org.joda.time.DateTime;

import java.util.LinkedList;

/**
 * Created by park on 2016-10-12.
 */
public class CervicalDataManager {

    private DbOpenHelper dbOpenHelper;
    private Context context;

    private LinkedList<CervicalData> dataQueue;

    public CervicalDataManager(Context context){
        dbOpenHelper = new DbOpenHelper(context);
        dataQueue = new LinkedList<CervicalData>();
    }

    public void insert(CervicalData cd){
        dataQueue.offer(cd);
        insertToDatabase(cd);

        if(dataQueue.getFirst().getStartTime().isBefore(DateTime.now().minusHours(12)))
            removeFromDatabase(dataQueue.poll());//Then delete data in database

    }

    private void insertToDatabase(CervicalData cd){
        dbOpenHelper.open();
        dbOpenHelper.insertRecord(String.valueOf(cd.getStartTime().getMillis()),
                                  String.valueOf(cd.getFinishTime().getMillis()),
                                  String.valueOf(cd.getAverageAngle()),
                                  String.valueOf(cd.getCervicalRiskIndex()));
        dbOpenHelper.close();
    }

    private void removeFromDatabase(CervicalData cd){

    }
}
