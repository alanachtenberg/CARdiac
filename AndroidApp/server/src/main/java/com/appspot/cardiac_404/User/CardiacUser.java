package com.appspot.cardiac_404.User;

import com.appspot.cardiac_404.ECG.ECGBean;
import com.appspot.cardiac_404.TimeBean;
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
    private String id;
    private String email;
    private Boolean monitor;//monitoring permissions

    private ArrayList<ECGBean> ecgData;

    public CardiacUser(){

    }

    public CardiacUser(User user) {
        id = user.getUserId();
        email = user.getEmail();
        monitor = false;
        ecgData = new ArrayList<ECGBean>();
        ecgData.add(new ECGBean(new TimeBean(),-1,false,false,0));
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<ECGBean> getEcgData() {
        return ecgData;
    }

    public Boolean isMonitor() {
        return monitor;
    }

    public void toggleMonitor() {
        monitor = !monitor;
    }
}
