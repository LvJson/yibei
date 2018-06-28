package com.ts.lys.yibei.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lrh on 9/8/15.
 */
public class NumberUtils {

    private static DecimalFormat df = new DecimalFormat(",###.##");

    private static DecimalFormat df1 = new DecimalFormat(",###.00");

    private static DecimalFormat df0 = new DecimalFormat("0.00");

    private static DecimalFormat df2 = new DecimalFormat("0.0");

    private static BigDecimal ONE = new BigDecimal(1);

    public static String formatNumber(BigDecimal val) {
        if (val == null) {
            return "0";
        }
        if (val.compareTo(ONE) < 0 && val.compareTo(BigDecimal.ZERO) != 0) {
            return df0.format(val.doubleValue());
        }

        String tmp = df.format(val.doubleValue());
        if (tmp.indexOf(".") != -1) {
            return df1.format(val.doubleValue());
        }
        return tmp;
    }

    public static String formatNormalNumber(BigDecimal val) {
        if (val == null) {
            return "0";
        }

        if (val.compareTo(ONE) < 0 && val.compareTo(BigDecimal.ZERO) != 0) {
            return df0.format(val.doubleValue());
        }

        String tmp = df.format(val.doubleValue());
        return tmp;
    }


    public static String formatNormal(BigDecimal val) {
        if (val == null) {
            return "0";
        }

        if (val.compareTo(ONE) < 0 && val.compareTo(BigDecimal.ZERO) != 0) {
            return df2.format(val.doubleValue());
        }

        String tmp = df.format(val.doubleValue());
        return tmp;
    }


    public static String formatNormalNumber2(BigDecimal val) {
        if (val == null) {
            return "0.00";
        }
        if (val.compareTo(BigDecimal.ZERO) == 0)
            return "0.00";
        if (val.compareTo(ONE) < 0 && val.compareTo(BigDecimal.ZERO) != 0) {
            return df0.format(val.doubleValue());
        }
        String tmp = df1.format(val.doubleValue());
        return tmp;
    }

    public static String formatTwo(BigDecimal val) {
        if (val == null) {
            return "0.00";
        }

        return df0.format(val.doubleValue());
    }

    public static BigDecimal bigDecimalAdd(BigDecimal val1, BigDecimal val2) {
        return val1.add(val2);
    }

    public static BigDecimal bigDecimalSub(BigDecimal val1, BigDecimal val2) {
        return val1.subtract(val2);
    }

    public static BigDecimal bigDecimalDiv(BigDecimal val1, BigDecimal val2) {
        return val1.divide(val2, 2, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal bigDecimalMul(BigDecimal val1, BigDecimal val2) {
        return val1.multiply(val2);
    }

    public static int formatNormalInt(BigDecimal val) {
        if (val == null) {
            return 0;
        }
        if (val.compareTo(ONE) < 0 && val.compareTo(BigDecimal.ZERO) != 0) {
            return val.intValue();
        }

        int tmp = val.intValue();
        return tmp;
    }

    public static String result(BigDecimal one, BigDecimal two) {
        BigDecimal bigOne = bigDecimalDiv(bigDecimalSub(one, two), one);
        BigDecimal bigTwo = new BigDecimal(100);
        int aa = formatNormalInt(bigDecimalMul(bigOne, bigTwo));
        return String.valueOf(aa);
    }

    /**
     * 计算预期年化收益率
     *
     * @return
     */
    public static String annualYield(BigDecimal one, BigDecimal two) {

        StringBuffer buffer = new StringBuffer();
        if (one == null || two == null)
            return "0";
        if (two.doubleValue() > 0) {
            buffer.append(String.valueOf(one));
            buffer.append("%+");
            buffer.append(String.valueOf(two));
            buffer.append("%");
            return buffer.toString();
        } else {
            buffer.append(String.valueOf(one));
            buffer.append("%");
            return buffer.toString();
        }
    }

    public static String strSplice(BigDecimal one, String before, String back) {
        StringBuffer buffer = new StringBuffer();
        if (one == null || one.doubleValue() == 0)
            return "";

        if (!TextUtils.isEmpty(before)) {
            buffer.append(" " + before);
            buffer.append(deleteZero(one));

        } else
            buffer.append(one);
        buffer.append(back);
        return buffer.toString();
    }

    public static String remainAnt(BigDecimal ant) {
        BigDecimal one = bigDecimalDiv(ant, new BigDecimal(10000));
        String two = formatNumber(one);
        if (two.indexOf(".") > 0) {
//            two = two.replaceAll("0+?$", "");//去掉后面无用的零
            two = two.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return "剩余可投" + two + "万元";
    }

    public static String remainAntWan(BigDecimal ant) {
        BigDecimal one = bigDecimalDiv(ant, new BigDecimal(10000));
        String two = formatNumber(one);
        if (two.indexOf(".") > 0) {
//            two = two.replaceAll("0+?$", "");//去掉后面无用的零
            two = two.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return two;
    }

    public static int ringPercent(BigDecimal amt, BigDecimal remandAmt) {
        if (amt == null || remandAmt == null)
            return 0;
        BigDecimal one = bigDecimalSub(amt, remandAmt);
        return bigDecimalMul(bigDecimalDiv(one, amt), new BigDecimal(100)).intValue();
    }

    public static String deleteZero(BigDecimal bb) {
        String two = formatNumber(bb);
        if (two.indexOf(".") > 0) {
            two = two.replaceAll("0+?$", "");//去掉后面无用的零
            two = two.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return two;
    }
}
