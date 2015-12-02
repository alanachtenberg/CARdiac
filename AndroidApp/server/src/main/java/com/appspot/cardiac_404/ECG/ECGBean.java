package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.TimeLocBean;
import com.googlecode.objectify.annotation.Subclass;

import java.io.Serializable;

/**
 * Created by Alan on 10/6/2015.
 */

@Subclass
public class ECGBean implements Serializable{
    private TimeLocBean time;
    private float heartRate;
    private boolean missedBeat;
    private boolean lowVoltPeak;
    private float lowVoltValue;

    public ECGBean() {

    }

    public ECGBean( TimeLocBean time, float heartRate, boolean missedBeat, boolean lowVoltPeak, float lowVoltValue) {
        this.time = time;
        this.heartRate = heartRate;
        this.missedBeat = missedBeat;
        this.lowVoltPeak = lowVoltPeak;
        this.lowVoltValue = lowVoltValue;
    }

    public TimeLocBean getTime() {
        return time;
    }

    public void setTime(TimeLocBean time) {
        this.time = time;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public boolean getMissedBeat() {
        return missedBeat;
    }

    public void setMissedBeat(boolean missedBeat) {
        this.missedBeat = missedBeat;
    }

    public boolean isLowVoltPeak() {
        return lowVoltPeak;
    }

    public void setLowVoltPeak(boolean lowVoltPeak) {
        this.lowVoltPeak = lowVoltPeak;
    }

    public float getLowVoltValue() {
        return lowVoltValue;
    }

    public void setLowVoltValue(float lowVoltValue) {
        this.lowVoltValue = lowVoltValue;
    }
}
