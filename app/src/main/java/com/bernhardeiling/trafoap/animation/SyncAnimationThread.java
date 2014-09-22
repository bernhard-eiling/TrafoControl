package com.bernhardeiling.trafoap.animation;

import android.util.Log;

import com.bernhardeiling.trafoap.interfaces.SyncAnimationInterface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Bernhard on 21.09.14.
 */

public class SyncAnimationThread extends Thread {

    private int frameCounter = 0;
    private Animation animation = null;
    private int port;
    private ArrayList<InetAddress> inetAddresses = new ArrayList<InetAddress>();
    private boolean syncAnimation = false;

    public SyncAnimationThread(int port) {
        this.port = port;
    }

    public void run() {
        while (true) {
            if (syncAnimation == true && animation != null) {
                int frameIndex = frameCounter % animation.getNumFrames();

                for (InetAddress address : inetAddresses) {
                    try {
                        String syncData = frameIndex + "t";
                        byte[] syncSignal = syncData.getBytes();
                        DatagramPacket packet = new DatagramPacket(syncSignal, syncSignal.length, address, port);
                        DatagramSocket socket = new DatagramSocket();
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    frameCounter++;
                    try {
                        sleep(animation.getFrameDelay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    void setAnimation(Animation animation) {
        this.animation = animation;
    }

    void setSyncAnimation(boolean syncAnimation) {
        this.syncAnimation = syncAnimation;
    }

    void setInetAddresses(ArrayList<InetAddress> inetAddresses) {
        this.inetAddresses = inetAddresses;
    }
}
