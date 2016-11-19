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
        Imgproc.Canny(image, image, 80, 80);
        Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 9), new Point(3, 5));
        Imgproc.morphologyEx(image, image, Imgproc.MORPH_DILATE, structuringElement);

        OpencvRoutine.detectNeckPoints(image.getNativeObjAddr());

        diagnosisView = (ImageView)findViewById(R.id.diagnosis_view);
        Bitmap resultImage = Bitmap.createBitmap(image.cols(), image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, resultImage);
        diagnosisView.setImageBitmap(resultImage);

    }

    private Mat getLabels(Mat inputArray){

        Mat outputArray = new Mat(inputArray.rows(), inputArray.cols(), CvType.CV_8UC1, new Scalar(0));
        Stack<Point> components = new Stack<Point>();

        for(int i = 0; i < inputArray.cols(); ++i){
            for(int j = 0; j < inputArray.rows(); ++j){
                if(inputArray.get(j, i)[0] == 255.0){
                    double[] data = new double[3];
                    data[0] = 125.0;
                    inputArray.put(j, i, data);
                }

            }
        }

        return outputArray;
    }

}
