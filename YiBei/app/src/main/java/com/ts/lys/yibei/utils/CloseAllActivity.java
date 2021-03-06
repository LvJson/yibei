package com.ts.lys.yibei.utils;

import android.app.Activity;

import com.ts.lys.yibei.ui.activity.MainActivity;

import java.util.Stack;

/**
 * Created by LYS on 2016/10/18.
 */
public class CloseAllActivity {
    private static Stack<Activity> mActivityStack = new Stack<Activity>();
    private static CloseAllActivity instance = new CloseAllActivity();

    private CloseAllActivity() {
    }

    public static CloseAllActivity getScreenManager() {
        return instance;
    }

    // 弹出当前activity并销毁
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    // 将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    // 退出栈中所有Activity
    public void clearAllActivity() {
        while (!mActivityStack.isEmpty()) {
            Activity activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    // 退出栈中除了MainActivity的所有Activity
    public void clearAllApartFromMainActivity() {
        while (!mActivityStack.isEmpty()) {
            Activity activity = mActivityStack.pop();
            if (activity != null && !(activity instanceof MainActivity)) {
                activity.finish();
            }
        }
    }

    public Activity getFirstActivity() {
        if (mActivityStack.size() > 0)
            return mActivityStack.get(mActivityStack.size() - 1);
        else return null;
    }

    public boolean mainActivityExit() {
        for (Activity activity : mActivityStack) {
            if (activity instanceof MainActivity) {
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            clearAllActivity();
        } catch (Exception e) {
        }
        System.exit(0);
        System.gc();
    }
}
