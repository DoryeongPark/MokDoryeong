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

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by park on 2016-10-11.
 */
public class GraphView extends ImageView {

    public static final int MODE_HOUR = 0;
    public static final int MODE_DAY = 1;
    public static final int MODE_WEEK= 2;

    private float width;
    private float height;

    private ArrayList<CervicalData> dataSet;

    private int currentMode = MODE_HOUR;

    public GraphView(Context context) {
        super(context);
        initSettings();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSettings();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSettings();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        width = getWidth();
        height = getHeight();
    }

    private void initSettings(){
        dataSet = new ArrayList<CervicalData>();
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.MATCH_PARENT));
        this.setBackgroundColor(Color.LTGRAY);
    }

    private void cutTimeSpan(ArrayList<CervicalData> dataSet){
        this.dataSet.clear();
        DateTime graphStartTime;

        if(currentMode == MODE_HOUR)
            graphStartTime = DateTime.now().minusHours(6);
        else if(currentMode == MODE_DAY)
            graphStartTime = DateTime.now().minusDays(6);
        else
            graphStartTime = DateTime.now().minusWeeks(6);

        for(CervicalData c : dataSet){
            if(c.getStartTime().isBefore(graphStartTime))
                break;
            this.dataSet.add(c);
        }
    }

    public void update(ArrayList<CervicalData> dataSet){
        cutTimeSpan(dataSet);
        invalidate();
    }

    public void setMode(int mode){
        this.currentMode = mode;
    }

    protected void onDraw(Canvas canvas){
        if(width == 0 || height == 0)
            return;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);

        //For bottom axis bar
        float graphHeight = height * 0.85f;
        canvas.drawLine(0, graphHeight, width, graphHeight, paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);

        //Draw data as view
        if(currentMode == MODE_HOUR){

        }else if(currentMode == MODE_DAY){

        }else{

        }
    }

}
