package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.TimeBean;

/**
 * Created by Alan on 10/6/2015.
 */
public class ECGBean {
    private String id;
    private int heartRate;
    private boolean problemOne;
    private boolean problemTwo;
    private boolean problemThree;
    private TimeBean time;

    public ECGBean() {

    }

    public ECGBean(String id, int heartRate, boolean problemOne, boolean problemTwo, boolean problemThree) {
        this.id = id;
        this.heartRate = heartRate;
        this.problemOne = problemOne;
        this.problemTwo = problemTwo;
        this.problemThree = problemThree;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public boolean getProblemOne() {
        return problemOne;
    }

    public void setProblemOne(boolean problemOne) {
        this.problemOne = problemOne;
    }

    public boolean getProblemTwo() {
        return problemTwo;
    }

    public void setProblemTwo(boolean problemTwo) {
        this.problemTwo = problemTwo;
    }

    public boolean getProblemThree() {
        return problemThree;
    }

    public void setproblemThree(boolean problemThree) {
        this.problemThree = problemThree;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }
}
