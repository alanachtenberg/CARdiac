package com.appspot.cardiac_404.Vehicle;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.ECG.ECGBean;
import com.appspot.cardiac_404.Time.TimeBean;
import com.google.api.server.spi.config.ApiMethod;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Alan on 10/30/2015.
 */
public class VehicleApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "get")
    public TimeBean test() {
        return new TimeBean();
    }

}
