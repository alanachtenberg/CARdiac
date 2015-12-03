package com.appspot.cadiac_404.androidapp;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import cardiac_404.appspot.com.cardiacApi.CardiacApi;
import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.VehicleBean;

/**
 * Created by Alan on 12/2/2015.
 */
public class ApiCaller {
    CardiacApi endpointsApi;

    public ApiCaller(GoogleAccountCredential credentials) {
        CardiacApi.Builder endpointsBuilder = new CardiacApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), MainActivity.credentials);
        endpointsBuilder.setRootUrl("https://cardiac-404.appspot.com/_ah/api/");
        endpointsBuilder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });

        endpointsBuilder.setApplicationName("CARdiac");
        endpointsApi = endpointsBuilder.build();
    }

    public void insertVehicle(VehicleBean bean) throws IOException {
        endpointsApi.vehicleApi().insertVehicle(bean).execute();
    }

    public void insertEcg(ECGBean bean) throws IOException {
        endpointsApi.ecgApi().insertECG(bean).execute();
    }
}
