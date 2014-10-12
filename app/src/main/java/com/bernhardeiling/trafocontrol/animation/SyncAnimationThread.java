package com.bernhardeiling.trafocontrol.animation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    private LoadAnimationThread loadAnimationThread;

    public SyncAnimationThread(int port) {
        this.port = port;
    }

    public void run() {

        while (true) {
            if (syncAnimation == true && loadAnimationThread != null && animation != null) {
                syncData(loadAnimationThread);
            }
        }
    }

    private void syncData(LoadAnimationThread loadAnimationThread) {

        synchronized (loadAnimationThread) {

            int frameIndex = frameCounter % animation.getNumFrames();
            long startTime = System.currentTimeMillis();
            for (InetAddress address : inetAddresses) {

                try {
                    String syncData = "s" + frameIndex + "t";
                    byte[] syncSignal = syncData.getBytes();
                    DatagramPacket packet = new DatagramPacket(syncSignal, syncSignal.length, address, port);
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frameCounter++;
            }
            long endTime = System.currentTimeMillis() - startTime;
            try {
                int frameDelay = animation.getFrameDelay() - (int)endTime;
                if (frameDelay < 0) frameDelay = 0;
                sleep(frameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    void setLoadAnimationThread(LoadAnimationThread loadAnimationThread) {
        this.loadAnimationThread = loadAnimationThread;
        this.loadAnimationThread.start();
    }
}
