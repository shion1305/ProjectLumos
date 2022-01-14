package com.shion1305.lumos;

import com.shion1305.components.fileio.FileDownloader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageCreator {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new FileDownloader("https://cdn.discordapp.com/avatars/706873888461684866/7843d7d68ecf8415f963669ec7dacb14.png?size=512").getStream());
//        BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR);
        System.out.println(image.getHeight());
        for (int i = 0; i < 512; i++) {
            for (int j = 0; j < 512; j++) {
//                image.setRGB(i, j, 0xffffffff);
                int loc = (int) (Math.pow((i - 255.5), 2) + Math.pow((j - 255.5), 2));
                if (loc > 65281) {
                    image.setRGB(i, j, 0x00ffffff);
                } else if (loc > 50000) {
//                    image.setRGB(i,j,0x0);
                    image.setRGB(i, j, combineRGB(0xFF000000, image.getRGB(i, j), (double) (loc - 50000) / 16000.0));
                }
            }
        }
        ImageIO.write(image, "png", new File("test.png"));
    }

    ArrayList<BufferedImage> profiles = new ArrayList<>();

    public ImageCreator() {

    }

    public void create() {

    }

    public void addImage(BufferedImage image) {
        profiles.add(image);
    }

    private static int combineRGB(int rgb1, int rgb2, double ratio) {
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
}
