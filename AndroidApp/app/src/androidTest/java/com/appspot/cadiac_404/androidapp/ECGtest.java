package com.appspot.cadiac_404.androidapp;

import android.test.AndroidTestCase;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Calendar;

import cardiac_404.appspot.com.cardiacApi.CardiacApi;
import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.ECGBeanCollection;
import cardiac_404.appspot.com.cardiacApi.model.TimeBean;

/**
 * Created by Alan on 10/18/2015.
 */

public class ECGtest extends AndroidTestCase {

    String ROOT_URL = "https://cardiac-404.appspot.com/_ah/api/";
    String APP_NAME = "CARdiac";
    CardiacApi endpointApi;
    TimeBean time = new TimeBean();

    Integer EXPECTED_HEART_RATE = 100;
    Boolean EXPECTED_PROB_1 = true;
    Boolean EXPECTED_PROB_2 = true;
    Boolean EXPECTED_PROB_3 = false;


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
        ecgObject.setProblemOne(EXPECTED_PROB_1);
        ecgObject.setProblemTwo(EXPECTED_PROB_2);
        ecgObject.setProblemThree(EXPECTED_PROB_3);
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
        assertEquals(EXPECTED_PROB_1, ecgObject.getProblemOne());
        assertEquals(EXPECTED_PROB_2, ecgObject.getProblemTwo());
        assertEquals(EXPECTED_PROB_3, ecgObject.getProblemThree());

    }
}
