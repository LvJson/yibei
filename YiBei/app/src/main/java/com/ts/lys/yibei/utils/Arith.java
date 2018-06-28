package com.ts.lys.yibei.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;

/**
 * Created by jcdev1 on 2017/5/18.
 * <p>
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
 */
public class Arith { // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10; // 这个类不能实例化

    private Arith() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();//保留小数点位数多的
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double sub2(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (v2 == 0) return 0;
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五也舍
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static double div2(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            return 0;

        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).doubleValue();
        }

    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String round2(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP) + "";
    }

    public static double round3(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五也舍
     *
     * @param v
     * @param scale
     * @return
     */
    public static double round4(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    public static String add2(double one, double two, int scale) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);
        return (b1.add(b2)).setScale(scale, BigDecimal.ROUND_HALF_UP) + "";
    }


    /**
     * 处理ask和bid的差值
     *
     * @param ask
     * @param bid
     * @return
     */
    public static String format(double ask, double bid, int digits) {

//        String buff = (ask.floatValue() + "");
//        int len1 = buff.length();
//        int len2 = buff.indexOf(".");
//        return ask.subtract(bid).abs().multiply(BigDecimal.TEN.pow(len1 - len2 - 1)).intValue();

        double a = Math.pow(10, digits) * ask;
        double b = Math.pow(10, digits) * bid;
        double result = Math.abs(a - b);
        String str = BaseUtils.getDigitsData(result / 10, 1);
        if (str.lastIndexOf("0") == str.length() - 1)
            return str.substring(0, str.length() - 2);
        else
            return str;
    }


    /**
     * 修改部分文字大小
     * @param text
     * @param digits
     * @return
     */
    public static SpannableString changePartTextSize(String text, int digits) {
        int lengthLeft = text.length();

        SpannableString ssRight = new SpannableString(text);
        int lengthRight = ssRight.length();
        RelativeSizeSpan spanRight = new RelativeSizeSpan((float) 1.5);

        if (digits > 2) {
            ssRight.setSpan(spanRight, lengthRight - 3, lengthRight - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            ssRight.setSpan(spanRight, lengthRight - 4, lengthRight - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return ssRight;
    }
}
