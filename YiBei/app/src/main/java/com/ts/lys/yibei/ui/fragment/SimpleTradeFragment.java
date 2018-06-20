package com.ts.lys.yibei.ui.fragment;

import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.customeview.ChooseTimesLayout;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class SimpleTradeFragment extends BaseFragment {
    @Bind(R.id.tv_free_margin)
    TextView tvFreeMargin;
    @Bind(R.id.add_delete_view)
    AddDeleteView addDeleteView;
    @Bind(R.id.choose_times_layout)
    ChooseTimesLayout chooseTimesLayout;


    QuotationsActivity parentActivity;

    private float transQuanty = 0.01f;// TODO 步长，由后台决定

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_simple_trade;
    }

    @Override
    protected void initBaseView() {
        initView();
        initListener();
    }

    private void initView() {
        parentActivity = (QuotationsActivity) getActivity();
        addDeleteView.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, 0);
        addDeleteView.setnumber(transQuanty);
        chooseTimesLayout.setBaseTimes(transQuanty);

    }

    private void initListener() {
        addDeleteView.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                double getnumber = addDeleteView.getnumber();
                getnumber += transQuanty;
                addDeleteView.setnumber(getnumber);


            }

            @Override
            public void onDelClick() {
                double getnumber = addDeleteView.getnumber();
                getnumber -= transQuanty;
                addDeleteView.setnumber(getnumber);
                if (getnumber <= 0)
                    addDeleteView.setOnClickStatus(false);

            }
        });

        parentActivity.keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardLayout.KEYBOARD_STATE_HIDE) {
                    addDeleteView.setEditTextStatus(false);
                }

            }
        });

        chooseTimesLayout.setOnItemClickListener(new ChooseTimesLayout.OnItemClickListener() {
            @Override
            public void onItemClick(float times) {

                //此处需要判断选择的步长是否大于后台给定的步长，大于的话有效，否则无效
                if (times == 0) {
                    showToast("最低手数" + transQuanty + "手");
                } else
                    addDeleteView.setnumber(times);

            }
        });

    }

}
