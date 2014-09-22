package com.bernhardeiling.trafoap.interfaces;

import java.util.ArrayList;

/**
 * Created by Bernhard on 28.08.14.
 */
public interface ScanDevicesInterface {

    void onFinishScanningConnectedDevices(ArrayList<String> devices);
}
