package com.appspot.cadiac_404.androidapp.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by Alan on 3/31/2015.
 */
public class BtCommThread extends Thread {
    private BluetoothDevice device;
    private BluetoothSocket socket;

    private BufferedReader input;
    private PrintWriter output;
    private BluetoothInterface callbacks;

    BtCommThread(BluetoothDevice btDevice, BluetoothInterface btInterface) throws IOException, InterruptedException {//let caller handle Exception with constructor
        callbacks = btInterface;
        device = btDevice;
        socket = device.createInsecureRfcommSocketToServiceRecord(BluetoothService.uuid);
        socket.connect();//attempt connection
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));//get input
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));//get output
    }

    public void setCallbacks(BluetoothInterface callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            String receivedMessage;
            receivedMessage = read();
            callbacks.logMessage(receivedMessage);
            callbacks.handleReceivedMessages(receivedMessage);
        }
    }

    public void write(String s) {
        output.write(s);
        output.flush();
    }

    public String read() {
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            callbacks.logError(e.getMessage());
            close();//close the connection
        }
        return null;
    }

    /*
    *Closes Socket connection, must be called when thread is being disposed
    * to prevent socket.read() from preventing interruption of thread
     */
    public void close() {
        try {
            if (socket != null && socket.isConnected() == true) {
                this.socket.close();
                this.socket = null;//set socket to null
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
