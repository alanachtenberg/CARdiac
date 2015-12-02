package com.appspot.cadiac_404.androidapp;

import android.test.AndroidTestCase;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import cardiac_404.appspot.com.cardiacApi.CardiacApi;
import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.ECGBeanCollection;
import cardiac_404.appspot.com.cardiacApi.model.TimeLocBean;

/**
 * Created by Alan on 10/18/2015.
 */

public class ECGtest extends AndroidTestCase {

    String ROOT_URL = "https://cardiac-404.appspot.com/_ah/api/";
    String APP_NAME = "CARdiac";
    CardiacApi endpointApi;
    TimeLocBean time = new TimeLocBean();

    Float EXPECTED_HEART_RATE = 100f;
    Boolean EXPECTED_1 = true;
    Boolean EXPECTED_2 = true;
    Float EXPECTED_3 = 0f;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        CardiacApi.Builder endpointsBuilder = new CardiacApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        endpointsBuilder.setRootUrl(ROOT_URL);
        endpointsBuilder.setApplicationName(APP_NAME);
        endpointApi = endpointsBuilder.build();
    }

    public void testMain() throws IOException {
        insertECG();
        listECG();
    }

    private void insertECG() throws IOException {
        ECGBean ecgObject = new ECGBean();
        ecgObject.setTime(time);
        ecgObject.setHeartRate(EXPECTED_HEART_RATE);
        ecgObject.setMissedBeat(EXPECTED_1);
        ecgObject.setLowVoltPeak(EXPECTED_2);
        ecgObject.setLowVoltValue(EXPECTED_3);
        endpointApi.ecgApi().insertECG(ecgObject).execute();
        System.out.println("Inserting ECG object");
        System.out.println(ecgObject.toPrettyString());
    }

    private void listECG() throws IOException {
        ECGBeanCollection ecgList = endpointApi.ecgApi().listECG().execute();
        ECGBean ecgObject = null;
        for (ECGBean bean : ecgList.getItems()) {
            if (bean.getTime() != null && bean.getTime().equals(time)) {
                ecgObject = bean;
            }
        }
        System.out.println("Retrieved ECG object");
        System.out.println(ecgObject.toPrettyString());
        assertNotNull(ecgObject);
        assertEquals(time, ecgObject.getTime());
        assertEquals(EXPECTED_HEART_RATE, ecgObject.getHeartRate());
        assertEquals(EXPECTED_1, ecgObject.getMissedBeat());
        assertEquals(EXPECTED_2, ecgObject.getLowVoltPeak());
        assertEquals(EXPECTED_3, ecgObject.getLowVoltValue());
    }
}
