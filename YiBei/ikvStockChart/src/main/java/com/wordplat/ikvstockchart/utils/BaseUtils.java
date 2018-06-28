package com.wordplat.ikvstockchart.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jcdev1 on 2017/6/28.
 */

public class BaseUtils {
    /**
     * 格式化数据
     *
     * @param price
     * @param digits
     * @return
     */
    public static String getDigitsData(double price, int digits) {
        StringBuilder builder = new StringBuilder();
        builder.append("0.0");
        for (int i = 0; i < digits - 1; i++) {
            builder.append("0");
        }
        DecimalFormat decimalFormatter = new DecimalFormat(builder.toString());

        return decimalFormatter.format(price);

    }

    /**
     * 格式化数据
     *
     * @param price
     * @param digits
     * @return
     */
    public static String getDigitsData(String price, int digits) {
        double pr = 0;
        try {
            pr = Double.parseDouble(price);
        } catch (Exception e) {
            pr = 0;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("0.0");
        for (int i = 0; i < digits - 1; i++) {
            builder.append("0");
        }
        DecimalFormat decimalFormatter = new DecimalFormat(builder.toString());
        return decimalFormatter.format(pr);

    }

    //秒数转化为日期
    public static String getDateFromMillionSeconds(long million, String style) {
        DateFormat formatter = new SimpleDateFormat(style);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }
}
