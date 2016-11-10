package com.mokdoryeong.team7.mokdoryeong;

import org.opencv.core.Rect;

/**
 * Created by park on 2016-10-31.
 */

public class OpencvRoutine {
    public static native int[] nonFrontalFaceDetection(long addrRgba, int x1, int y1, int x2, int y2);
}
