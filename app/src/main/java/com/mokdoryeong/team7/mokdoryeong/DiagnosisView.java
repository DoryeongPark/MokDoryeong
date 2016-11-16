package com.mokdoryeong.team7.mokdoryeong;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class DiagnosisView extends Activity {

    private ImageView diagnosisView;
    private Mat image;
    private ArrayList<Rect> roiCandidates;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();


        setContentView(R.layout.dialog_diagnosis);

        diagnosisView = (ImageView)findViewById(R.id.diagnosis_view);
        Bitmap resultImage = Bitmap.createBitmap(image.cols(), image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, resultImage);
        diagnosisView.setImageBitmap(resultImage);

    }
}
