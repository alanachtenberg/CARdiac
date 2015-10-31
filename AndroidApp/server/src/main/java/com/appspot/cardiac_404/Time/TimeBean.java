package com.appspot.cardiac_404.Time;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Alan on 10/30/2015.
 */
public class TimeBean implements Serializable{
    private Calendar time;

    public TimeBean(){
        time = Calendar.getInstance();
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
    public void setTimeFromJson(){

        //TODO convert json fields to time
    }
}
