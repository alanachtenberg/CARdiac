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
public class TimeLocBean implements Serializable{
    private Date time;
    private double latitude;
    private double longitude;

    public TimeLocBean(){
        time = Calendar.getInstance().getTime();
    }

    public TimeLocBean(double lattitude, double longitude){
        time = Calendar.getInstance().getTime();
        this.latitude = lattitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
