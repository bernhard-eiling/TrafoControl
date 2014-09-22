package com.bernhardeiling.trafoap.animation;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.bernhardeiling.trafoap.interfaces.SyncAnimationInterface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Bernhard on 28.08.14.
 */
public class LoadAnimationTask extends AsyncTask<Void, Void, Void> {

    final static String TAG = "SendData";
    ArrayList<InetAddress> inetAddresses = new ArrayList<InetAddress>();
    Animation animation = null;
    private int port;
    private SyncAnimationInterface delegate ;
    private boolean currentSyncAnimation;

    public LoadAnimationTask(int port, SyncAnimationInterface delegate, boolean currentSyncAnimation) {
        this.port = port;
        this.delegate = delegate;
        this.currentSyncAnimation = currentSyncAnimation;
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (animation != null) {
            for (InetAddress address : inetAddresses) {
                for (int i = 0; i < animation.getNumFrames(); i++) {
                    try {
                        byte[] frameToLoad = frameToBytes(i);
                        DatagramPacket packet = new DatagramPacket(frameToLoad, frameToLoad.length, address, port);
                        DatagramSocket socket = new DatagramSocket();
                        socket.send(packet);
                    } catch (final SocketException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    } catch (final IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void _) {
        delegate.onFinishLoadingAnimation(currentSyncAnimation);
    }

    private byte[] frameToBytes(int frameIndex) {
        String animationString = "";
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

    void setAnimation(Animation animation) {
        this.animation = animation;
    }

    void setInetAddresses(ArrayList<InetAddress> inetAddresses) {
        this.inetAddresses = inetAddresses;
    }
}
