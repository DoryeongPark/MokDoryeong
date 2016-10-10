package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by park on 2016-10-10.
 */
public class WidgetView extends ImageView {

    private float angle = 0.0f;

    private WindowManager wm;
    private WindowManager.LayoutParams wParams;

    public WidgetView(Context context, WindowManager wm) {
        super(context);
        this.wm = wm;
        initiateSettings();
    }

    public WidgetView(Context context, WindowManager wm, AttributeSet attrs) {
        super(context, attrs);
        this.wm = wm;
        initiateSettings();
    }

    public WidgetView(Context context, WindowManager wm, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.wm = wm;
        initiateSettings();
    }

    protected void onDraw(Canvas c){

    }

    private void initiateSettings(){
        wParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        wParams.width = 100;
        wParams.height = 100;
        wParams.x = 0;
        wParams.y = 0;
        wm.addView(this, wParams);

        this.setOnTouchListener(new OnTouchListener(){
            float startX = 0.0f;
            float startY = 0.0f;
            int viewX = 0;
            int viewY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch(e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = e.getRawX();
                        startY = e.getRawY();
                        viewX = wParams.x;
                        viewY = wParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int)(e.getRawX() - startX);
                        int y = (int)(e.getRawY() - startY);
                        wParams.x = viewX + x;
                        wParams.y = viewY + y;
                        wm.updateViewLayout(WidgetView.this, wParams);
                        break;
                }
                return true;
            }
        });
    }

    public void update(float angle){
        if(angle > 80.0f){
            setBackgroundColor(Color.rgb(20, 255, 23));
        }else if(angle > 70.0f){
            setBackgroundColor(Color.rgb(100, 255, 23));
        }else if(angle > 60.0f){
            setBackgroundColor(Color.rgb(180, 255, 23));
        }else if(angle > 50.0f){
            setBackgroundColor(Color.rgb(255, 255, 23));
        }else if(angle > 40.0f){
            setBackgroundColor(Color.rgb(255, 190, 23));
        }else if(angle > 30.0f){
            setBackgroundColor(Color.rgb(255, 125, 23));
        }else if(angle > 20.0f){
            setBackgroundColor(Color.rgb(255, 60, 23));
        }else if(angle > 10.0f){
            setBackgroundColor(Color.rgb(255, 0, 23));
        }else{
            setBackgroundColor(Color.rgb(20, 255, 23));
        }
    }

    public void setWidgetSize(int w, int h){
        wParams.width = w;
        wParams.height = h;
        wm.updateViewLayout(this, wParams);
    }

    public void setWidgetPos(int x, int y){
        wParams.x = x;
        wParams.y = y;
        wm.updateViewLayout(this, wParams);
    }

}
