package com.shion1305.lumos.activity_summary;

import com.shion1305.components.fileio.FileDownloader;
import com.shion1305.lumos.general.DiscordClientManager;
import com.shion1305.lumos.graphic.ImageTool;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@WebServlet("/image/*")
public class ImageHandler extends HttpServlet implements ServletContextListener {
    private static final Logger logger = Logger.getLogger("ImageHandler");
    private static HashMap<String, byte[]> data;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reqFile = req.getPathInfo();
        logger.info(reqFile);
        reqFile = reqFile.substring(1);
        Pattern regex = Pattern.compile("^[A-Za-z0-9-]+\\.[a-z]{3,4}$");
        if (!regex.matcher(reqFile).matches()) {
            logger.info("invalid file");
            declineRequest(resp);
            return;
        }
        if (data.containsKey(reqFile)) {
            resp.setStatus(200);
            resp.setContentType("image/png");
            try (OutputStream s = resp.getOutputStream()) {
                s.write(data.get(reqFile));
            }
        } else {
            logger.info("target file not found");
            for (String d : data.keySet()) {
                logger.info(d);
            }
        }
    }

    private void declineRequest(HttpServletResponse resp) throws IOException {
        resp.sendError(404);
        resp.getOutputStream().println("NO SUCH FILE IS REGISTERED");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        data = new HashMap<>();
        //start loading discord icons
        loadIcons();
        //start loading static files
        loadStaticImages();

    }

    public static void loadIcons() {
        Guild guild = DiscordClientManager.getClient().getGuildById(Snowflake.of(894226019240800276L)).block();
        guild.getMembers().subscribe(member -> {
            try (InputStream stream = new FileDownloader(member.getAvatarUrl()).getStream()) {
                BufferedImage image = ImageIO.read(stream);
                image = createRoundIcon(image);
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    ImageIO.write(image, "png", bos);
                    String filename = member.getId().asString() + ".png";
                    addImage(filename, bos.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadStaticImages() {
        String tomcatBase = System.getProperty("catalina.home");
        String[] targets = {"next.png", "LumosLogo.png"};
        for (String target : targets) {
            try (FileInputStream fis = new FileInputStream(tomcatBase + "/webapps/Lumos/" + target)) {
                addImage(target, fis.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage createRoundIcon(BufferedImage target) {
        target = ImageTool.resize(target, 401, 401);
        for (int x = 0; x < 401; x++) {
            for (int y = 0; y < 401; y++) {
                int loc = (int) (Math.pow((x - 200), 2) + Math.pow((y - 200), 2));
                if (loc > 40000) {
                    target.setRGB(x, y, 0x00ffffff);
                } else if (loc > 30000) {
                    target.setRGB(x, y, ImageTool.transparentEffect(target.getRGB(x, y), (double) (40000 - loc) / 10000));
                }
            }
        }
        return target;
    }

    public static void addImage(String filename, byte[] imgData) {
        data.put(filename, imgData);
        logger.info(filename + " is loaded");
    }
}
