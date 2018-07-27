package com.ts.lys.yibei.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.tv_password)
    TextView tvPassword;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    KeyboardLayout keyboardLayout;

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
     * 密码
     */
    private String password;

    private boolean[] status = new boolean[3];

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
                Logger.e("position", position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editTextTouchListener();

    }

    /**
     * EditText触摸监听
     */
    private void editTextTouchListener() {
        etMail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int one = (int) (0.1124 * BaseUtils.getScreenHeight(getApplicationContext()));
                scrollViewScrollTo(one);
                return false;
            }
        });
        etPhoneNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scrollViewScrollTo(scrollView.getHeight());

                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollViewScrollTo(scrollView.getHeight());

                return false;
            }
        });
    }

    /**
     * 校验输入内容
     */
    private void verifyText(int position) {

        switch (position) {
            case 0:
                if (phoneNum != null) {

                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

    }

    /**
     * 输入框内容是否为空
     */
    private boolean haveNull() {
        if (TextUtils.isEmpty(mailAddre) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password))
            return true;
        else
            return false;

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
        switch (view.getId()) {
            case R.id.iv_delete_mail:
                break;
            case R.id.iv_delete_phone:
                break;
            case R.id.tv_agree:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
