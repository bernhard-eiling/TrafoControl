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
    Animation lowLight;
    Animation swap;

    public AnimationContainer() {
        strobo = createStroboAnimation();
        red = createRedAnimation();
        black = createBlackAnimation();
        randomColor = createRandomColorAnimation();
        lowLight = createLowLightAnimation();
        swap = createSwapAnimation();
    }
/*
    private Animation createSnakeAnimation() {
        Animation strobo = new Animation(100);
        int[][] first = new int[NUMLEDS][NUMCOLORCHANNELS];
        int[][] second = new int[NUMLEDS][NUMCOLORCHANNELS];
        int[][] thirs = new int[NUMLEDS][NUMCOLORCHANNELS];

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
*/
    private Animation createStroboAnimation() {
        Animation strobo = new Animation(100);
        int[][] whiteFrame = new int[NUMLEDS][NUMCOLORCHANNELS];
        int[][] blackFrame = new int[NUMLEDS][NUMCOLORCHANNELS];

        for (int l = 0; l < whiteFrame.length; l++) {
            for (int c = 0; c < whiteFrame[l].length; c++) {
                whiteFrame[l][c] = 255;
                blackFrame[l][c] = 0;
            }
        }
        strobo.addFrame(whiteFrame);
        strobo.addFrame(blackFrame);
        return strobo;
    }

    private Animation createSwapAnimation() {
        Animation ani = new Animation(100);
        int[][] first = new int[NUMLEDS][NUMCOLORCHANNELS];
        int[][] second = new int[NUMLEDS][NUMCOLORCHANNELS];

        for (int l = 0; l < first.length; l += 2) {
            for (int c = 0; c < first[l].length; c++) {
                first[l][c] = 255;
                first[l + 1][c] = 0;
                second[l][c] = 0;
                second[l + 1][c] = 255;
            }
        }
        ani.addFrame(first);
        ani.addFrame(second);
        return ani;
    }

    private Animation createRandomColorAnimation() {
        Animation randomColor = new Animation(500);
        int numFrames = 2;
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

            for (int l = 0; l < redFrame.length; l++) {
                redFrame[l][0] = 255;
                redFrame[l][1] = 0;
                redFrame[l][2] = 0;
            }

        red.addFrame(redFrame);
        return red;
    }

    private Animation createLowLightAnimation() {
        Animation ani = new Animation(1000);
        int[][] redFrame = new int[NUMLEDS][NUMCOLORCHANNELS];

        for (int l = 0; l < redFrame.length; l++) {
            redFrame[l][0] = 5;
            redFrame[l][1] = 5;
            redFrame[l][2] = 5;
        }

        ani.addFrame(redFrame);
        return ani;
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
    public Animation getLow() {
        return lowLight;
    }
    public Animation getSwap() {
        return swap;
    }
}
