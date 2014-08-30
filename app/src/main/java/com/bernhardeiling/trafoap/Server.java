package com.bernhardeiling.trafoap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bernhard on 28.08.14.
 */
public class Server extends AsyncTask<Void, Void, Void> {

    final static String TAG = "SendData";
    String message;
    Context context;
    String ip;

    final Handler toastHandler = new Handler();

    public Server(String ip, Context context) {
        this.ip = ip;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        InetAddress ipAddress = null;
        final int port = 997;

        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, "unknown host");
        }

        try {
            DatagramSocket socket = new DatagramSocket();
            OSCPortOut portOut = new OSCPortOut(ipAddress, port, socket);

            List<Object> args = new ArrayList<Object>(2);
            args.add(3);
            args.add("hello");

            OSCMessage message = new OSCMessage("/sayhello", args);
            message.addArgument("OSC msg");
            portOut.send(message);

        } catch (final UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (final SocketException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (final Exception e) {
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
