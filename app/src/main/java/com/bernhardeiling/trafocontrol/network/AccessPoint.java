package com.bernhardeiling.trafocontrol.network;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.bernhardeiling.trafocontrol.interfaces.ScanDevicesInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Bernhard on 21.08.14.
 */
public class AccessPoint extends Thread {

    private WifiManager wifiManager;
    private String ssid;
    private String password;

    public AccessPoint(Context context, String ssid, String password) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.ssid = ssid;
        this.password = password;
    }

    public void createAccessPoint() {

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        Method[] managerMethods = wifiManager.getClass().getDeclaredMethods();

        for (Method method : managerMethods) {
            if (method.getName().equals("setWifiApEnabled")) {

                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = ssid;
                wifiConfiguration.wepKeys[0] = password;
                wifiConfiguration.wepTxKeyIndex = 0;
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); // NONE uses WEP

                try {
                    method.invoke(wifiManager, wifiConfiguration, true);
                }  catch (IllegalAccessException e) {
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

