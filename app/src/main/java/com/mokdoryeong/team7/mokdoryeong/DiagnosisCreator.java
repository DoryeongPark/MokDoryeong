package com.mokdoryeong.team7.mokdoryeong;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import android.graphics.Rect;
import org.opencv.core.Scalar;

import java.util.ArrayList;

/**
 * Created by park on 2016-10-31.
 */
public class DiagnosisCreator extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{

    private ArrayList<Rect> roiCandidates;

    private int faceX1;
    private int faceY1;
    private int faceX2;
    private int faceY2;

    private FrameLayout mainLayout;

    private Mat imgFrame;
    private JavaCameraView javaCameraView;

    private ImageView targetView = null;
    private View guideView = null;
    private View progressView = null;

    private Rect faceDetectionArea = null;

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

    private void loadTargetView(){
        if(targetView != null){
            mainLayout.removeView(targetView);
        }

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int targetViewWidth = (int)((float)dm.heightPixels * 0.8f);
        targetView = (ImageView)findViewById(R.id.target_view);
        targetView.setLayoutParams(new FrameLayout.LayoutParams(targetViewWidth, targetViewWidth));
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)targetView.getLayoutParams();
        margin.setMargins((int)((float)(dm.widthPixels - targetViewWidth) / 2f),
                          (int)((float)(dm.heightPixels - targetViewWidth) / 2f), 0, 0);
        targetView.setLayoutParams(margin);

    }

    private void loadGuideView(){
        if(guideView != null)
            mainLayout.removeView(guideView);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int guideViewWidth = (int)((float)dm.widthPixels * 0.16f);
        int guideViewHeight= (int)((float)dm.heightPixels);
        guideView = (View)findViewById(R.id.guide_view);
        guideView.setLayoutParams(new FrameLayout.LayoutParams(guideViewWidth, guideViewHeight));

    }

    private void loadProgressView(){
        if(progressView != null)
            mainLayout.removeView(progressView);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int progressViewWidth = (int)((float)dm.widthPixels * 0.16f);
        int progressViewHeight= (int)((float)dm.heightPixels);
        progressView = (View)findViewById(R.id.progress_view);
        progressView.setLayoutParams(new FrameLayout.LayoutParams(progressViewWidth, progressViewHeight));
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)progressView.getLayoutParams();
        margin.setMargins((int)((float)(dm.widthPixels - progressViewWidth)), 0, 0, 0);
        progressView.setLayoutParams(margin);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        faceX1 = faceY1 = faceX2 = faceY2 = 0;

        mainLayout = (FrameLayout)findViewById(R.id.diagnosis_mainlayout);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        loadTargetView();
        loadGuideView();
        loadProgressView();

        roiCandidates = new ArrayList<Rect>();
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        int[] location = new int[2];
        targetView.getLocationInWindow(location);
        faceDetectionArea = new Rect(location[0], location[1],
                location[0] + targetView.getWidth(), location[1] + targetView.getHeight());
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(faceDetectionRoutine != null) {
            faceDetectionRoutine.abort();
            faceDetectionRoutine = null;
        }

        if(javaCameraView != null)
            javaCameraView.disableView();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(faceDetectionRoutine != null) {
            faceDetectionRoutine.abort();
            faceDetectionRoutine = null;
        }

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

        if(faceDetectionRoutine == null) {
            faceDetectionRoutine = new FaceDetectionRoutine(this);
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
        imgFrame = inputFrame.rgba();

        if(faceDetectionArea.contains(faceX1, faceY1, faceX2, faceY2))
            Core.rectangle(imgFrame, new Point(faceX1, faceY1),
                    new Point(faceX2, faceY2), new Scalar(0, 255, 255), 4);

        return imgFrame;
    }

    public Mat getCopiedFrame() {
        if(imgFrame == null)
            return null;
        Mat copiedFrame = new Mat();
        imgFrame.copyTo(copiedFrame);
        return copiedFrame;
    }

    public void setPoints(int x1, int y1, int x2, int y2){
        faceX1 = y1; faceY1 = imgFrame.rows() - x1;
        faceX2 = y2; faceY2 = imgFrame.rows() - x2;

        if(faceX1 == 0 && faceX2 == 0)
            return;
        //First condition - Is detected ROI located at appropriate face area
        if(faceDetectionArea.contains(faceX1, faceY1, faceX2, faceY2)) {
            if (roiCandidates.isEmpty()) {
                roiCandidates.add(new Rect(faceX1, faceY1, faceX2, faceY2));
                return;
            }else{//Second condition - Is detected ROI almost matching with first one
                int faceCenterX = (faceX1 + faceX2) / 2;
                int faceCenterY = (faceY1 + faceY2) / 2;
                int standardCenterX = roiCandidates.get(0).centerX();
                int standardCenterY = roiCandidates.get(0).centerY();
                if(faceCenterX > standardCenterX - 30 && faceCenterX < standardCenterX + 30 &&
                        faceCenterY > standardCenterY - 30 && faceCenterY < standardCenterY + 30) {
                    roiCandidates.add(new Rect(faceX1, faceY1, faceX2, faceY2));
                    Log.d("OpenCV", roiCandidates.size() + "");
                }else{
                    roiCandidates.clear();
                }
            }
        }else{
            return;
        }
    }

    private Bitmap rotateImage(Bitmap src, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

}
