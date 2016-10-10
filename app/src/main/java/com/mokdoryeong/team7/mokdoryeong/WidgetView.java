package com.mokdoryeong.team7.mokdoryeong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
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
        setImageResource(R.drawable.testicon);
    }
}
