package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by park on 2016-10-10.
 */
public class WidgetView extends ImageView {

    private float angle = 0.0f;

    public WidgetView(Context context) {
        super(context);
        initiateSettings();
    }

    public WidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initiateSettings();
    }

    public WidgetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initiateSettings();
    }

    private void initiateSettings(){
        setBackgroundColor(Color.RED);
    }

    protected void onDraw(Canvas c){

    }

    public void update(float angle){
        if(angle > 80.0f){
            setBackgroundColor(Color.rgb(17, 255, 23));
        }else if(angle > 70.0f){
            setBackgroundColor(Color.rgb(119, 255, 23));
        }else if(angle > 60.0f){
            setBackgroundColor(Color.rgb(176, 255, 23));
        }else if(angle > 50.0f){
            setBackgroundColor(Color.rgb(255, 255, 23));
        }else if(angle > 40.0f){
            setBackgroundColor(Color.rgb(255, 210, 23));
        }else if(angle > 30.0f){
            setBackgroundColor(Color.rgb(255, 130, 23));
        }else if(angle > 20.0f){
            setBackgroundColor(Color.rgb(255, 60, 23));
        }else if(angle > 10.0f){
            setBackgroundColor(Color.rgb(255, 0, 23));
        }else{
            setBackgroundColor(Color.rgb(17, 255, 23));
        }
    }




}
