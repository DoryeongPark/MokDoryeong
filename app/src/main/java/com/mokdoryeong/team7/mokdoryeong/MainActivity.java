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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutGraph;
    private GraphView gv;

    private Button btnWidgetOn;
    private Button btnWidgetOff;
    private Button btnDiagnosis;

    private BroadcastReceiver dataResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strReceived = intent.getStringExtra("DataResponse");
            ArrayList<CervicalData> dataArr = (ArrayList<CervicalData>)intent.getSerializableExtra("Data");
            if(strReceived != null && dataArr != null) {
                gv.update(dataArr);
                Log.d("Database", strReceived);
            }
        }
    };;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutGraph = (LinearLayout)findViewById(R.id.layout_graph);

        btnWidgetOn = (Button)findViewById(R.id.btn_widgeton);
        btnWidgetOff = (Button)findViewById(R.id.btn_widgetoff);
        btnDiagnosis = (Button)findViewById(R.id.btn_diagnosis);

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
        btnDiagnosis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiagnosisCreator.class);
                startActivity(intent);
            }
        });

        gv = new GraphView(this);
        layoutGraph.addView(gv);
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(BackgroundService.isAlive == false){//This is for initial data of Graph view
            startService(new Intent(MainActivity.this, BackgroundService.class));
            loadCervicalDataFromService();
            stopService(new Intent(MainActivity.this, BackgroundService.class));
        }else {
            loadCervicalDataFromService();
        }
    }

    @Override
    protected void onPause(){
        unregisterReceiver(dataResponseReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void loadCervicalDataFromService(){
        registerReceiver(dataResponseReceiver,
                new IntentFilter("com.mokdoryeong.team7.SEND_GRAPH_DATA_RESPONSE"));

        Intent intent = new Intent("com.mokdoryeong.team7.SEND_GRAPH_DATA_REQUEST");
        intent.putExtra("DataRequest", "Data request is successfully sent");
        sendBroadcast(intent);
    }

}
