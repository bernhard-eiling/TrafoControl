package com.bernhardeiling.trafocontrol.animation;

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

                    try {
                        byte[] channelToLoad = frameToHexaBytes(f);
                        DatagramPacket packet = new DatagramPacket(channelToLoad, channelToLoad.length, address, port);
                        socket.send(packet);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }
            } catch (final SocketException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            String finishString = "ft";
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
        String animationString = "l";
        int[][] frameToSend = animation.getFrame(frameIndex);
        for (int f = 0; f < frameToSend.length; f++) {
            for (int c = 0; c < frameToSend[f].length; c++) {
                animationString += frameToSend[f][c];
                animationString += ",";
            }
            animationString += "c";
        }
        animationString += "t";
        return animationString.getBytes();
    }

    private byte[] frameToHexaBytes(int frameIndex) {
        String animationString = "l";
        int[][] frameToSend = animation.getFrame(frameIndex);
        for (int f = 0; f < frameToSend.length; f++) {
            for (int c = 0; c < frameToSend[f].length; c++) {
                StringBuilder builder = new StringBuilder();
                String hexValue = Integer.toHexString(frameToSend[f][c]);
                builder.append(hexValue);
                if (builder.length() < 2) builder.insert(0, '0'); // add a leading zero if string has length of 1
                animationString += builder.toString();
            }
        }
        animationString += "t";
        return animationString.getBytes();
    }

    private byte[] channelToBytes(int frameIndex, int channelIndex) {
        String animationString = "l";
        int[][] frameToSend = animation.getFrame(frameIndex);
        for (int f = 0; f < frameToSend.length; f++) {
            animationString += frameToSend[f][channelIndex];
            animationString += ",";
        }
        animationString += "t";
        return animationString.getBytes();
    }

    void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
