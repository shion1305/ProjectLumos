package com.shion1305.lumos.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTool {
    public static int combineRGB(int rgb1, int rgb2, double ratio) {
        int r = (int) ((rgb1 & 0xff) * ratio + (rgb2 & 0xff) * (1 - ratio));
        int g = (int) (((rgb1 & 0xff00) >>> 8) * ratio + ((rgb2 & 0xff00) >>> 8) * (1 - ratio));
        int b = (int) (((rgb1 & 0xff0000) >>> 16) * ratio + ((rgb2 & 0xff0000) >>> 16) * (1 - ratio));
        int a = (int) (((rgb1 & 0xff000000) >>> 24) * ratio + ((rgb2 & 0xff000000) >>> 24) * (1 - ratio));
        int rgb = r;
        rgb |= g << 8;
        rgb |= b << 16;
        rgb |= a << 24;
        return rgb;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
