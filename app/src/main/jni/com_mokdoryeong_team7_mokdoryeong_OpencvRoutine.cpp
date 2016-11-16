#include "com_mokdoryeong_team7_mokdoryeong_OpencvRoutine.h"
JNIEXPORT jintArray JNICALL Java_com_mokdoryeong_team7_mokdoryeong_OpencvRoutine_nonFrontalFaceDetection(JNIEnv* env, jclass jcls, jlong addrRgba,
        jint x1, jint y1, jint x2, jint y2){

    Mat& frame = *(Mat*) addrRgba;

    int frameCols = frame.cols;
    int frameRows = frame.rows;

    int faceX1 = (int)x1 / 3;
    int faceY1 = (int)y1 / 3;
    int faceX2 = (int)x2 / 3;
    int faceY2 = (int)y2 / 3;

    try {
        resize(frame, frame, Size(frameCols / 3, frameRows / 3), 0, 0, CV_INTER_NN);
    }catch(Exception e){

        faceX1 *= 3; faceY1 *= 3; faceX2 *= 3; faceY2 *= 3;
        jintArray facePoint = env->NewIntArray(4);
        jint points[4] = {(jint)faceX1, (jint)faceY1, (jint)faceX2, (jint)faceY2};
        env->SetIntArrayRegion(facePoint, 0, 4, points);
        return facePoint;

    }

    detect(frame, faceX1, faceY1, faceX2, faceY2);

    //Returns image matrix original size
    transpose(frame, frame);
    flip(frame, frame, 0);
    resize(frame, frame, Size(frameCols, frameRows), 0, 0, CV_INTER_NN);

    //Transfer ROI points to java
    faceX1 *= 3; faceY1 *= 3; faceX2 *= 3; faceY2 *= 3;
    jintArray facePoint = env->NewIntArray(4);
    jint points[4] = {(jint)faceX1, (jint)faceY1, (jint)faceX2, (jint)faceY2};
    env->SetIntArrayRegion(facePoint, 0, 4, points);
    return facePoint;

}

void detect(Mat& frame, int& x1, int& y1, int& x2, int& y2){

    transpose(frame, frame);
    flip(frame, frame, 1);

    String face_cascade_name = "/storage/emulated/0/data/lbpcascade_profileface.xml";

    CascadeClassifier face_cascade;

    if( !face_cascade.load( face_cascade_name ) ){ printf("--(!)Error loading\n"); return; };

    std::vector<Rect> faces;
    Rect elect;
    bool checkExistance = false;

    Mat frame_gray;
    cvtColor( frame, frame_gray, CV_BGR2GRAY );
    equalizeHist( frame_gray, frame_gray );

    //-- Detect faces
    face_cascade.detectMultiScale( frame_gray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

    for( size_t i = 0; i < faces.size(); i++ )
    {
        Point center( faces[i].x + faces[i].width * 0.5, faces[i].y + faces[i].height * 0.5 );

        int currentX1 = faces[i].x;
        int currentY1 = faces[i].y;
        int currentX2 = faces[i].x + faces[i].width;
        int currentY2 = faces[i].y + faces[i].height;

        if( (x1 > currentX1 - 40 && x1 < currentX1 + 40 && y1 > currentY1 - 40 && y1 < currentY1 + 40) ||
                (checkExistance == false && i == faces.size() - 1)){

//            rectangle( frame,
//                       Point(faces[i].x, faces[i].y),
//                       Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height),
//                       Scalar(255, 0, 0), 2, 8, 0);

            elect = faces[i];

            x1 = elect.x;               y1 = elect.y;
            x2 = elect.x + elect.width; y2 = elect.y + elect.height;

            checkExistance = true;
            break;
        }

    }


    if(checkExistance == false)
        x1 = y1 = x2 = y2 = 0;

}