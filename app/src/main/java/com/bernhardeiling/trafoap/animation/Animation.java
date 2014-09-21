package com.bernhardeiling.trafoap.animation;

import java.util.ArrayList;

/**
 * Created by Bernhard on 19.09.14.
 */
public class Animation {

    int frameDelay;
    ArrayList<int[][]> frames = new ArrayList<int[][]>();

    public Animation(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public ArrayList<int[][]> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<int[][]> frames) {
        this.frames = frames;
    }

    public void addFrame(int[][] frame) {
        this.frames.add(frame);
    }

    public int[][] getFrame(int index) {
        return frames.get(index);
    }

    public int getNumFrames() {
        return frames.size();
    }
}
