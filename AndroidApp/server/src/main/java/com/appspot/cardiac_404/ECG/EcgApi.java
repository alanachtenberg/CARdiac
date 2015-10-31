package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.google.api.server.spi.config.ApiMethod;

import java.util.ArrayList;

/**
 * Created by Alan on 9/25/2015.
 */

public class EcgApi extends CARdiacApiBase {
    private static ArrayList<ECGBean> ecgDataList = new ArrayList<ECGBean>();

    static {
        //test data
        ECGBean bean = new ECGBean("TEST DATA", 123, true, false, false);
        ecgDataList.add(bean);
    }

    @ApiMethod(httpMethod = "post")
    public void insertECG(ECGBean data) {
        ecgDataList.add(data);
    }

    @ApiMethod(httpMethod = "get")
    public ArrayList<ECGBean> listECG() {
        return ecgDataList;
    }

}
