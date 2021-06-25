package com.example.shopapp.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Config {
    public static String user_id = "";
    public static String user_name = "";
    public static String user_email = "";
    public static String user_phone = "";
    public static String shopStatus = "";
    public static String changeDateFormat(String currentDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date date = null;

        try {
            date = formatter.parse(currentDate);
        } catch (ParseException e) {
        }

        String newDate = df.format(date);
        return newDate;
    }
}
