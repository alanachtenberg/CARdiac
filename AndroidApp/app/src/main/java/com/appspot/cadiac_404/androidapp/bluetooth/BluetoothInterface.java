package com.appspot.cadiac_404.androidapp.bluetooth;

/**
 * Created by Alan on 10/2/2015.
 */
public interface BluetoothInterface {

    void handleReceivedMessages(String jsonString);

    void logMessage(String message);

    void logError(String message);

}
