package com.appspot.cardiac_404.Vehicle;

import com.appspot.cardiac_404.TimeBean;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by Alan on 10/31/2015.
 */
public class VehicleBean {

    private Boolean collision;
    private float velocity;
    private TimeBean time;

    public Boolean getCollision() {
        return collision;
    }

    public void setCollision(Boolean collision) {
        this.collision = collision;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }


    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }
}
