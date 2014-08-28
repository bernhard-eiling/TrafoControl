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
 * Created by Bernhard on 28.08.14.
 */
public class Server extends AsyncTask<Void, Void, Void> {

    final static String TAG = "SendData";
    String message;
    Context context;
    String ip;
    InetAddress ipAddress;
    final static int PORT = 1212;
    final Handler toastHandler = new Handler();

    public Server(String ip, Context context) {
        this.ip = ip;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DatagramSocket socket;

        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, "unknown host");
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
