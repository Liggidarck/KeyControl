package com.george.keyControll.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeUtils {

    public String getDate() {
        return LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear();
    }

    public String getTime() {
        return LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
    }

}
