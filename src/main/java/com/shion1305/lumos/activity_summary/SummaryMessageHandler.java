package com.shion1305.lumos.activity_summary;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.shion1305.lumos.general.ConfigManager;
import com.shion1305.lumos.general.TimerManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SummaryMessageHandler implements ServletContextListener {
    private static DiscordVoiceMonitor monitor;
    private static Logger logger = Logger.getLogger("SummaryMessageSystem");
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
    private static List<Date> dates = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initialization started");
        readConfig();
    }

    public static void readConfig() {
        try (FileInputStream stream = new FileInputStream(System.getProperty("user.home") + "/Lumos/schedule.data")) {
            Scanner scanner = new Scanner(stream);
            //read all dates from schedule.data
            while (scanner.hasNextLine()) {
                Date tmp;
                if ((tmp = resolveExpression(scanner.nextLine())) != null) {
                    if (tmp.after(new Date())) {
                        dates.add(tmp);
                    }
                }
            }
            logger.info(dates.toArray().toString());
            formatData();
            for (Date date : dates) {
                TimerManager.schedule(generateTask(), date);
                logger.info("Scheduled at " + date.toString());
            }
        } catch (FileNotFoundException e) {
            logger.info("Schedule Data NOT FOUND");
        } catch (IOException e) {
            logger.warning("UNEXPECTED ERROR");
            logger.warning(e.getMessage());
        }
    }

    private static TimerTask generateTask() {
        return new TimerTask() {
            @Override
            public void run() {
                logger.info("TASK EXECUTED");
                monitor = new DiscordVoiceMonitor();
                monitor.start();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                SimpleDateFormat dateF = new SimpleDateFormat("M/d");
                TimerManager.schedule(generateEndTask(dateF.format(new Date())), calendar.getTime());
            }
        };
    }

    private static TimerTask generateEndTask(String date) {
        return new TimerTask() {
            @Override
            public void run() {
                List<Long> result = monitor.end();
                Date nextDate = getNextDate();
                FlexMessage message;
                if (nextDate == null) message = new SummaryLineMessageBuilder(date, result, "", "").build();
                else {
                    SimpleDateFormat dateF = new SimpleDateFormat("M/d");
                    SimpleDateFormat dateT = new SimpleDateFormat("HH:mm~");
                    message = new SummaryLineMessageBuilder(date, result, dateF.format(nextDate), dateT.format(nextDate)).build();
                }
                LineMessagingClient client = LineMessagingClient.builder(ConfigManager.getConfig("LineMessagingToken")).build();
                try {
                    client.pushMessage(new PushMessage("U68c3c8e484974b3ca784315d1c2d23ec", message)).get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.severe("FAILED TO SEND MESSAGE");
                    e.printStackTrace();
                }
            }
        };
    }


    private static Date resolveExpression(String exp) {
        try {
            return format.parse(exp);
        } catch (ParseException e) {
            return null;
        }
    }

    private static void formatData() {
        dates.removeIf(date -> date.before(new Date()));
        dates.sort((o1, o2) -> o1.after(o2) ? 1 : -1);
    }

    public static Date getNextDate() {
        formatData();
        if (dates.size() > 0) return dates.get(0);
        else return null;
    }
}
