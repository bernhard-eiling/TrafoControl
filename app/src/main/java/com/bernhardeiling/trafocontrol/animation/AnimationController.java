package com.bernhardeiling.trafocontrol.animation;

import android.content.Context;

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

    public void setAnimation(String animationString) {
        Animation animation = null;
        if (animationString.equals("Strobo")) {
            animation = animationContainer.getStrobo();
        } else if (animationString.equals("Color")) {
            animation = animationContainer.getRandomColor();
        } else if (animationString.equals("Red")) {
            animation = animationContainer.getRed();
        } else if (animationString.equals("Off")) {
            animation = animationContainer.getBlack();
        } else if (animationString.equals("Swap")) {
            animation = animationContainer.getSwap();
        } else if (animationString.equals("Low")) {
            animation = animationContainer.getLow();
        }
        LoadAnimationThread loadAnimationThread = new LoadAnimationThread(PORT, this.inetAddresses, animation);
        syncAnimationThread.setLoadAnimationThread(loadAnimationThread);
        syncAnimationThread.setAnimation(animation);
    }

    public void setDevices(ArrayList<String> IPs) {
        inetAddresses.clear();
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
