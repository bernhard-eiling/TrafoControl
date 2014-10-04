package com.bernhardeiling.trafoap.network;

import android.os.AsyncTask;
import android.util.Log;

import com.bernhardeiling.trafoap.interfaces.ScanDevicesInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Bernhard on 28.08.14.
 */
public class ScanDevicesTask extends AsyncTask<Void, Void, ArrayList<String>> {

    final static String TAG = "ScanDevices";
    public ScanDevicesInterface delegate;

    public ScanDevicesTask(ScanDevicesInterface delegate) {
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... urls) {

        BufferedReader reader = null;
        ArrayList<String> ipList = new ArrayList<String>();
        int timeout = 500;

        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        if (InetAddress.getByName(splitted[0]).isReachable(timeout)) ipList.add(splitted[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        //ipList.add("192.56.23.1"); // TESTING
        return ipList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> ipList) {
        delegate.onFinishScanningConnectedDevices(ipList);
    }
}
