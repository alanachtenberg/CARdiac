package com.appspot.cadiac_404.androidapp;

import org.json.JSONException;
import org.json.JSONObject;

import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.VehicleBean;

/**
 * Created by Alan on 12/2/2015.
 */
public class CommandParser {

    public enum TYPE {
        ECG, VEHICLE, UNKNOWN
    }

    public CommandParser() {

    }

    public static TYPE parseCommand(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            String typeString = jsonObject.getString("TYPE");
            if (typeString.toLowerCase().contains("ecg")){
                return TYPE.ECG;
            }else if(typeString.toLowerCase().contains("vehicle")){
                return TYPE.VEHICLE;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return TYPE.UNKNOWN;
        }
        return TYPE.UNKNOWN;
    }
    public static ECGBean parseECGString(String jsonString){
        ECGBean bean = new ECGBean();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Double heartRate = jsonObject.getDouble("HEART_RATE");
            Boolean missedBeat = jsonObject.getBoolean("MISSED_BEAT");
            Boolean lowVoltPeak = jsonObject.getBoolean("LOW_VOLT_PEAK");
            Double lowVoltVal = jsonObject.getDouble("LOW_VOLT_VAL");

            bean.setHeartRate(heartRate);
            bean.setMissedBeat(missedBeat);
            bean.setLowVoltPeak(lowVoltPeak);
            bean.setLowVoltValue(lowVoltVal);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }
    public static VehicleBean parseVehicleString(String jsonString){
        VehicleBean bean = new VehicleBean();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Boolean collision = jsonObject.getBoolean("COLLISION");
            Double velocity = jsonObject.getDouble("VELOCITY");
            bean.setCollision(collision);
            bean.setVelocity(velocity);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }
}
