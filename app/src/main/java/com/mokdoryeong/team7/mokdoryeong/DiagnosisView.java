package com.mokdoryeong.team7.mokdoryeong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.Stack;

public class DiagnosisView extends Activity {

    private ImageView diagnosisView;
    private Mat image;
    private int[] faceStartPoint;
    private int[] neckStartPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_diagnosis);

        Intent intent = getIntent();
        image = new Mat(intent.getExtras().getLong("image"));
        faceStartPoint = intent.getExtras().getIntArray("faceStartPoint");
        neckStartPoint = intent.getExtras().getIntArray("neckStartPoint");

        Core.transpose(image, image);
        Core.flip(image, image, 1);

        Core.circle(image, new Point(faceStartPoint[0], faceStartPoint[1]), 5, new Scalar(255, 0, 0));
        Core.circle(image, new Point(neckStartPoint[0], neckStartPoint[1]), 5, new Scalar(0, 255, 0));

        //Analysis Routine
        OpencvRoutine.detectNeckPoints(image.getNativeObjAddr(), faceStartPoint[0], faceStartPoint[1], neckStartPoint[0], neckStartPoint[1]);

        diagnosisView = (ImageView)findViewById(R.id.diagnosis_view);
        Bitmap resultImage = Bitmap.createBitmap(image.cols(), image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, resultImage);
        diagnosisView.setImageBitmap(resultImage);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        image.release();
    }

    @Override
    protected void onPause(){
        super.onPause();
        image.release();
        finish();
    }


}
