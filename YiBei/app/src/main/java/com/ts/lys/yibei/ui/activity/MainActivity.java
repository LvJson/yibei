package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.FragmentTabHost;
import com.ts.lys.yibei.ui.fragment.HomeFragment;
import com.ts.lys.yibei.ui.fragment.InfomationFragment;
import com.ts.lys.yibei.ui.fragment.MarketFragment;
import com.ts.lys.yibei.ui.fragment.MineFragment;
import com.ts.lys.yibei.ui.fragment.OrderFragment;
import com.ts.lys.yibei.utils.CloseAllActivity;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;

import butterknife.Bind;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends BaseFragmentActivity implements TabHost.OnTabChangeListener {

    @Bind(R.id.real_tab_content)
    FrameLayout realTabContent;
    @Bind(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;


    private Class fragmentArray[] = {HomeFragment.class, MarketFragment.class, InfomationFragment.class, OrderFragment.class, MineFragment.class};

    private String mTextviewArray[] = {"首页", "行情", "资讯", "订单", "我的"};
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_market_btn, R.drawable.tab_infomation_btn, R.drawable.tab_order_btn, R.drawable.tab_mine_btn};


    private int clickNum = 0;

    private boolean isQuitApp = false;

    private Socket socket;

    {
        try {
            //1.初始化socket.io，设置链接
            socket = IO.socket(UrlContents.SOCKETIO_URL);
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        Logger.e("userId", userId + "  accessToken: " + accessToken);
//        mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mTabHost.setCurrentTabByTag(mTextviewArray[2]);
//                updateTab(mTabHost);
//
//            }
//        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.recycler_item_selecter);
        }
        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.textview);
        tv.setTextColor(this.getResources().getColor(R.color.main_color));
        mTabHost.setOnTabChangedListener(this);
        mTabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);

        socket.connect();
        socket.on("quote", socketIoListener);

    }

    //监听回调
    private Emitter.Listener socketIoListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Logger.e("quote-main", args[0].toString());
            quota(args[0].toString());
        }
    };

    private void quota(String json) {
        EventBus.getDefault().post(new EventBean(EventContents.REAL_TIME_DATA, json));
    }

    public void changeAcc() {
        String accType = SpUtils.getString(this, BaseContents.TYPE, "");
        if (TextUtils.isEmpty(accType) || accType.equals("0"))
            SpUtils.putString(this, BaseContents.TYPE, "6");
        Logger.e("acctype", SpUtils.getString(this, BaseContents.TYPE));
        socket.emit("quote", SpUtils.getString(this, BaseContents.TYPE));
    }

    @Override
    public void onTabChanged(String tabId) {
        clickNum = 0;
        /**
         * 未登录状态点击"订单"和"我的"
         */
        if (TextUtils.isEmpty(userId) && (tabId.equals(mTextviewArray[3]) || tabId.equals(mTextviewArray[4]))) {
            startActivity(AccountLoginActivity.class);
            mTabHost.onTabChanged(mTextviewArray[1]);

        } else {
            mTabHost.setCurrentTabByTag(tabId);
            updateTab(mTabHost);
            Logger.e(TAG, tabId);
        }

        if (tabId.equals("行情")) {
            EventBus.getDefault().post(new EventBean(EventContents.MARKET_CLICK, null));
        } else
            EventBus.getDefault().post(new EventBean(EventContents.MARKET_NOT_CLICK, null));

        if (tabId.equals("订单"))
            EventBus.getDefault().post(new EventBean(EventContents.ORDER_CLICK, null));
        else
            EventBus.getDefault().post(new EventBean(EventContents.ORDER_NOT_CLICK, null));

        if (tabId.equals("首页"))
            EventBus.getDefault().post(new EventBean(EventContents.HOME_CLICK, null));
        else
            EventBus.getDefault().post(new EventBean(EventContents.HOME_NOT_CLICK, null));

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    /**
     * 更新Tab标签的颜色，和字体的颜色
     *
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.textview);
            if (tabHost.getCurrentTab() == i) {//选中
                tv.setTextColor(this.getResources().getColor(R.color.main_color));
            } else {//不选中
                tv.setTextColor(this.getResources().getColor(R.color.two_text_color));
            }
        }
    }

    /**
     * 跳转到某个Tag界面
     *
     * @param tagName
     * @param secondTab : 二级tab的位置
     */
    public void goSomeTab(final String tagName, final int secondTab) {
        mTabHost.onTabChanged(tagName);

        if (tagName.equals(mTextviewArray[2])) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InfomationFragment infomationFragment = (InfomationFragment) getSupportFragmentManager().findFragmentByTag(tagName);
                    if (infomationFragment != null)
                        infomationFragment.setCurrentPosition(secondTab);

                }
            }, 300);
        }

        /**
         *跳转到订单界面，并延迟0.5秒后定位到订单中的某个tab
         */
        if (tagName.equals(mTextviewArray[3])) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag(tagName);
                    if (orderFragment != null)
                        orderFragment.setCurrentPosition(secondTab);
                }
            }, 300);

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 有新的开仓
         */
        if (event.getTagOne().equals(EventContents.NEW_TRADING)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goSomeTab("订单", 0);
                }
            }, 300);
        } else if (event.getTagOne().equals(EventContents.NEW_PENDING)) {//有新的挂单
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goSomeTab("订单", 1);
                }
            }, 300);
        } else if (event.getTagOne().equals(EventContents.ALL_MAIN_REFRESH)) {
            refreshAllData();
        } else if (event.getTagOne().equals(EventContents.DROP_OUT_SUCCESS)) {
            isQuitApp = true;
        } else if (event.getTagOne().equals(EventContents.FORCE_QUIT_APP)) {

            SpUtils.cleanSP(this);
            CloseAllActivity.getScreenManager().clearAllApartFromMainActivity();
            goSomeTab(mTextviewArray[0], 0);
            refreshAllData();
            startActivity(AccountLoginActivity.class);


        }
    }

    /**
     * 刷新所有一级界面数据
     */
    private void refreshAllData() {
        changeAcc();
        initBaseData();
        //一级界界面全局刷新
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[0]);
        MarketFragment marketFragment = (MarketFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[1]);
        OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[3]);
        MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[4]);

        if (homeFragment != null)
            homeFragment.refreshData();
        if (marketFragment != null)
            marketFragment.refreshData();
        if (orderFragment != null)
            orderFragment.setCurrentPosition(0);
        if (mineFragment != null)
            mineFragment.refreshData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        clickNum = 0;
        changeAcc();
        if (isQuitApp) {
            mTabHost.onTabChanged(mTextviewArray[1]);
            isQuitApp = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickNum++;
            showToast("再按一次将退出应用");
            if (clickNum == 2) {
                CloseAllActivity.getScreenManager().AppExit();
            }
            return true;
        }
        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        socket.disconnect();
        socket.off("quote", socketIoListener);
    }
}
