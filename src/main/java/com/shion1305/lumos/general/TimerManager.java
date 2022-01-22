package com.shion1305.lumos.general;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    static Timer timer=new Timer();
    
    public static void schedule(TimerTask task, Date date) {
        timer.schedule(task, date);
    }
}