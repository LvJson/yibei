package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.KeyboardLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/9.
 */

public class BindAccountActivity extends BaseActivity {


    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    KeyboardLayout keyboardLayout;
    @Bind(R.id.et_mt4_account)
    EditText etMTAccount;
    @Bind(R.id.et_mt_password)
    EditText etMTPassword;
    @Bind(R.id.et_login_server)
    EditText etloginServer;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);
        ButterKnife.bind(this);

        initView();
        initListener();
    }


    private void initView() {
        setBackButton();
        setTitle(getString(R.string.choose_boker));
        setStatusBarStatus();

    }

    private void initListener() {

        keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardLayout.KEYBOARD_STATE_SHOW) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 300);
                }
            }
        });
    }


    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String mtAccount = etMTAccount.getText().toString().trim();
                String mtPassword = etMTPassword.getText().toString().trim();
                String mtServer = etloginServer.getText().toString().trim();
                verifyData(mtAccount, mtPassword, mtServer);
                break;
        }
    }

    /**
     * 校验输入内容是否合格
     */
    private boolean verifyData(String account, String password, String mtServer) {

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(mtServer)) {
            showToast(getString(R.string.incomplete_info));
            return false;
        }

        return true;
    }

}
