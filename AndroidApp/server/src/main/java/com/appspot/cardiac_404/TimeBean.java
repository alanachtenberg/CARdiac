package com.appspot.cardiac_404;

import com.googlecode.objectify.annotation.Subclass;

import java.io.Serializable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import javax.xml.crypto.Data;

/**
 * Created by Alan on 10/30/2015.
 */

@Subclass
public class TimeBean implements Serializable{
    private Date time;

    public TimeBean(){
        time = Calendar.getInstance().getTime();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    public void setTimeFromJson(){
        //TODO convert json fields to time
    }
}
