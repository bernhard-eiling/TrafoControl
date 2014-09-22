package com.bernhardeiling.trafoap.animation;

/**
 * Created by Bernhard on 19.09.14.
 */
public class AnimationContainer {

    final static int NUMCOLORCHANNELS = 3;
    final static int NUMLEDS = 34;
    Animation strobo;
    Animation red;
    Animation black;
    Animation randomColor;

    public AnimationContainer() {
        strobo = createStroboAnimation();
        red = createRedAnimation();
        black = createBlackAnimation();
        randomColor = createRandomColorAnimation();
    }

    private Animation createStroboAnimation() {
        Animation strobo = new Animation(100);
        int[][] whiteFrame = new int[NUMLEDS][NUMCOLORCHANNELS];
        int[][] blackFrame = new int[NUMLEDS][NUMCOLORCHANNELS];

        for (int l = 0; l < whiteFrame.length; l++) {
            for (int c = 0; c < whiteFrame[l].length; c++) {
                whiteFrame[l][c] = 255;
            }
        }
        for (int l = 0; l < blackFrame.length; l++) {
            for (int c = 0; c < blackFrame[l].length; c++) {
                blackFrame[l][c] = 0;
            }
        }
        strobo.addFrame(whiteFrame);
        strobo.addFrame(blackFrame);
        return strobo;
    }

    private Animation createRandomColorAnimation() {
        Animation randomColor = new Animation(50);
        int numFrames = 10;
        for (int i = 0; i < numFrames; i++) {
            int[][] frame  = new int[NUMLEDS][NUMCOLORCHANNELS];
            for (int l = 0; l < frame.length; l++) {
                for (int c = 0; c < frame[l].length; c++) {
                    frame[l][c] = (int) (Math.random() * 256);
                }
            }
            randomColor.addFrame(frame);
        }
        return randomColor;
    }

    private Animation createRedAnimation() {
        Animation red = new Animation(1000);
        int[][] redFrame = new int[NUMLEDS][NUMCOLORCHANNELS];

            for (int l = 0; l < redFrame[0].length; l++) {
                redFrame[0][l] = 255;
                redFrame[1][l] = 0;
                redFrame[2][l] = 0;
            }

        red.addFrame(redFrame);
        return red;
    }

    private Animation createBlackAnimation() {
        Animation black = new Animation(1000);
        int[][] blackFrame = new int[NUMLEDS][NUMCOLORCHANNELS];
        black.addFrame(blackFrame);
        return black;
    }

    public Animation getStrobo() {
        return strobo;
    }
    public Animation getRed() {
        return red;
    }
    public Animation getBlack() {
        return black;
    }
    public Animation getRandomColor() {
        return randomColor;
    }
}
