package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
        loadData();
        int a;
        if(true)
            a = 0;
    }

    public void insert(CervicalData cd){
        dataQueue.offer(cd);
        insertToDatabase(cd);
        if(DateTime.now().minusHours(12).isAfter(dataQueue.getLast().getStartTime().getMillis()))
            removeFromDatabase(dataQueue.poll());
    }

    private void loadData(){
        dbOpenHelper.open();
        Cursor c = dbOpenHelper.getAllColumns();
        while(c.moveToNext()){
            dataQueue.offer(new CervicalData(new DateTime(c.getLong(c.getColumnIndex(Databases.CreateDB.STARTTIME))),
                                             new DateTime(c.getLong(c.getColumnIndex(Databases.CreateDB.FINISHTIME))),
                                             c.getFloat(c.getColumnIndex(Databases.CreateDB.AVERAGEANGLE)),
                                             c.getFloat(c.getColumnIndex(Databases.CreateDB.CERVICALRISKINDEX))));

        }
        dbOpenHelper.close();
        Log.d("Database", "Data is successfully loaded");
    }

    private void insertToDatabase(CervicalData cd){
        dbOpenHelper.open();
        boolean result = dbOpenHelper.insertRecord(String.valueOf(cd.getStartTime().getMillis()),
                                  String.valueOf(cd.getFinishTime().getMillis()),
                                  String.valueOf(cd.getAverageAngle()),
                                  String.valueOf(cd.getCervicalRiskIndex()));
        Log.d("Database", "Insert successfully - " + result);
        dbOpenHelper.close();
    }

    private void removeFromDatabase(CervicalData cd){
        dbOpenHelper.open();
        boolean result = dbOpenHelper.deleteRecord(String.valueOf(cd.getStartTime().getMillis()));
        Log.d("Database", "Delete succesfully - " + result);
        dbOpenHelper.close();
    }
}
