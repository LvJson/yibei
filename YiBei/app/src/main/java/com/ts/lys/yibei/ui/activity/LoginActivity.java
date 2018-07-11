package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.KeyboardLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/9.
 */

public class LoginActivity extends BaseActivity {


    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    KeyboardLayout keyboardLayout;
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
        initListener();
    }


    private void initView() {
        setBackButton();

    }

    private void initListener() {

        keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardLayout.KEYBOARD_STATE_SHOW) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getHeight());
                        }
                    }, 300);
                }
            }
        });
    }


    @OnClick({R.id.tv_get_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                MyCountDownTimer timer = new MyCountDownTimer(60000, 1000);
                timer.start();
                break;
            case R.id.btn_login:
                String phoneNum = etPhoneNum.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                verifyData(phoneNum, verifyCode);
                break;
        }
    }

    /**
     * 校验手机号和验证码是否合格
     */
    private boolean verifyData(String phone, String code) {

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
            showToast(getString(R.string.incomplete_info));
            return false;
        }

        if (phone.length() != 11) {
            showToast(getString(R.string.illegal_phone));
            return false;
        }

        return true;
    }

    /**
     * 计时器
     */
    class MyCountDownTimer extends CountDownTimer {

        MyCountDownTimer(long millisInfuture, long countDownInterval) {
            super(millisInfuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setEnabled(false);
            tvGetCode.setTextColor(getResources().getColor(R.color.two_text_color));
            tvGetCode.setText(millisUntilFinished / 1000 + "s重新获取");
        }

        @Override
        public void onFinish() {
            tvGetCode.setTextColor(getResources().getColor(R.color.main_color));
            tvGetCode.setEnabled(true);
            tvGetCode.setText("获取验证码");

        }
    }
}
