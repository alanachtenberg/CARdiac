package com.appspot.cadiac_404.androidapp.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.appspot.cadiac_404.androidapp.ApiCaller;
import com.appspot.cadiac_404.androidapp.MainActivity;
import com.google.api.client.util.DateTime;

import com.appspot.cadiac_404.androidapp.CommandParser;

import java.io.IOException;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.TimeLocBean;
import cardiac_404.appspot.com.cardiacApi.model.VehicleBean;

public class BluetoothService extends Service {
    public static UUID uuid = UUID.fromString("1afe39b3-2c5c-4bf4-a2c2-267ee767fd9d");
    public static String LOGGER_TAG = "Bluetooth";
    private static int NUM_RETRIES = 10; //number of retries to connect bluetooth socket
    private int tryCount = 0;
    private SharedPreferences mSP;
    private BluetoothAdapter mBluetoothAdapter;
    private String targetDeviceName = "default";
    private Boolean targetDevicePaired = false;
    private BluetoothDevice targetDevice;
    private BtCommThread btCommThread;
    private Service thisService;

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        tryCount = 0;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mSP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        // Register the BroadcastReceiver for when a bluetooth device is found
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(deviceFoundReceiver, filter); // Don't forget to unregister during onDestroy
        filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(actionStateReciever, filter);
        Log.d(LOGGER_TAG, "Bluetooth Comm Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thisService = this;
        tryCount = 0;
        if (btCommThread == null) {
            Toast.makeText(this, "BluetoothComm Service Started", Toast.LENGTH_SHORT).show();
            retrievePreferences();//gets target device name
            targetDevicePaired = checkForTargetDevice();
            if (!targetDevicePaired) {
                discoverDevice();//initiate discovery
            } else if (btCommThread == null || !btCommThread.isAlive()) {
                try {
                    attemptConnection();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    btCommThread = null;//must have been a problem with Thread constructor, so get rid of instance
                    Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
                    stopSelf();//stop the bluetooth service, let user have a chance to update settings
                }
            } else {
                Toast.makeText(this, "Bluetooth Device Already Connected", Toast.LENGTH_LONG).show();
            }
        /*START_NOT_STICKY Tells os to not recreate service if it is stopped in the event to free memmory.
        * In other words, we will let the user trigger starting a new service in the event
        * that it is stopped
         */
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            try {
                unregisterReceiver(deviceFoundReceiver);
                unregisterReceiver(actionStateReciever);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();//ignore for unit test
            }
            //dispose of connection thread
            if (btCommThread != null) {
                btCommThread.interrupt();//signals for thread to stop execution as soon as possible
                closeConnection();//closes socket connection to device, must be done to cancel any blocking reads that might prevent interruption
                btCommThread.join();//wait for thread to complete execution
            }
            Toast.makeText(this, "Bluetooth Comm Service Destroyed", Toast.LENGTH_SHORT).show();
            this.stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void attemptConnection() throws InterruptedException, IOException {
        try {
            btCommThread = new BtCommThread(targetDevice, mCallbacks);
            btCommThread.start();//start connection thread
        } catch (IOException e) {
            if (tryCount < NUM_RETRIES) {
                ++tryCount;
                Log.d(LOGGER_TAG, String.format("Attempted connection failed retrying connection, retries = %d", tryCount));
                attemptConnection();
            } else {
                Log.e(LOGGER_TAG, String.format("Could not open connection, retries = %d", NUM_RETRIES));
                throw e;
            }

        }
    }

    private Boolean checkForTargetDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Check if our device is paired already
                // Check if our device is paired already
                device.fetchUuidsWithSdp();//refresh uuid list
                ParcelUuid[] parcelUuid = device.getUuids();
                for (ParcelUuid aParcelUuid : parcelUuid) {
                    if (aParcelUuid.getUuid().equals(uuid)) {//target device contains our uuid
                        targetDevice = device;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void discoverDevice() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    private void retrievePreferences() {
        targetDeviceName = mSP.getString("bt_device_name", "UDOO");
    }


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // determine if device is our desired target
                if (device != null && device.getName() != null && device.getName().equals(targetDeviceName)) {
                    targetDevice = device;
                    targetDevicePaired = targetDevice.createBond();
                }
            }
        }
    };

    private final BroadcastReceiver actionStateReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                if (intent.getParcelableExtra(BluetoothDevice.EXTRA_BOND_STATE).equals(BluetoothDevice.BOND_BONDED)
                        && intent.getParcelableExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE).equals(BluetoothDevice.BOND_BONDING)) {
                    if (btCommThread == null || !btCommThread.isAlive()) {
                        try {
                            attemptConnection();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            btCommThread = null;
                            Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show();
                            stopSelf();//stop the bluetooth service, let user have a chance to update settings
                        }
                    }
                }
            }
        }
    };

    public void sendMessage(String message) {
        if (btCommThread != null) {
            btCommThread.write(message);
        }
    }

    public void closeConnection() {
        if (btCommThread != null) {
            btCommThread.close();
        }
    }


    /*
    * Used for unit test
     */
    public void setCallbacks(BluetoothInterface callbacks) {
        if (btCommThread != null) {
            btCommThread.setCallbacks(callbacks);
        }
    }

    private Location getLocation() {
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        return lastKnownLocation;
    }

    private TimeLocBean createTimeBean() {
        TimeLocBean bean = new TimeLocBean();
        Location location = getLocation();
        bean.setLatitude(location.getLatitude());
        bean.setLongitude(location.getLongitude());
        DateTime now = new DateTime(Calendar.getInstance().getTimeInMillis());
        bean.setTime(now);
        return bean;
    }

    private void showText(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


    private BluetoothInterface mCallbacks = new BluetoothInterface() {
        @Override
        public void handleReceivedMessages(String jsonString) {
            CommandParser.TYPE type = CommandParser.parseCommand(jsonString);
            switch (type) {
                case ECG:
                    ECGBean ecgBean = CommandParser.parseECGString(jsonString);
                    ecgBean.setTime(createTimeBean());
                    //showText("Sending data to Ecg API");
                    try {
                        Log.i(LOGGER_TAG,"Sending data to ECG API");
                        Log.i(LOGGER_TAG,ecgBean.toPrettyString());
                        MainActivity.apiCaller.insertEcg(ecgBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case VEHICLE:
                    VehicleBean vehicleBean = CommandParser.parseVehicleString(jsonString);
                    vehicleBean.setTime(createTimeBean());
                    //showText("Sending data to Vehicle API");
                    try {
                        Log.i(LOGGER_TAG,"Sending data to ECG API");
                        Log.i(LOGGER_TAG,vehicleBean.toPrettyString());
                        MainActivity.apiCaller.insertVehicle(vehicleBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case UNKNOWN:
                    throw new IllegalStateException("unknown  command type");
            }

        }

        @Override
        public void logMessage(String message) {
            Log.i(LOGGER_TAG, message);
        }

        @Override
        public void logError(String message) {
            Log.e(LOGGER_TAG, message);
        }
    };


}
