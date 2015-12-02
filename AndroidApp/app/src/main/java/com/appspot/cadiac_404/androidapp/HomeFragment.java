package com.appspot.cadiac_404.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appspot.cadiac_404.androidapp.bluetooth.BluetoothInterface;
import com.appspot.cadiac_404.androidapp.bluetooth.BluetoothService;
import com.google.api.client.util.DateTime;

import java.util.Calendar;

import cardiac_404.appspot.com.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.cardiacApi.model.TimeLocBean;
import cardiac_404.appspot.com.cardiacApi.model.VehicleBean;

import static com.appspot.cadiac_404.androidapp.CommandParser.TYPE;
import static com.appspot.cadiac_404.androidapp.CommandParser.parseCommand;
import static com.appspot.cadiac_404.androidapp.CommandParser.parseECGString;
import static com.appspot.cadiac_404.androidapp.CommandParser.parseVehicleString;

public class HomeFragment extends Fragment {
    private View view;
    private Button signInButton;
    private Button connectBlueToothButton;
    private Button connectServerButton;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        //empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        signInButton = (Button) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).signIn();
            }
        });

        connectBlueToothButton = (Button) view.findViewById(R.id.connect_bluetooth_button);
        connectBlueToothButton.setOnClickListener(connectBlueToothListener);

        return view;
    }

    private View.OnClickListener connectBlueToothListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Log.d("CARdiac", "device does not support Bluetooth");// Device does not support Bluetooth
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                StartBluetoothService();
            }
        }
    };

    public Location getLocation(){
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);//
        return lastKnownLocation;
    }

    public void StartBluetoothService() {
        getActivity().startService(new Intent(getActivity(), BluetoothService.class));
        BluetoothService service = (BluetoothService) getActivity().getSystemService(".bluetooth.BluetoothService");//ignore
        service.setCallbacks(new BluetoothInterface() {
            @Override
            public void handleReceivedMessages(String jsonString) {
                TYPE command = parseCommand(jsonString);
                TimeLocBean timeLocBean = new TimeLocBean();
                timeLocBean.setTime(new DateTime(Calendar.getInstance().getTimeInMillis()));
                Location location = getLocation();
                timeLocBean.setLatitude(location.getLatitude());
                timeLocBean.setLongitude(location.getLongitude());
                switch (command){
                    case ECG:
                        ECGBean ecgBean = parseECGString(jsonString);
                        assert ecgBean != null;
                        ecgBean.setTime(timeLocBean);
                        //TODO call api
                        break;
                    case VEHICLE:
                        VehicleBean vehicleBean = parseVehicleString(jsonString);
                        vehicleBean.setTime(timeLocBean);
                        //TODO call api
                        break;
                    case UNKNOWN:
                        Log.e(MainActivity.TAG,"UNKNOWN COMMAND RECIEVED OVER BLUETOOTH");
                        break;
                }
            }

            @Override
            public void logMessage(String message) {
                Log.i(MainActivity.TAG,message);
            }

            @Override
            public void logError(String message) {
                Log.e(MainActivity.TAG,message);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK)
                StartBluetoothService();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}