package com.mokdoryeong.team7.mokdoryeong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    private boolean isExecutedFirst = true;

    private LinearLayout layoutGraph;
    private GraphView gv;

    private Button btnWidgetOn;
    private Button btnWidgetOff;

    private BroadcastReceiver dataResponseReceiver;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){

            }
        }
    };

    static{
        if(OpenCVLoader.initDebug())
            Log.d("OpenCV", "OpenCV successfully loaded");
        else
            Log.d("OpenCV", "OpenCV not loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutGraph = (LinearLayout)findViewById(R.id.layout_graph);

        btnWidgetOn = (Button)findViewById(R.id.btn_widgeton);
        btnWidgetOff = (Button)findViewById(R.id.btn_widgetoff);
        btnWidgetOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"WidgetService 시작", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, BackgroundService.class));
            }
        });
        btnWidgetOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"WidgetService 종료", Toast.LENGTH_SHORT).show();
                stopService(new Intent(MainActivity.this, BackgroundService.class));
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(isExecutedFirst){//This is for initial data of Graph view
            startService(new Intent(MainActivity.this, BackgroundService.class));
            stopService(new Intent(MainActivity.this, BackgroundService.class));
            isExecutedFirst = false;
        }
        loadCervicalDataFromService();
        gv = new GraphView(this, handler);
        layoutGraph.addView(gv);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(dataResponseReceiver);
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(dataResponseReceiver);
        super.onDestroy();
    }

    private void loadCervicalDataFromService(){
        Intent intent = new Intent("com.mokdoryeong.team7.SEND_GRAPH_DATA_REQUEST");
        intent.putExtra("DataRequest", "Data request is successfully sent");
        sendBroadcast(intent);

        dataResponseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strReceived = intent.getStringExtra("DataResponse");
                ArrayList<CervicalData> dataArr = (ArrayList<CervicalData>)intent.getSerializableExtra("Data");
                Log.d("Database", dataArr.get(0).getStartTime().toString());
                Log.d("Database", dataArr.get(1).getStartTime().toString());
                Log.d("Database", strReceived);
            }
        };

        registerReceiver(dataResponseReceiver,
                new IntentFilter("com.mokdoryeong.team7.SEND_GRAPH_DATA_RESPONSE"));
    }

}
