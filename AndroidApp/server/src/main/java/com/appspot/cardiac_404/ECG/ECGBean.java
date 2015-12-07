package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.TimeLocBean;
import com.googlecode.objectify.annotation.Subclass;

import java.io.Serializable;

/**
 * Created by Alan on 10/6/2015.
 */
public class ECGBean implements Serializable {
    private TimeLocBean time;
    private double heartRate;
    private boolean missedBeat;
    private boolean lowVoltPeak;
    private double lowVoltValue;

    public ECGBean() {

    }

    public ECGBean(TimeLocBean time, double heartRate, boolean missedBeat, boolean lowVoltPeak, double lowVoltValue) {
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

    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
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

    public double getLowVoltValue() {
        return lowVoltValue;
    }

    public void setLowVoltValue(double lowVoltValue) {
        this.lowVoltValue = lowVoltValue;
    }

    @Override
    public String toString() {
        return String.format("%s HeartRate:%d MissedBeat:%s LowVoltPeak:%s LowVoltValue: %d",
                time.toString(), heartRate, missedBeat, lowVoltPeak, lowVoltValue);
    }
}
