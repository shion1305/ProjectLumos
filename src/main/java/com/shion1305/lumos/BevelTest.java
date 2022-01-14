package com.shion1305.lumos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BevelTest {
    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
        //initialization
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                image.setRGB(i, j, 0xffffff);
                int loc = (int) (Math.pow((i - 500), 2) + Math.pow((j - 500), 2));
                if (loc > 120000 && loc < 140000) {
                    int a = (int) ((20000 - (loc - 120000)) * 255 / 20000);
                    int rgb = (a * 0x10101);
                    image.setRGB(i, j, rgb);
                }
            }
        }


        ImageIO.write(image, "png", new File("test.png"));

    }

    private int combineRGB(int rgb1, int rgb2, double ratio) {
        int rgb = (int) ((rgb1 & 0x11) * ratio);
        rgb += (int) ((rgb1 & 0x1100) * ratio);
        rgb += (int) ((rgb1 & 0x110000) * ratio);
        rgb += (int) ((rgb2 & 0x11) * (1-ratio));
        rgb += (int) ((rgb2 & 0x1100) * (1-ratio));
        rgb += (int) ((rgb2 & 0x110000) * (1-ratio));
        return rgb;
    }
}
