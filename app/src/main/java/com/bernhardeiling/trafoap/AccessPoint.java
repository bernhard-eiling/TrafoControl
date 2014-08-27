package com.bernhardeiling.trafoap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Bernhard on 21.08.14.
 */
public class AccessPoint {

    private final static String TAG = "AP";
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private Context context;

    private boolean enabled = false;

    public AccessPoint(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnected() {
        return networkInfo.isConnected();
    }

    public void createAccessPoint(String ssid, String pass) {

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        Method[] managerMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound = false;

        for (Method method : managerMethods) {
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;

                WifiConfiguration wifiConfiguration = new WifiConfiguration();

                wifiConfiguration.SSID = ssid;
                wifiConfiguration.wepKeys[0] = pass;
                wifiConfiguration.wepTxKeyIndex = 0;

                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                try {
                    enabled = (Boolean) method.invoke(wifiManager, wifiConfiguration, true);
                    for (Method isWifiApEnabledmethod : managerMethods) {
                        if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                            while (!(Boolean) isWifiApEnabledmethod.invoke(wifiManager)) {
                                // Keep it running until ...
                                Log.d("Tag", "running");
                            }

                            for (Method method1 : managerMethods) {
                                if (method1.getName().equals("getWifiApState")) {
                                    method1.invoke(wifiManager);
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> getConnectedDevices() {
        BufferedReader reader = null;
        ArrayList<String> ipList = new ArrayList<String>();

        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        ipList.add(splitted[0]);
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

        return ipList;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }
}
