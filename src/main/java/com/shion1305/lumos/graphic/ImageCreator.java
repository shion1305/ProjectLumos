package com.shion1305.lumos.graphic;

import com.shion1305.components.fileio.FileDownloader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.shion1305.lumos.graphic.ImageTool.combineRGB;
import static com.shion1305.lumos.graphic.ImageTool.resize;

public class ImageCreator {
    public static void main(String[] args) throws IOException {
        try (FileOutputStream os = new FileOutputStream("sample.png")) {
            os.write(new FileDownloader("https://cdn.discordapp.com/avatars/706873888461684866/7843d7d68ecf8415f963669ec7dacb14.png?size=512").doTask());
        }
        BufferedImage image = ImageIO.read(new FileDownloader("https://cdn.discordapp.com/avatars/706873888461684866/7843d7d68ecf8415f963669ec7dacb14.png?size=512").getStream());
        System.out.println(image.getHeight());
        for (int i = 0; i < 512; i++) {
            for (int j = 0; j < 512; j++) {
//                image.setRGB(i, j, 0xffffffff);
                int loc = (int) (Math.pow((i - 255.5), 2) + Math.pow((j - 255.5), 2));
                if (loc > 65281) {
                    image.setRGB(i, j, 0x00ffffff);
                } else if (loc > 50000) {
//                    image.setRGB(i,j,0x0);
                    image.setRGB(i, j, combine1(image.getRGB(i, j), 1 - (double) (loc - 50000) / 16000.0));
                }
            }
        }
        ImageIO.write(image, "png", new File("test.png"));

        System.out.println(image.getHeight());
        BufferedImage image1 = resize(image, 200, 200);
        System.out.println(image1.getHeight());
        ImageIO.write(image1, "png", new File("test1.png"));

    }

    ArrayList<BufferedImage> profiles = new ArrayList<>();

    public ImageCreator() {

    }

    public void create() {

    }

    public void addImage(BufferedImage image) {
        profiles.add(image);
    }

    public static int combine1(int color, double ratio) {
        int a = (int) ((color >>> 24) * ratio) << 24;
        return (color & 0xffffff) | a;
    }

}
