package com.mokdoryeong.team7.mokdoryeong;

import android.util.Log;

import org.opencv.core.Mat;

/**
 * Created by park on 2016-11-10.
 */

public class FaceDetectionRoutine extends Thread {

    private DiagnosisCreator diagnosisCreator;

    private int faceX1 = 0;
    private int faceY1 = 0;
    private int faceX2 = 0;
    private int faceY2 = 0;

    public FaceDetectionRoutine(DiagnosisCreator diagnosisCreator){
        this.diagnosisCreator = diagnosisCreator;
    }

    private boolean isAlive = false;

    public void run() {
        isAlive = true;
        while (true) {

            if (isAlive == false)
                break;

            Mat copiedFrame = diagnosisCreator.getCopiedFrame();
            if(copiedFrame == null)
                continue;
            detectFaceROI(copiedFrame);
            diagnosisCreator.setPoints(faceX1, faceY1, faceX2, faceY2);
            try{ Thread.sleep(250); }catch(Exception e){}

        }
    }
    public void abort(){
        isAlive = false;
    }

    public boolean isRunning(){
        return isAlive;
    }

    private void detectFaceROI(Mat clonedImgFrame){
        int[] result = OpencvRoutine.nonFrontalFaceDetection(clonedImgFrame.getNativeObjAddr(), faceX1, faceY1, faceX2, faceY2);
        Log.d("OpenCV", result[0] + " " + result[1] + " " + result[2] + " " + result[3]);

        faceX1 = result[0];
        faceY1 = result[1];
        faceX2 = result[2];
        faceY2 = result[3];

    }


}
