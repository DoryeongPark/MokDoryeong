package com.mokdoryeong.team7.mokdoryeong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by park on 2016-10-12.
 */
public class CervicalDataManager {

    private DbOpenHelper dbOpenHelper;
    private Context context;

    private Deque<CervicalData> dataQueue;

    private BroadcastReceiver dataRequestReceiver;

    public CervicalDataManager(Context context){
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
        dataQueue = new ArrayDeque<CervicalData>();
        loadData();

        dataRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strReceived = intent.getStringExtra("DataRequest");
                Log.d("Database", strReceived);
                context.sendBroadcast(new Intent("com.mokdoryeong.team7.SEND_GRAPH_DATA_RESPONSE")
                        .putExtra("DataRequest","Data response is successfully sent"));
            }
        };
        context.registerReceiver(dataRequestReceiver,
                new IntentFilter("com.mokdoryeong.team7.SEND_GRAPH_DATA_REQUEST"));
        //This is for initial data of graph view
        Intent intent = new Intent("com.mokdoryeong.team7.SEND_GRAPH_DATA_RESPONSE");
        intent.putExtra("DataResponse","Data response is successfully sent");
        intent.putExtra("Data", dataQueue.getFirst());
        context.sendBroadcast(intent);
    }

    public void insert(CervicalData cd){
        dataQueue.addFirst(cd);
        insertToDatabase(cd);
        if(DateTime.now().minusHours(12).isAfter(dataQueue.getLast().getStartTime().getMillis()))
            removeFromDatabase(dataQueue.pollLast());
    }

    private void loadData(){
        dbOpenHelper.open();
        Cursor c = dbOpenHelper.getAllColumns();
        while(c.moveToNext()){
            dataQueue.addFirst(new CervicalData(new DateTime(c.getLong(c.getColumnIndex(Databases.CreateDB.STARTTIME))),
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

    @Override
    protected void finalize(){
        context.unregisterReceiver(dataRequestReceiver);
    }
}
