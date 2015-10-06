package com.appspot.cadiac_404.androidapp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by Alan on 10/3/2015.
 */

/*
*Bluetooth must be enabled prior to test, bluetooth server must also be discoverable and running prior to test.
 */
public class BluetoothTest extends ServiceTestCase<BluetoothService> {
    private static String EXPECTED_MESSAGE;
    private static int TIMEOUT = 15;
    static {
        JSONObject expectedJSONObject = new JSONObject();
        try {
            expectedJSONObject.put("time", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            expectedJSONObject.put("accident", false);
            expectedJSONObject.put("velocity", 45);
            expectedJSONObject.put("relativeVelocity", 10);
            expectedJSONObject.put("warning", true);
            expectedJSONObject.put("heartRate", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EXPECTED_MESSAGE = expectedJSONObject.toString();
    }

    BluetoothService btService;
    String message;
    public BluetoothTest() {
        super(BluetoothService.class);
    }

    /**
     * Constructor
     *
     * @param serviceClass The type of the service under test.
     */
    public BluetoothTest(Class<BluetoothService> serviceClass) {
        super(serviceClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.startService(new Intent(getSystemContext(),BluetoothService.class));
        btService = this.getService();
        btService.setCallbacks(callbacks);
    }
    @Override
    public void testServiceTestCaseSetUpProperly() throws Exception {
        super.testServiceTestCaseSetUpProperly();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        assertEquals(true, adapter.isEnabled());//insure bluetooth is enabled
    }
    public void testMessageRecieved() throws InterruptedException, TimeoutException {
        for (int i =0; i<TIMEOUT;++i){
            Thread.sleep(1000);//wait one second
            if (message!=null){
                assertEquals(EXPECTED_MESSAGE, message);
            }
        }
        throw new TimeoutException("BluetoothTest Time Out");
    }


    private BluetoothInterface callbacks = new BluetoothInterface() {
        @Override
        public void handleReceivedMessages(String jsonString) {
            System.out.println(jsonString);
            message=jsonString;
        }

        @Override
        public void logMessage(String message) {
            Log.d(BluetoothService.LOGGER_TAG, message);
        }

        @Override
        public void logError(String message) {
            Log.e(BluetoothService.LOGGER_TAG, message);
        }
    };

}
