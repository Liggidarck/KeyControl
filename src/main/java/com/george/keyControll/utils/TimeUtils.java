package com.george.keyControll.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeUtils {

    public static String getDate() {
        int dayOfMoth = LocalDate.now().getDayOfMonth();
        int moth = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        String dayMoth = String.valueOf(dayOfMoth);
        String mothStr = String.valueOf(moth);
        if(dayOfMoth < 10) {
            dayMoth = "0" + dayOfMoth;
        }

        if(moth < 10) {
            mothStr = "0" + moth;
        }

        return dayMoth + "." + mothStr + "." + year;
    }

    public static String getTime() {
        return LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
    }

}
