package com.bernhardeiling.trafoap.animation;

import android.content.Context;
import android.os.AsyncTask;

import com.bernhardeiling.trafoap.interfaces.SyncAnimationInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Bernhard on 21.09.14.
 */
public class AnimationController {

    private static final int PORT = 997;
    private boolean syncAnimation = false;
    Context context;
    AnimationContainer animationContainer;
    ArrayList<InetAddress> inetAddresses = new ArrayList<InetAddress>();
    SyncAnimationThread syncAnimationThread = new SyncAnimationThread(PORT);

    public AnimationController(Context context) {
        this.context = context;
        animationContainer = new AnimationContainer();
        syncAnimationThread.start();
    }

    SyncAnimationInterface syncAnimationInterface = new SyncAnimationInterface() {
        @Override
        public void onFinishLoadingAnimation(boolean syncAnimation) {
            syncAnimationThread.setSyncAnimation(syncAnimation);
        }
    };

    public void setAnimation(String animation) {
        syncAnimationThread.setSyncAnimation(false);
        LoadAnimationTask loadAnimationTask = new LoadAnimationTask(PORT, syncAnimationInterface, this.syncAnimation);
        if (animation.equals("Strobo")) {
            loadAnimationTask.setAnimation(animationContainer.getStrobo());
            syncAnimationThread.setAnimation(animationContainer.getStrobo());
        } else if (animation.equals("Color")){
            loadAnimationTask.setAnimation(animationContainer.getRandomColor());
            syncAnimationThread.setAnimation(animationContainer.getRandomColor());
        } else if (animation.equals("Red")){
            loadAnimationTask.setAnimation(animationContainer.getRed());
            syncAnimationThread.setAnimation(animationContainer.getRed());
        } else if (animation.equals("Off")){
            loadAnimationTask.setAnimation(animationContainer.getBlack());
            syncAnimationThread.setAnimation(animationContainer.getBlack());
        }
        loadAnimationTask.setInetAddresses(inetAddresses);
        loadAnimationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setDevices(ArrayList<String> IPs) {
        for (String ip : IPs) {
            try {
                inetAddresses.add(InetAddress.getByName(ip));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        syncAnimationThread.setInetAddresses(inetAddresses);
    }

    public void setSyncAnimation(boolean syncAnimation) {
        this.syncAnimation = syncAnimation;
        syncAnimationThread.setSyncAnimation(this.syncAnimation);
    }
}
