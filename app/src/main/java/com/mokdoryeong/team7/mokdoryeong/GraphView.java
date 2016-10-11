package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by park on 2016-10-11.
 */
public class GraphView extends ImageView {

    private final int DATA_COUNT = 8;

    private float width;
    private float height;

    private Handler handler;
    private LinkedHashMap<Calendar, Float> dataSet;

    public GraphView(Context context, Handler handler) {
        super(context);
        this.handler = handler;
        initSettings();
    }

    public GraphView(Context context, Handler handler, AttributeSet attrs) {
        super(context, attrs);
        this.handler = handler;
        initSettings();
    }

    public GraphView(Context context, Handler handler, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.handler = handler;
        initSettings();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        width = getWidth();
        height = getHeight();
    }

    private void initSettings(){

        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.MATCH_PARENT));
        this.setBackgroundColor(Color.LTGRAY);

        dataSet = new LinkedHashMap<Calendar, Float>();

        //Graph sample data
        Calendar today = Calendar.getInstance();
        Calendar after1Hour = Calendar.getInstance();
        after1Hour.add(Calendar.HOUR, 1);
        dataSet.put(today, 20.0f);
        dataSet.put(after1Hour, 30.0f);
    }

    protected void onDraw(Canvas canvas){
        Paint paint = new Paint();

        //For bottom axis bar
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);

        canvas.drawLine(0, height * 0.85f, width, height * 0.85f, paint);

        Calendar timeStamp = Calendar.getInstance();
        timeStamp.add(Calendar.HOUR_OF_DAY, -4);

        paint.setTextSize(30.0f);
        paint.setTextAlign(Paint.Align.CENTER);

        float incrementPoint = 0.0f;

        for(int i = 0; i < 6; ++i){
            if(i >= 1 && i <= 4) {

                canvas.drawText(String.valueOf(timeStamp.get(Calendar.HOUR_OF_DAY)) + "시",
                        width * incrementPoint, height * 0.95f, paint);
                canvas.drawCircle(width * incrementPoint, height * 0.95f, 5, paint);
                timeStamp.add(Calendar.HOUR_OF_DAY, 1);
                incrementPoint += 0.24f;
            }else {
                incrementPoint += 0.14f;
            }
        }
        float graphHeight = height * 0.85f;
    }

}
