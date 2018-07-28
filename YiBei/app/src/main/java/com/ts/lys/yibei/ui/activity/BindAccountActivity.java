package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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

    private String mt4Acc;
    private String mt4Pass;
    private String loginServer;

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

        etMTAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mt4Acc = etMTAccount.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMTPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mt4Pass = etMTPassword.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etloginServer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginServer = etloginServer.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void haveNull() {

        if (TextUtils.isEmpty(mt4Acc) || TextUtils.isEmpty(mt4Pass) || TextUtils.isEmpty(loginServer)) {
            btnLogin.setEnabled(false);
        } else
            btnLogin.setEnabled(true);
    }

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                bindServer();
                break;
        }
    }

    private void bindServer() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        map.put("password", mt4Pass);
        map.put("accType", "");//TODO  账户类型
        map.put("mt4Id", mt4Acc);
        map.put("server", loginServer);

        showCustomProgress();
        CustomHttpUtils.getServiceDatas(map, UrlContents.BROKER_BIND, className, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                showToast(getString(R.string.net_error));
                disCustomProgress();
            }

            @Override
            public void success(String response, int id) {
                disCustomProgress();
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    String errCode = "";
                    String errMsg = "";

                    if (jsonObject.has("err_code") && !jsonObject.get("err_code").isJsonNull())
                        errCode = jsonObject.get("err_code").getAsString();
                    if (jsonObject.has("err_msg") && !jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();

                    if (errCode.equals("0")) {
                        showToast(getString(R.string.mt4_bind_success));
                        //TODO MT4账户绑定成功

                    } else
                        showToast(errMsg);
                }

            }
        });

    }


}
