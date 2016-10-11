package com.mokdoryeong.team7.mokdoryeong;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity  {

    private LinearLayout layoutGraph;
    private GraphView gv;

    private Button btnWidgetOn;
    private Button btnWidgetOff;

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

        gv = new GraphView(this, handler);
        layoutGraph.addView(gv);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

}
