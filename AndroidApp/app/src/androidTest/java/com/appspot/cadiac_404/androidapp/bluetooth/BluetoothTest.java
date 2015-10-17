package com.appspot.cadiac_404.androidapp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by Alan on 10/3/2015.
 */

/*
*Bluetooth must be enabled prior to test, bluetooth server must also be discoverable and running prior to test.
 */
public class BluetoothTest extends ServiceTestCase<BluetoothService> {
    private static int TIMEOUT = 15;
    private static JSONObject EXPECTED_JSON_OBJECT;
    private static JSONObject EXPECTED_JSON_OBJECT_SERIAL;

    static {
        EXPECTED_JSON_OBJECT = new JSONObject();
        EXPECTED_JSON_OBJECT_SERIAL = new JSONObject();
        JSONObject time = new JSONObject();
        try {
            time.put("hour", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            EXPECTED_JSON_OBJECT.put("time", time);
            EXPECTED_JSON_OBJECT.put("accident", false);
            EXPECTED_JSON_OBJECT.put("velocity", 45);
            EXPECTED_JSON_OBJECT.put("relativeVelocity", 10);
            EXPECTED_JSON_OBJECT.put("warning", true);
            EXPECTED_JSON_OBJECT.put("heartRate", 10);

            EXPECTED_JSON_OBJECT_SERIAL.put("hdr", 8);
            EXPECTED_JSON_OBJECT_SERIAL.put("val1", 13);
            EXPECTED_JSON_OBJECT_SERIAL.put("val2", 17);
            EXPECTED_JSON_OBJECT_SERIAL.put("ftr", 21);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BluetoothService btService;
    JSONObject receivedObject;

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
        this.startService(new Intent(getSystemContext(), BluetoothService.class));
        btService = this.getService();
        btService.setCallbacks(callbacks);
    }

    @Override
    public void testServiceTestCaseSetUpProperly() throws Exception {
        super.testServiceTestCaseSetUpProperly();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        assertEquals(true, adapter.isEnabled());//insure bluetooth is enabled
    }

    /*public void test() throws InterruptedException, TimeoutException, JSONException {
        for (int i = 0; i < TIMEOUT; ++i) {
            Thread.sleep(1000);//wait one second
            if (receivedObject != null) {
                assertEquals(EXPECTED_JSON_OBJECT.getJSONObject("time").get("hour"), receivedObject.getJSONObject("time").get("hour"));
                assertEquals(EXPECTED_JSON_OBJECT.get("accident"), receivedObject.get("accident"));
                assertEquals(EXPECTED_JSON_OBJECT.get("velocity"), receivedObject.get("velocity"));
                assertEquals(EXPECTED_JSON_OBJECT.get("relativeVelocity"), receivedObject.get("relativeVelocity"));
                assertEquals(EXPECTED_JSON_OBJECT.get("warning"), receivedObject.get("warning"));
                assertEquals(EXPECTED_JSON_OBJECT.get("heartrate"), receivedObject.get("heartrate"));
                btService.closeConnection();//message recieved close the connection
                return;
            }
        }
        btService.closeConnection();//message recieved close the connection
        throw new TimeoutException("BluetoothTest Time Out");
    }*/

    public void testSerialValues() throws InterruptedException, TimeoutException, JSONException {
        for (int i = 0; i < TIMEOUT; ++i) {
            Thread.sleep(1000);//wait one second
            if (receivedObject != null) {
                assertEquals(EXPECTED_JSON_OBJECT_SERIAL.getJSONObject("hdr"), receivedObject.getJSONObject("hdr"));
                assertEquals(EXPECTED_JSON_OBJECT_SERIAL.get("val1"), receivedObject.get("val1"));
                assertEquals(EXPECTED_JSON_OBJECT_SERIAL.get("val2"), receivedObject.get("val2"));
                assertEquals(EXPECTED_JSON_OBJECT_SERIAL.get("ftr"), receivedObject.get("ftr"));
                if (btService != null)
                    btService.closeConnection();//message recieved close the connection
                return;
            }
        }
        btService.closeConnection();//message recieved close the connection
        throw new TimeoutException("BluetoothTest Time Out");
    }

    private BluetoothInterface callbacks = new BluetoothInterface() {
        @Override
        public void handleReceivedMessages(String jsonString) {
            if (jsonString == null) {
                logError("Can not handle string, string is null");
            } else {
                JSONTokener tokener = new JSONTokener(jsonString);
                try {
                    receivedObject = (JSONObject) tokener.nextValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                    assert false;
                }
            }

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
