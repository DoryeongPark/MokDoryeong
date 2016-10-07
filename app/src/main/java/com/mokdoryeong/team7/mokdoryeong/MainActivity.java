package com.mokdoryeong.team7.mokdoryeong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Opencv";

    static{
        if(OpenCVLoader.initDebug())
            Log.d(TAG, "Opencv successfuly loaded");
        else
            Log.d(TAG, "Opencv not loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
