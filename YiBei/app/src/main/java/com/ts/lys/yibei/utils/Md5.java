package com.ts.lys.yibei.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LYS on 2016/12/20.
 */

public class Md5 {
    //md5加密
    public String getSign1(String userName, String type, String publicKey) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sf.format(new Date());
        String plainText = userName + "-" + type + "-" + dateTime + "-" + publicKey;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSign2(String userName, String type, String agent_id, String publicKey) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sf.format(new Date());
        String plainText = userName + "-" + type + "-" + agent_id + "-" + dateTime + "-" + publicKey;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //md5加密
    public static String getSign3(String uuid, String rd, String t, String salt) {
        String plainText = uuid + rd + t + salt;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte bytes[] = md.digest();
//            int i;

            StringBuffer buf = new StringBuffer();
            if (bytes != null && bytes.length > 0) {
                StringBuilder buff = new StringBuilder(bytes.length << 1);
                String tmp = null;
                for (int i = 0; i < bytes.length; i++) {
                    tmp = (Integer.toHexString(bytes[i] & 0xFF));
                    if (tmp.length() == 1) {
                        buff.append('0');
                    }
                    buff.append(tmp);
                }
                return buff.toString();
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //md5加密
    public static String getSign4(String password) {
        String plainText = password;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte bytes[] = md.digest();
//            int i;

            StringBuffer buf = new StringBuffer();
            if (bytes != null && bytes.length > 0) {
                StringBuilder buff = new StringBuilder(bytes.length << 1);
                String tmp = null;
                for (int i = 0; i < bytes.length; i++) {
                    tmp = (Integer.toHexString(bytes[i] & 0xFF));
                    if (tmp.length() == 1) {
                        buff.append('0');
                    }
                    buff.append(tmp);
                }
                return buff.toString();
            }
            //32位加密
            return buf.toString();
//            return buf.toString().toUpperCase();//32位大写加密
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
