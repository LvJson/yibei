package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisSuccessActivity extends BaseActivity {

    @Bind(R.id.tv_account)
    TextView tvAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_success);

        tvAccount.setText("  " + SpUtils.getString(getApplicationContext(), BaseContents.PHONE_NUMBER) + "  ");
        setStatusBarStatus();

    }

    @OnClick(R.id.btn_finish)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventBean(EventContents.REGIST_SUCCESS, null));
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new EventBean(EventContents.REGIST_SUCCESS, null));
            return true;
        }
        return false;

    }
}
