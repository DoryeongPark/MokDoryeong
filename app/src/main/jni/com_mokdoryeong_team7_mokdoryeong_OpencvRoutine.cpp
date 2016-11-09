#include "com_mokdoryeong_team7_mokdoryeong_OpencvRoutine.h"
JNIEXPORT jintArray JNICALL Java_com_mokdoryeong_team7_mokdoryeong_OpencvRoutine_nonFrontalFaceDetection(JNIEnv* env, jclass jcls, jlong addrRgba,
        jint x, jint y){

    Mat& frame = *(Mat*) addrRgba;
    int frameWidth = frame.cols;
    int frameRows = frame.rows;

    int centerX = (int)x;
    int centerY = (int)y;

    detect(frame, centerX, centerY);

    jintArray facePoint = env->NewIntArray(2);
    jint xy[2] = {(jint)centerX, (jint)centerY};
    env->SetIntArrayRegion(facePoint, 0, 2, xy);
    return facePoint;

}

void detect(Mat& frame, int& x, int& y){

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
        Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
        //ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );

        int currentCenterX = faces[i].x + faces[i].width / 2;
        int currentCenterY = faces[i].y + faces[i].height / 2;

        if( (x > currentCenterX - 40  && x < currentCenterX + 40 && y > currentCenterY - 40 && y < currentCenterY + 40) ||
                (checkExistance == false && i == faces.size() - 1)){

            rectangle( frame,
                       Point(faces[i].x, faces[i].y),
                       Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height),
                       Scalar(255, 0, 0), 2, 8, 0);

            x = currentCenterX;
            y = currentCenterY;

            elect = faces[i];
            checkExistance = true;
            break;
        }

    }


    if(checkExistance == false)
        x = y = 0;

    transpose(frame, frame);
    flip(frame, frame, 0);

}