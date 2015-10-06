package com.appspot.cadiac_404.androidapp.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends Service {
    public static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static String LOGGER_TAG = "Bluetooth";
    private SharedPreferences mSP;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private String targetDeviceName = "default";
    private Boolean targetDevicePaired = false;
    private BluetoothDevice targetDevice;
    private BtCommThread btCommThread;

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
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
        if (btCommThread == null) {
            Toast.makeText(this, "BluetoothComm Service Started", Toast.LENGTH_SHORT).show();
            retrievePreferences();//gets target device name
            targetDevicePaired = checkForTargetDevice();
            if (!targetDevicePaired) {
                discoverDevice();//initiate discovery
            } else if (btCommThread == null || !btCommThread.isAlive()) {
                try {
                    btCommThread = new BtCommThread(targetDevice, mCallbacks);
                    btCommThread.start();//
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
                btCommThread.close();//closes socket connection to device, must be done to cancel any blocking reads that might prevent interruption
                btCommThread.join();//wait for thread to complete execution

            }
            Toast.makeText(this, "Bluetooth Comm Service Destroyed", Toast.LENGTH_SHORT).show();
            this.stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Boolean checkForTargetDevice() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Check if our device is paired already
                // Check if our device is paired already
                device.fetchUuidsWithSdp();//refresh uuid list
                ParcelUuid[] parcelUuid = device.getUuids();
                for (int i = 0; i < parcelUuid.length; ++i) {
                    if (parcelUuid[i].getUuid().equals(uuid)) {//target device contains our uuid
                        targetDevice = device;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void discoverDevice() {
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }

    private void retrievePreferences() {
        targetDeviceName = mSP.getString("bt_device_name", "udoo");
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
                    if (btCommThread == null || btCommThread.isAlive() == false) {
                        try {
                            btCommThread = new BtCommThread(targetDevice, mCallbacks);
                            btCommThread.start();//start connection thread
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            btCommThread = null;//must have been a problem with Thread constructor, so get rid of instance
                            Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show();
                            stopSelf();//stop the bluetooth service, let user have a chance to update settings
                        }
                    }
                }
            }
        }
    };

    BluetoothInterface mCallbacks = new BluetoothInterface() {
        @Override
        public void handleReceivedMessages(String jsonString) {
            //TODO parse received json object
            Log.i(LOGGER_TAG, jsonString);
        }

        @Override
        public void logMessage(String message) {
            Log.d(LOGGER_TAG, message);
        }

        @Override
        public void logError(String message) {
            Log.e(LOGGER_TAG, message);
        }
    };

    /*
    * Used for unit test
     */
    public void setCallbacks(BluetoothInterface callbacks) {
        if (btCommThread != null) {
            btCommThread.setCallbacks(callbacks);
        }
    }
}
