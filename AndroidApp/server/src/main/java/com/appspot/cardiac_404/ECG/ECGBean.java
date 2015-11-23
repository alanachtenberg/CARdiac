package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.TimeBean;
import com.googlecode.objectify.annotation.Subclass;

import java.io.Serializable;

/**
 * Created by Alan on 10/6/2015.
 */

@Subclass
public class ECGBean implements Serializable{
    private TimeBean time;
    private int heartRate;
    private boolean problemOne;
    private boolean problemTwo;
    private boolean problemThree;

    public ECGBean() {

    }

    public ECGBean( TimeBean time, int heartRate, boolean problemOne, boolean problemTwo, boolean problemThree) {
        this.time = time;
        this.heartRate = heartRate;
        this.problemOne = problemOne;
        this.problemTwo = problemTwo;
        this.problemThree = problemThree;
    }

    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public boolean isProblemOne() {
        return problemOne;
    }

    public void setProblemOne(boolean problemOne) {
        this.problemOne = problemOne;
    }

    public boolean isProblemTwo() {
        return problemTwo;
    }

    public void setProblemTwo(boolean problemTwo) {
        this.problemTwo = problemTwo;
    }

    public boolean isProblemThree() {
        return problemThree;
    }

    public void setProblemThree(boolean problemThree) {
        this.problemThree = problemThree;
    }


}
