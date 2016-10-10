package com.mokdoryeong.team7.mokdoryeong;

import android.os.Handler;

/**
 * Created by park on 2016-10-09.
 */
public class WidgetThread extends Thread{

    private Handler handler;
    boolean isAlive = true;

    public WidgetThread(Handler handler){
        this.handler = handler;
    }

    public void abort(){
        synchronized (this){
            this.isAlive = false;
        }
    }

    public void run(){
        while(isAlive){
            handler.sendEmptyMessage(0);
            try{ Thread.sleep(5000); }catch(Exception e){}
        }
    }
}
