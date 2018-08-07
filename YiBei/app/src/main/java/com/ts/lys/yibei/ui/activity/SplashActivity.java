package com.ts.lys.yibei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;
import com.ts.lys.yibei.R;

/**
 * Created by jcdev1 on 2018/3/10.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTranslucent(this, 0);
//        final boolean isFirst = SpUtils.getBoolean(this, "firstOpen", true);

        if (App.tag == 0) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
//                    if (isFirst) {
//                        startActivity(new Intent(SplashActivity.this, WelcomeGuideActivity.class));
//                        finish();
//                    } else {
//
//                    }
                }
            }, 2000);

            App.tag = 1;
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }


    }
}
