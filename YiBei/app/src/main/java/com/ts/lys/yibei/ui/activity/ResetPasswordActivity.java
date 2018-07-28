package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class ResetPasswordActivity extends BaseActivity {

    @Bind(R.id.et_mail_box)
    EditText etMailBox;
    @Bind(R.id.btn_reset_pass)
    Button btnResetPass;

    private String mailAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();

        initListener();
    }


    private void initView() {
        setBackButton();
        setStatusBarStatus();
    }

    private void initListener() {

        etMailBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mailAdd = etMailBox.getText().toString().trim();
                if (TextUtils.isEmpty(mailAdd))
                    btnResetPass.setEnabled(false);
                else
                    btnResetPass.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.iv_delete, R.id.btn_reset_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                etMailBox.setText(null);
                break;
            case R.id.btn_reset_pass:
                resetPassWord();
                break;
        }
    }

    private void resetPassWord() {
        Map<String, String> map = new HashMap<>();
        map.put("email", mailAdd);

        showCustomProgress();
        CustomHttpUtils.getServiceDatas(map, UrlContents.MODIFYPASSWD, className, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                disCustomProgress();
                showToast(getString(R.string.net_error));
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
                        showToast(errMsg);
                        //TODO 重置密码邮箱发送成功
                        finish();
                    } else
                        showToast(errMsg);
                }

            }
        });
    }
}
