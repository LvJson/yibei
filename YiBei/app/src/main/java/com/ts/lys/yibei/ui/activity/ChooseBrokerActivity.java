package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.ExpandTextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/10.
 */

public class ChooseBrokerActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.expand_text_view)
    ExpandTextView expandTextView;
    @Bind(R.id.btn_go)
    Button btnGo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_broker);

        initView();
    }

    private void initView() {
        setStatusBarStatus();
        setTitle(getString(R.string.choose_boker));
        setBackButton();
    }

    @OnClick({R.id.btn_go, R.id.tv_bind_mt, R.id.iv_doubt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_go:
                break;
            case R.id.tv_bind_mt:
                startActivity(BindAccountActivity.class);
                break;
            case R.id.iv_doubt:
                break;
        }
    }
}
