package com.appspot.cardiac_404.ECG;

import java.io.Serializable;

/**
 * Created by Alan on 10/6/2015.
 */
public class ECGBean {
    private int heartRate;
    private boolean problemOne;
    private boolean problemTwo;
    private boolean problemThree;

    public ECGBean(){

    }
    public ECGBean(int heartRate, boolean problemOne, boolean problemTwo, boolean problemThree)
    {
        this.heartRate=heartRate;
        this.problemOne=problemOne;
        this.problemTwo=problemTwo;
        this.problemThree=problemThree;
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
}
