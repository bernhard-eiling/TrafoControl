package com.bernhardeiling.trafoap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Bernhard on 22.08.14.
 */
public class SendDataTask extends AsyncTask <Void, Void, Void> {

    final static String TAG = "SendData";
    String message;
    Context context;
    InetAddress ipAddress;
    final static int PORT = 1212;
    final Handler toastHandler = new Handler();

    public SendDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DatagramSocket socket;
        boolean isReachable = false;

        try {
            ipAddress = InetAddress.getByName("192.168.1.24");
            isReachable = ipAddress.isReachable(2000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, "unknown host");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "io exception");
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
            Log.e(TAG, "NetworkOnMainThreadException");
        } finally {
            Log.e(TAG, "is reachable: " + isReachable);
        }

        try {
            socket = new DatagramSocket();
            DatagramPacket packet;
            packet = new DatagramPacket(message.getBytes(), message.length(), ipAddress, PORT);
            socket.setBroadcast(true);
            socket.send(packet);
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (final SocketException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    void setMessage(String message) {
        this.message = message;
    }
    void setContext(Context context) {
        this.context = context;
    }
    void msg(String e) {
        Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
    }
}
