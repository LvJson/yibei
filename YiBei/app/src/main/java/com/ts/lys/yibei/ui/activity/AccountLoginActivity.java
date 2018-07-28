package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.LoginModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;
import com.ts.lys.yibei.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class AccountLoginActivity extends BaseActivity {
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    KeyboardLayout keyboardLayout;
    @Bind(R.id.tv_one)
    TextView tvOne;
    @Bind(R.id.ll_two)
    LinearLayout llTwo;

    /**
     * 账号
     */
    private String accountNum;
    /**
     * 密码
     */
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        EventBus.getDefault().register(this);
        initView();
        initListener();
    }

    private void initView() {
        setBackButton();
        setStatusBarStatus();
    }

    private void initListener() {
        keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardLayout.KEYBOARD_STATE_SHOW) {
                    tvOne.setVisibility(View.INVISIBLE);
                    setTitle(getString(R.string.account_login));
                } else {
                    tvOne.setVisibility(View.VISIBLE);
                    setTitle("");
                }
            }
        });

        etPhoneNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, (int) (0.1124 * BaseUtils.getScreenHeight(getApplicationContext())));
                    }
                }, 300);
                return false;
            }
        });


        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int one = (int) (0.052 * BaseUtils.getScreenHeight(getApplicationContext()));
                final int three = llTwo.getBottom() - one - scrollView.getHeight();
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, three);
                    }
                }, 300);
                return false;
            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                accountNum = etPhoneNum.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = etPassword.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * 验证账号和密码是否为空
     *
     * @return
     */
    private void haveNull() {
        if (TextUtils.isEmpty(accountNum) || TextUtils.isEmpty(password)) {

            btnLogin.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);

        }

    }

    @OnClick({R.id.tv_forgit, R.id.btn_login, R.id.tv_regist, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgit:
                startActivity(ResetPasswordActivity.class);
                break;
            case R.id.btn_login:
                loginApp();
                break;
            case R.id.tv_regist:
                startActivity(RegistActivity.class);
                break;
            case R.id.iv_delete:
                etPhoneNum.setText(null);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 注册成功
         */
        if (event.getTagOne().equals(EventContents.REGIST_SUCCESS)) {

            etPhoneNum.setText(SpUtils.getString(getApplicationContext(), BaseContents.MAIL_BOX));
        }
    }


    private void loginApp() {
        Map<String, String> map = new HashMap<>();
        map.put("email", accountNum);
        map.put("password", password);

        showCustomProgress();

        CustomHttpUtils.getServiceDatas(map, UrlContents.LOGIN, className, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                disCustomProgress();
                showToast(getString(R.string.net_error));
            }

            @Override
            public void success(String response, int id) {
                disCustomProgress();
                new JsonAnalysisUtils<LoginModel>(LoginModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<LoginModel>() {
                    @Override
                    public void success(LoginModel loginModel) {
                        LoginModel.DataBean.UserBean user = loginModel.getData().getUser();
                        SpUtils.putBoolean(getApplicationContext(), BaseContents.FIRST_LOGIN, user.isFirstLogin());
                        SpUtils.putString(getApplicationContext(), BaseContents.ACCESS_TOKEN, user.getAccessToken());
                        SpUtils.putInt(getApplicationContext(), BaseContents.TYPE, user.getType());
                        SpUtils.putString(getApplicationContext(), BaseContents.USERID, String.valueOf(user.getUserId()));
                        SpUtils.putString(getApplicationContext(), BaseContents.INVITE_CODE, user.getInviteCode());
                        //TODO 登录成功，通知刷新界面

                        finish();
                    }

                    @Override
                    public void fail(String str) {
                        showToast(str);
                    }
                });


            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CustomHttpUtils.cancelHttp(className);

    }
}
