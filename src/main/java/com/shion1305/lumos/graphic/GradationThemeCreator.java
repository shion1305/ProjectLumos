package com.shion1305.lumos.graphic;

import com.shion1305.lumos.graphic.ImageTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GradationThemeCreator {
    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        int color2 = 0xff30FC32;
        int color1 = 0xFF44FAEF;
        ImageIO.write(createGradation(image, color1, color2), "png", new File("test2.png"));
        ImageIO.write(createGradation(image, color2, color1), "png", new File("test3.png"));

    }

    public static BufferedImage createGradation(BufferedImage image, int color1, int color2) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                long l1 = (long) (Math.pow(i, 2) + Math.pow(j, 2));
                long l2 = (long) (Math.pow(image.getHeight() - i, 2) + Math.pow(image.getWidth() - j, 2));
                image.setRGB(j, i, ImageTool.combineRGB(color1, color2, (double)l1/(l1+l2)));
            }
        }
        return image;
    }
}
