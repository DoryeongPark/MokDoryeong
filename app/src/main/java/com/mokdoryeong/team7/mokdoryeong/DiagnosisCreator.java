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

/**
 * Created by park on 2016-10-31.
 */

public class DiagnosisCreator extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{

    private class FaceDetectionRoutine extends Thread{
        private boolean isAlive = false;

        public void run() {
            isAlive = true;
            while (true) {
                if (isAlive == false)
                    break;
            }
        }
        public void abort(){
            isAlive = true;
        }

        public boolean isRunning(){
            return isAlive;
        }

        public void detectFaceROI(Mat clonedImgFrame){
            int[] result = OpencvRoutine.nonFrontalFaceDetection(clonedImgFrame.getNativeObjAddr(), faceX1, faceY1, faceX2, faceY2);
            Log.d("OpenCV", result[0] + " " + result[1]);

            faceX1 = result[0];
            faceY1 = result[1];
            faceX2 = result[2];
            faceY2 = result[3];

            if(result[0] == 0 && result[2] == 0)
                faceX1 = faceY1 = faceX2 = faceY2 = 0;

        }



     };

    private int faceX1;
    private int faceY1;
    private int faceX2;
    private int faceY2;

    private int measureCounter = 0;
    private int measureInterval = 5;

    private Mat imgFrame;
    private JavaCameraView javaCameraView;

    private FaceDetectionRoutine faceDetectionRoutine = null;


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

        faceX1 = faceY1 = faceX2 = faceY2 = 0;

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();

        if(faceDetectionRoutine != null) {
            faceDetectionRoutine.abort();
            faceDetectionRoutine = null;
        }
    }

    @Override
    protected void onDestroy(){
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();

        if(faceDetectionRoutine != null) {
            faceDetectionRoutine.abort();
            faceDetectionRoutine = null;
        }
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

        if(faceDetectionRoutine == null) {
            faceDetectionRoutine = new FaceDetectionRoutine();
            faceDetectionRoutine.start();
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


        if(measureCounter == measureInterval){

            if(faceDetectionRoutine.isRunning() == true){
                Mat clonedImgFrame = new Mat();
                imgFrame.copyTo(clonedImgFrame);
                faceDetectionRoutine.detectFaceROI(clonedImgFrame);
            }

            measureCounter = 0;
        }

        //Core.circle(imgFrame, new Point(faceCenterY, imgFrame.rows() - faceCenterX), 4, new Scalar(0, 255, 0), 3);

        Core.rectangle(imgFrame, new Point(faceY1, imgFrame.rows() - faceX1),
                new Point(faceY2, imgFrame.rows() - faceX2), new Scalar(0, 0, 255), 5);

        return imgFrame;
    }
}
