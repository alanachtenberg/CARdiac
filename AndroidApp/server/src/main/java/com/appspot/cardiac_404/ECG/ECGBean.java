package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.TimeBean;
import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Alan on 10/6/2015.
 */

@Entity
public class ECGBean implements Serializable{
    @Id
    private Long id;
    private TimeBean time;
    private int heartRate;
    private boolean problemOne;
    private boolean problemTwo;
    private boolean problemThree;

    public ECGBean() {

    }

    public ECGBean(Long id, TimeBean time, int heartRate, boolean problemOne, boolean problemTwo, boolean problemThree) {
        this.id = id;
        this.time = time;
        this.heartRate = heartRate;
        this.problemOne = problemOne;
        this.problemTwo = problemTwo;
        this.problemThree = problemThree;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
