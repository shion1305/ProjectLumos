package com.shion1305.lumos;

import com.shion1305.lumos.activity_summary.SummaryMessageHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/updateDate")
public class DateUpdater extends HttpServlet {
    Logger logger = Logger.getLogger("DateUpdater");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("DataUpdater is called");
        StringBuilder builder = new StringBuilder();
        try (Scanner scanner = new Scanner(req.getInputStream())) {
            Pattern pattern = Pattern.compile("20\\d\\d\\/[0,1]?\\d\\/[0-3]?\\d [0-2][0-9]:[0-5]\\d");
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (!pattern.matcher(input).matches() && SummaryMessageHandler.resolveExpression(input) != null) {
                    resp.setStatus(400);
                    return;
                }
                builder.append(input);
                builder.append("\n");
            }
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(System.getProperty("user.home") + "/Lumos/schedule.data", false))) {
                writer.write(builder.toString());
                writer.flush();
                logger.info("UPDATED");
            } catch (IOException e) {
                resp.setStatus(503);
                logger.severe("Error during processing request");
                e.printStackTrace();
            }
            SummaryMessageHandler.readConfig();
            resp.setStatus(202);
        } catch (
                IOException e) {
            resp.setStatus(503);
            logger.severe("Error during processing request");
            e.printStackTrace();
        }
    }
}
