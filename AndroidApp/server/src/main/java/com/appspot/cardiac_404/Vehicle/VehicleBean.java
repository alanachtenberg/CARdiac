package com.appspot.cardiac_404.Vehicle;

import com.appspot.cardiac_404.TimeLocBean;

/**
 * Created by Alan on 10/31/2015.
 */
public class VehicleBean {

    private boolean collision;
    private double velocity;
    private TimeLocBean time;

    public VehicleBean() {

    }

    public VehicleBean(TimeLocBean time, boolean collision, double velocity) {
        this.collision = collision;
        this.velocity = velocity;
        this.time = time;
    }

    public Boolean getCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }


    public TimeLocBean getTime() {
        return time;
    }

    public void setTime(TimeLocBean time) {
        this.time = time;
    }
}
