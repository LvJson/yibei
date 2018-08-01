package com.ts.lys.yibei.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.AccountServiceBean;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
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
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.spinner)
    Spinner spinner;

    private String mt4Acc;
    private String mt4Pass;
    private String loginServer;
    private String[] mItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loginServer = (String) adapterView.getSelectedItem();
                haveNull();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        map.put("accType", "6");//TODO  账户类型

        showCustomProgress();
        CustomHttpUtils.getServiceDatas(map, UrlContents.BROKER_LOGINSHOW, className + "2", new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                disCustomProgress();
                showToast(getString(R.string.net_error));
            }

            @Override
            public void success(String response, int id) {
                disCustomProgress();
                if (response != null) {
                    new JsonAnalysisUtils<AccountServiceBean>(AccountServiceBean.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<AccountServiceBean>() {
                        @Override
                        public void success(AccountServiceBean accountServiceBean) {
                            List<String> servers = accountServiceBean.getData().getServers();
                            if (servers != null && servers.size() > 0) {
                                mItem = new String[servers.size()];
                                loginServer = servers.get(0);
                                for (int i = 0; i < servers.size(); i++) {
                                    mItem[i] = servers.get(i);
                                }
                                ArrayAdapter adapter = new ArrayAdapter(BindAccountActivity.this, R.layout.spinner_item, mItem);
                                adapter.setDropDownViewResource(R.layout.dropdown_stytle);
                                spinner.setAdapter(adapter);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                                    spinner.setDropDownVerticalOffset(100);
                            }
                        }

                        @Override
                        public void fail(String str) {
                            showToast(str);
                        }
                    });
                }
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
        map.put("accType", "6");//TODO  账户类型
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
                        finish();
                        EventBus.getDefault().post(new EventBean(EventContents.ALL_MAIN_REFRESH, null));

                    } else
                        showToast(errMsg);
                }

            }
        });

    }


}
