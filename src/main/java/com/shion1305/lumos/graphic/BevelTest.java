package com.shion1305.lumos.graphic;

import com.shion1305.components.fileio.FileDownloader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.shion1305.lumos.graphic.ImageTool.combineRGB;

public class BevelTest {
    public static void main(String[] args) throws IOException {
        BufferedImage logo = ImageIO.read(new File("C:\\Users\\shion\\OneDrive\\Welcome\\Active\\YokohamaNationalUniversity\\Club Activity\\Lumos\\400ppi\\LumosLogo.png"));
        BufferedImage next = ImageIO.read(new File("C:\\Users\\shion\\OneDrive\\Welcome\\Active\\YokohamaNationalUniversity\\Club Activity\\Lumos\\400ppi\\next.png"));
        BufferedImage text_activity = ImageIO.read(new File("C:\\Users\\shion\\OneDrive\\Welcome\\Active\\YokohamaNationalUniversity\\Club Activity\\Lumos\\400ppi\\のアクティビティ.png"));
        BufferedImage card = new BufferedImage(1800, 1040, BufferedImage.TYPE_INT_ARGB);
        card = GradationThemeCreator.createGradation(card, 0xFF0fdb46, 0xFF1c2cba);
//        card = GradationThemeCreator.createGradation(card, 0xff353535, 0xFF282828);
        int pY = 60;
        for (int i = 0, pX = 720; i < 4; i++, pX += 260) {
            BufferedImage icon = ImageIO.read(new FileDownloader("https://cdn.discordapp.com/avatars/706873888461684866/7843d7d68ecf8415f963669ec7dacb14.png?size=512").getStream());
            icon = ImageTool.resize(icon, 240, 240);
            for (int x = 0; x < 240; x++) {
                for (int y = 0; y < 240; y++) {
                    int loc = (int) (Math.pow((x - 120), 2) + Math.pow((y - 120), 2));
                    if (loc < 6000) {
                        card.setRGB(x + pX, y + pY, icon.getRGB(x, y));
                    } else if (loc < 7200) {
                        card.setRGB(x + pX, y + pY, combineRGB(card.getRGB(pX + x, pY + y), icon.getRGB(x, y), (double) (loc - 6000) / 1200));
                    }
                }
            }
        }
        Graphics g = card.getGraphics();
        g.drawImage(logo, 700, 620, 1080, 330, null);
        g.drawImage(next, 0, 540, 540, 460, null);
        g.drawImage(text_activity, 0, 30, 740, 710, null);
        Font font = new Font("Kaisei Tokumin", Font.PLAIN, 200);
        g.setFont(font);
        g.drawString("12", 100, 200); //文字を書く
        g.dispose();
        ImageIO.write(card, "png", new File("testing.png"));
    }
}
