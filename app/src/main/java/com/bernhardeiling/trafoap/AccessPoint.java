package com.bernhardeiling.trafoap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.bernhardeiling.trafoap.interfaces.ScanDevicesInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Bernhard on 21.08.14.
 */
public class AccessPoint {

    private final static String TAG = "AP";
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    private boolean enabled = false;

    public AccessPoint(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void createAccessPoint(String ssid, String pass) {

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        Method[] managerMethods = wifiManager.getClass().getDeclaredMethods();

        for (Method method : managerMethods) {
            if (method.getName().equals("setWifiApEnabled")) {

                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = ssid;
                wifiConfiguration.wepKeys[0] = pass;
                wifiConfiguration.wepTxKeyIndex = 0;

                //wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
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

    public void scanForConnectedDevices(ScanDevicesInterface delegate) {
        ScanDevicesTask scanDevicesTask = new ScanDevicesTask(delegate);
        scanDevicesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

