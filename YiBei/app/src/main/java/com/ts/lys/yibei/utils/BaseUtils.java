package com.ts.lys.yibei.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author yangyu
 *         功能描述：常量工具类
 */
public class BaseUtils {
    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 得到状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断字符串是否可以转换成数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 一下是不同参数获取签名的方法
     *
     * @param userName
     * @param type
     * @param publicKey
     * @return
     */
    public static String getSign(String userName, String type, String publicKey) {
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

    public static String getSign(String userName, String type, String agent_id, String publicKey) {
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

    public static String getNullString(String[] strName, String[] strData) {
        List<Integer> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strData.length; i++) {
            if (TextUtils.isEmpty(strData[i])) {
                list.add(i);
            }
        }
        if (list.size() == 0) {
            return null;
        } else {
            for (int i = 0; i < list.size(); i++) {
                buffer.append(strName[list.get(i)] + " ");
            }
            return buffer.toString();
        }
    }

    /**
     * uri转bitmap
     *
     * @param uri
     * @param context
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {

        Bitmap bitmap = null;

        try {

            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;

        }

        return bitmap;

    }

    /**
     * 毫秒转日期
     *
     * @param million
     * @return
     */
    public static String millionToDate(Long million) {
        DateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    public static String millionToDay(Long million) {
        DateFormat formatter = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    public static String millionToMinute(Long million) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取两个整数间的随机数
     *
     * @param min
     * @param max
     * @return
     */
    private static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 设置dialog的样式
     *
     * @param dlg
     * @param ctx
     */
    public static void setDialogWindowAttr(Dialog dlg, Context ctx, int gravity, int width) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = width;//宽高可设置具体大小
        Log.e("width", width + "");
        dlg.getWindow().setAttributes(lp);
    }


    //将指定日期转化为秒数
    public static long getMillionSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //秒数转化为日期
    public static String getDateFromMillionSeconds(long million) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    //秒数转化为日期
    public static String getDateFromMillionSeconds(long million, String style) {
        DateFormat formatter = new SimpleDateFormat(style);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

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
     * 复制数据库
     *
     * @param context
     * @param DBName
     */
    public static void copyDataBaseFromAssets(Context context, String DBName) {

        InputStream in = null;
        FileOutputStream out = null;
        File file = context.getApplicationContext().getDatabasePath(DBName);   //data/data

        if (!file.exists()) {
            //判断database目录是否为空
            File parent = new File(file.getParent());
            if (!parent.exists()) parent.mkdirs();

            try {
                in = context.getAssets().open(DBName); // 从assets目录下复制
                out = new FileOutputStream(file.getPath());
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据时间返回当前大致时间
     *
     * @param a
     * @return
     */
    public static String friendlyTime(long a) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long a=Long.parseLong("1459236542000");
        Date date = new Date(a);
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);

        if (ct == 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return Math.max(ct / 3600, 1) + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = Math.max(ct / 86400, 1);
            return day + "天前";
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return Math.max(ct / 2592000, 1) + "月前";
        }
        return Math.max(ct / 31104000, 1) + "年前";
    }

    /**
     * 处理美元符号与正负号的
     *
     * @param result
     * @return
     */
    public static String dealSymbol(double result) {
        if (result < 0)
            return "-$" + getDigitsData(Math.abs(result), 2);
        else
            return "$" + getDigitsData(result, 2);
    }

    /**
     * 吐司
     *
     * @param toast
     * @param context
     */
    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 指示器
     *
     * @param mContext
     * @param mDataList
     * @param viewPager
     * @param magicIndicator
     */
    public static void setNavigator(Context mContext, final List<String> mDataList, final ViewPager viewPager, MagicIndicator magicIndicator, final boolean isShowIndecator) {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int i) {
                final SimplePagerTitleView clipPagerTitleView = new SimplePagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(i));
                clipPagerTitleView.setNormalColor(Color.parseColor("#666666"));
                clipPagerTitleView.setSelectedColor(Color.parseColor("#FF6200"));
//                clipPagerTitleView.setTextSize(UIUtil.dip2px(context,5.2f));
//                clipPagerTitleView.setPadding(UIUtil.dip2px(context, 0f), 0, UIUtil.dip2px(context, 0f), 0);

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        magicIndicator.onPageSelected(i);
//                        commonNavigator.onPageSelected(i);
//                        commonNavigator.onAttachToMagicIndicator();
                        viewPager.setCurrentItem(i);
                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(new Integer[]{Color.parseColor("#FF6200")});
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(dip2px(context, 2));
                indicator.setLineWidth(dip2px(context, 65));
                indicator.setRoundRadius(10);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//                indicator.setYOffset(UIUtil.dip2px(context, -10));
                if (isShowIndecator)
                    return indicator;
                else
                    return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        titleContainer.setDividerPadding(UIUtil.dip2px(this, 5));
//        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
