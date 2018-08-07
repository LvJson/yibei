package com.ts.lys.yibei.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.ButtonUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RegistActivity extends BaseActivity {

    @Bind(R.id.tv_one)
    TextView tvOne;
    @Bind(R.id.et_mail)
    EditText etMail;
    @Bind(R.id.tv_mail_error)
    TextView tvMailError;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;
    @Bind(R.id.tv_phone_error)
    TextView tvPhoneError;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.tv_password_error)
    TextView tvPasswordRrror;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    KeyboardLayout keyboardLayout;
    @Bind(R.id.tv_code)
    TextView tvCode;

    private String[] mItem;
    /**
     * 邮箱地址
     */
    private String mailAddre;
    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * 手机号码归属地
     */
    private int phoneFrom = 0;
    /**
     * 密码
     */
    private String password;
    /**
     * 地区编码
     */
    private String code = "+86";

    private boolean[] status = new boolean[3];
    private static String[] codeStr = new String[]{"+86", "+852"};

    private static String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{10,15}$";
    private static String emailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        initView();
        initListener();

    }


    private void initView() {
        setBackButton();
        setStatusBarStatus();
        mItem = new String[]{getString(R.string.china_mainland), getString(R.string.china_hongkong)};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, mItem);
        adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        spinner.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            spinner.setDropDownVerticalOffset(100);

    }

    private void initListener() {

        keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardLayout.KEYBOARD_STATE_SHOW) {
                    tvOne.setVisibility(View.INVISIBLE);
                    setTitle(getString(R.string.regist));
                } else {
                    tvOne.setVisibility(View.VISIBLE);
                    setTitle("");
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                code = codeStr[position];
                tvCode.setText(code);
                phoneFrom = position;
                if (position == 0)
                    etPhoneNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                else
                    etPhoneNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editTextTouchListener();
        editTextOnTextChangeListener();

    }

    /**
     * EditText触摸监听
     */
    private void editTextTouchListener() {
        etMail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tvMailError.setVisibility(View.GONE);
                int one = (int) (0.1124 * BaseUtils.getScreenHeight(getApplicationContext()));
                scrollViewScrollTo(one);
                return false;
            }
        });
        etPhoneNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tvPhoneError.setVisibility(View.GONE);
                scrollViewScrollTo(scrollView.getHeight());

                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                tvPasswordRrror.setVisibility(View.GONE);
                scrollViewScrollTo(scrollView.getHeight());

                return false;
            }
        });
    }


    /**
     * 输入框输入内容变化监听
     */
    private void editTextOnTextChangeListener() {

        etMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mailAddre = etMail.getText().toString().trim();
                haveNull();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNum = etPhoneNum.getText().toString().trim();
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

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                haveNull();
            }
        });
    }


    /**
     * 输入框内容是否为空
     */
    private void haveNull() {
        if (TextUtils.isEmpty(mailAddre) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password) || !checkbox.isChecked())
            btnNext.setEnabled(false);
        else
            btnNext.setEnabled(true);

    }


    private void scrollViewScrollTo(final int toY) {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, toY);
            }
        }, 300);
    }

    @OnClick({R.id.iv_delete_mail, R.id.iv_delete_phone, R.id.tv_agree, R.id.btn_next})
    public void onViewClicked(View view) {
        if (ButtonUtils.isFastDoubleClick(view.getId(), 1500)) return;
        switch (view.getId()) {
            case R.id.iv_delete_mail:
                etMail.setText(null);
                break;
            case R.id.iv_delete_phone:
                etPhoneNum.setText(null);
                break;
            case R.id.tv_agree:
                // 注册协议
                Intent intentAgree = new Intent(this, WebViewActivity.class);
                intentAgree.putExtra("url", UrlContents.REGISTER_AGREE);
                startActivity(intentAgree);
                break;
            case R.id.btn_next:
                hideSoftWindow(view);
                if (verifyText())
                    regist();
                break;
        }
    }

    /**
     * 校验输入内容是否合法
     */
    private boolean verifyText() {

        boolean isRightMail = BaseUtils.matchString(mailAddre, emailRegex);
        boolean isRightPassworld = BaseUtils.matchString(password, passRegex);
        boolean isRightPhoneNum = verifyPhone(phoneFrom, phoneNum);

        if (isRightMail && isRightPassworld && isRightPhoneNum) {
            return true;
        } else {

            if (!isRightMail)
                tvMailError.setVisibility(View.VISIBLE);
            if (!isRightPassworld)
                tvPasswordRrror.setVisibility(View.VISIBLE);
            if (!isRightPhoneNum)
                tvPhoneError.setVisibility(View.VISIBLE);
            return false;
        }

    }

    /**
     * 校验手机号
     *
     * @param tag
     * @param phoneNum
     * @return
     */
    private boolean verifyPhone(int tag, String phoneNum) {

        if (tag == 0) {//中国大陆

            if (phoneNum.length() == 11)
                return true;
            else
                return false;

        } else {//香港

            if (phoneNum.length() == 8)
                return true;
            else
                return false;
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    private void hideSoftWindow(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 1);
    }


    private void regist() {
        Map<String, String> map = new HashMap<>();
        map.put("email", mailAddre);
        map.put("password", password);
        map.put("telephone", "(" + code + ")" + phoneNum);

        showCustomProgress();
        CustomHttpUtils.getServiceDatas(map, UrlContents.REGISTER, className, new CustomHttpUtils.ServiceStatus() {
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
                        //TODO 保存邮箱和手机号
                        SpUtils.putString(getApplicationContext(), BaseContents.MAIL_BOX, mailAddre);
                        SpUtils.putString(getApplicationContext(), BaseContents.PHONE_NUMBER, phoneNum);
                        startActivity(RegisSuccessActivity.class);
                        finish();

                    } else
                        showToast(errMsg);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomHttpUtils.cancelHttp(className);
    }
}
