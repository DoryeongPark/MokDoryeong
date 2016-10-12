package com.mokdoryeong.team7.mokdoryeong;

import org.joda.time.DateTime;

import java.util.LinkedList;

/**
 * Created by park on 2016-10-12.
 */
public class CervicalDataManager {
    private LinkedList<CervicalData> dataQueue;

    public CervicalDataManager(){
        dataQueue = new LinkedList<CervicalData>();
    }

    public void insert(CervicalData cd){
        dataQueue.offer(cd);
        //Then insert data in database

        if(dataQueue.getFirst().getStartTime().isBefore(DateTime.now().minusHours(12)))
            dataQueue.poll();
        //Then delete data in database
    }
}
