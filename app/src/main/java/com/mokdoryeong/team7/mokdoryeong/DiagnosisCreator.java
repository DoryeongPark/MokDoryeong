package com.mokdoryeong.team7.mokdoryeong;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by park on 2016-10-31.
 */

public class DiagnosisCreator extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{

    private int faceCenterX;
    private int faceCenterY;

    private int measureCounter = 0;
    private int measureInterval = 5;

    private Mat imgFrame;
    private JavaCameraView javaCameraView;

    static{
        System.loadLibrary("MyOpencvLibs");
    }


    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        faceCenterX = faceCenterY = 0;

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy(){
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "OpenCV successfully loaded");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.d("OpenCV", "OpenCV not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        imgFrame = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        imgFrame.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        ++measureCounter;
        imgFrame = inputFrame.rgba();

       if(measureCounter == measureInterval) {
            int[] result = OpencvRoutine.nonFrontalFaceDetection(imgFrame.getNativeObjAddr(), faceCenterX, faceCenterY);
            measureCounter = 0;

              Log.d("OpenCV", result[0] + " " + result[1]);

           faceCenterX = result[0];
           faceCenterY = result[1];

           if(result[0] == 0 && result[1] == 0)
               faceCenterX = faceCenterY = 0;

       }

        Core.circle(imgFrame, new Point(faceCenterY, imgFrame.rows() - faceCenterX), 4, new Scalar(0, 255, 0), 3);


        return imgFrame;
    }
}
