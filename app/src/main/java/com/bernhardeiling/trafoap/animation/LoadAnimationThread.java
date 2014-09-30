package com.bernhardeiling.trafoap.animation;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by Bernhard on 28.08.14.
 */
public class LoadAnimationThread extends Thread {

    final static String TAG = "SendData";
    ArrayList<InetAddress> inetAddresses = new ArrayList<InetAddress>();
    Animation animation = null;
    private int port;

    public LoadAnimationThread(int port, ArrayList<InetAddress> inetAddresses, Animation animation) {
        this.port = port;
        this.inetAddresses = inetAddresses;
        this.animation = animation;
    }

    @Override
    public void run() {
        loadData();
    }

    private synchronized void loadData() {
        for (InetAddress address : inetAddresses) {
            try {
                DatagramSocket socket = new DatagramSocket();
                for (int f = 0; f < animation.getNumFrames(); f++) {
                    for (int c = 0; c < 3; c++) {
                        try {
                            byte[] channelToLoad = channelToBytes(f, c);
                            Log.e(TAG, new String(channelToLoad));
                            DatagramPacket packet = new DatagramPacket(channelToLoad, channelToLoad.length, address, port);
                            socket.send(packet);
                        } catch (final IOException e) {
                                    /*
                                    e.printStackTrace();
                                    Log.e(TAG, e.getMessage());
                                    */
                        }
/*
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        */
                    }

                }
            } catch (final SocketException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            String finishString = "finisht";
            byte[] finishData = finishString.getBytes();
            try {
                DatagramPacket packetStart = new DatagramPacket(finishData, finishData.length, address, port);
                DatagramSocket socketStart = new DatagramSocket();
                socketStart.send(packetStart);
            } catch (final SocketException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            } catch (final IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private byte[] frameToBytes(int frameIndex) {
        String animationString = "load";
        int[][] frameToSend = animation.getFrame(frameIndex);
        for (int f = 0; f < frameToSend.length; f++) {
            for (int c = 0; c < frameToSend[f].length; c++) {
                animationString += Integer.toHexString(frameToSend[f][c]);
            }
        }
        animationString += "t";
        return animationString.getBytes();
    }

    private byte[] channelToBytes(int frameIndex, int channelIndex) {
        String animationString = "load";
        int[][] frameToSend = animation.getFrame(frameIndex);
        for (int f = 0; f < frameToSend.length; f++) {
            animationString += Integer.toHexString(frameToSend[f][channelIndex]);
        }
        animationString += "t";
        return animationString.getBytes();
    }

    void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
