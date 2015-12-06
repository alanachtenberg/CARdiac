package com.appspot.cardiac_404.User;

import com.appspot.cardiac_404.ECG.ECGBean;
import com.appspot.cardiac_404.TimeLocBean;
import com.appspot.cardiac_404.Vehicle.VehicleBean;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

/**
 * Created by Alan on 11/22/2015.
 */

@Entity
public class CardiacUser {
    @Id
    private String email;
    private Boolean monitor;//monitoring permissions

    private ArrayList<ECGBean> ecgData;
    private ArrayList<VehicleBean> vehicleData;

    public CardiacUser() {

    }

    public CardiacUser(User user) {
        email = user.getEmail();
        monitor = false;
        ecgData = new ArrayList<ECGBean>();
        vehicleData = new ArrayList<VehicleBean>();
        ecgData.add(new ECGBean(new TimeLocBean(), -1, false, false, 0));
        vehicleData.add(new VehicleBean(new TimeLocBean(), false, -1f));
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<ECGBean> getEcgData() {
        return ecgData;
    }

    public ArrayList<VehicleBean> getVehicleData() {
        return vehicleData;
    }

    public Boolean isMonitor() {
        return monitor;
    }

    public void toggleMonitor() {
        monitor = !monitor;
    }
}
