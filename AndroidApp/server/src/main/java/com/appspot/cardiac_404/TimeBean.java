package com.appspot.cardiac_404;

import com.googlecode.objectify.annotation.Subclass;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alan on 10/30/2015.
 */

@Subclass
public class TimeBean implements Serializable{
    private String time;

    public TimeBean(){
        time = Calendar.getInstance().toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setTimeFromJson(){
        //TODO convert json fields to time
    }
}
