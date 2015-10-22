package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.Constants;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.ArrayList;

/**
 * Created by Alan on 9/25/2015.
 */

@Api(name = Constants.API_NAME,
        version = Constants.VERSION,
        namespace = @ApiNamespace(ownerDomain = Constants.DOMAIN, ownerName = Constants.DOMAIN,
                packagePath = "ECG"),
        clientIds = {Constants.WEB_CLIENT_ID,Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID})
public class ECG {
    private ArrayList<ECGBean> ecgDataList = new ArrayList<ECGBean>();

    @ApiMethod(name = "ECG.insertECG",httpMethod = "post")
    public void insertECG(ECGBean data) {
        ecgDataList.add(data);
    }

    @ApiMethod(httpMethod = "get")
    public ArrayList<ECGBean> listECG() {
        return ecgDataList;
    }

}
