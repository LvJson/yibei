package com.ts.lys.yibei.ui.fragment;

import android.support.design.widget.TabLayout;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.StockChartDatas;
import com.ts.lys.yibei.mvppresenter.KLineDataPresenter;
import com.ts.lys.yibei.mvpview.IViewKLineData;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.wordplat.ikvstockchart.InteractiveKLineView;
import com.wordplat.ikvstockchart.drawing.HighlightDrawing;
import com.wordplat.ikvstockchart.drawing.MACDDrawing;
import com.wordplat.ikvstockchart.drawing.StockIndexYLabelDrawing;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.StockMACDIndex;
import com.wordplat.ikvstockchart.marker.XAxisTextMarkerView;
import com.wordplat.ikvstockchart.marker.YAxisTextMarkerView;
import com.wordplat.ikvstockchart.render.KLineRender;
import com.wordplat.ikvstockchart.render.TimeLineRender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/21.
 */

public class KLineFragment extends BaseFragment implements IViewKLineData {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.k_line)
    InteractiveKLineView kLine;


    private String[] tabItem;

    private KLineDataPresenter kLineDataPresenter;

    private Map<String, String> map = new HashMap<>();

    QuotationsActivity parentActivity;


    /**
     * K线图历史数据
     */
    List<StockChartDatas.DataBean.QuotesBean> qb;

    private long oldTime;//报价时间
    private double oldOpenPrice;//开盘价（某个时间段的）
    private double oldHigt;//最高价（某个时间段的）
    private double oldLow;//最低价（某个时间段的）
    private double oldClose;//收盘价（某个时间段的）
    private int realDataSize = 0;//真实K线图或分时图集合大小
    private int timeLineTag = 0;//用于判断是日分时还是分时 0：日分时 1:分k线图 2：日K图
    private boolean isKLine = false;//当前展示的是否为K线图
    private long timeDiff = 1000 * 60;//记录不同时间差


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_k_line;
    }

    @Override
    protected void initBaseView() {

        initView();
        initListener();
        initData();

    }


    private void initView() {
        tabItem = new String[]{getString(R.string.time_sharing)
                , getString(R.string.one_minute)
                , getString(R.string.five_minute)
                , getString(R.string.fifteen_minute)
                , getString(R.string.thirty_minute)
                , getString(R.string.one_hour)
                , getString(R.string.four_hour)
                , getString(R.string.day_line)
                , getString(R.string.weeek_line)
                , getString(R.string.month_line)};

        for (int i = 0; i < tabItem.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabItem[i]));
        }
    }


    private void initListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchKLineStatus(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initData() {
        parentActivity = (QuotationsActivity) getActivity();
        kLineDataPresenter = new KLineDataPresenter(this);
        kLineDataPresenter.attachView(this);
        map.put("accessToken", "");
        map.put("symbol", parentActivity.symbol);
        map.put("type", "0");
        kLineDataPresenter.getKLineData(map, className);
    }

    /**
     * 切换k线图
     *
     * @param tab
     */
    private void switchKLineStatus(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            /**
             * 0：分时
             */
            case 0:
                isKLine = false;
                timeLineTag = 0;
                timeDiff = 1000 * 60;
                map.put("type", "0");
                break;
            /**
             * 1：1分
             */
            case 1:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60;
                map.put("type", "1");
                break;
            /**
             * 2：5分
             */
            case 2:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 5;
                map.put("type", "5");
                break;
            /**
             * 3：15分
             */
            case 3:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 15;
                map.put("type", "15");
                break;
            /**
             * 4：30分
             */
            case 4:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 30;
                map.put("type", "30");
                break;
            /**
             * 5：1时
             */
            case 5:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 60;
                map.put("type", "60");
                break;
            /**
             * 6：4时
             */
            case 6:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 60 * 4;
                map.put("type", "240");
                break;
            /**
             * 7：日线
             */
            case 7:
                isKLine = true;
                timeLineTag = 2;
                timeDiff = 1000 * 60 * 60 * 24;
                map.put("type", "1440");
                break;
            /**
             * 8：周线
             */
            case 8:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 60 * 24 * 7;
                map.put("type", "10080");
                break;
            /**
             * 9：月线
             */
            case 9:
                isKLine = true;
                timeLineTag = 1;
                timeDiff = 1000 * 60 * 60 * 24 * 10;
                map.put("type", "43200");
                break;

        }
        kLineDataPresenter.getKLineData(map, className);
    }

    /**
     * K线图数据
     *
     * @param kld
     */
    @Override
    public void showKLineData(StockChartDatas kld) {
        qb = kld.getData().getQuotes();

        if (getActivity() == null) {
            return;
        }
        int qbSize = qb.size();
        if (qb == null || qb.size() == 0) return;
//                        Collections.reverse(qb);
        oldTime = qb.get(qb.size() - 1).getTime();
        String d1 = BaseUtils.getDateFromMillionSeconds(oldTime);
        oldTime = BaseUtils.getMillionSecondsFromDateKLine(d1);

        oldOpenPrice = qb.get(qb.size() - 1).getOpenPrice();
        oldHigt = qb.get(qb.size() - 1).getHigh();
        oldLow = qb.get(qb.size() - 1).getLow();
        oldClose = qb.get(qb.size() - 1).getClose();

        EntrySet entrySet = new EntrySet();

//        final int stockMarkerViewHeight = BaseUtils.dip2px(getActivity(), 15);
        final int stockMarkerViewHeight = BaseUtils.dip2px(getActivity().getBaseContext(), 15);
        realDataSize = qbSize;
        for (int i = 0; i < qb.size(); i++) {
            Long dataStr = qb.get(i).getTime();
            if (timeLineTag == 0)
                entrySet.addEntry(new Entry((float) qb.get(i).getOpenPrice(), (float) qb.get(i).getHigh(), (float) qb.get(i).getLow(), (float) qb.get(i).getClose(), qb.get(i).getVol(), dataStr + ""));
            else {
                String data = "";
                if (timeLineTag == 1) {
                    data = BaseUtils.millionToMinute(dataStr);
                } else {
                    data = BaseUtils.millionToDay(dataStr);
                }
                entrySet.addEntry(new Entry((float) qb.get(i).getOpenPrice(), (float) qb.get(i).getHigh(), (float) qb.get(i).getLow(), (float) qb.get(i).getClose(), qb.get(i).getVol(), data + ""));
            }
        }
        if (timeLineTag == 0) {
            for (int i = 0; i < 1440 - qbSize; i++)
                entrySet.addEntry(new Entry((float) qb.get(qbSize - 1).getOpenPrice(), (float) qb.get(qbSize - 1).getHigh(), (float) qb.get(qbSize - 1).getLow(), (float) qb.get(qbSize - 1).getClose(), qb.get(qbSize - 1).getVol(), 0 + ""));

        } else if (timeLineTag == -1) {
            for (int i = 0; i < 480 - qbSize; i++)
                entrySet.addEntry(new Entry((float) qb.get(qbSize - 1).getOpenPrice(), (float) qb.get(qbSize - 1).getHigh(), (float) qb.get(qbSize - 1).getLow(), (float) qb.get(qbSize - 1).getClose(), qb.get(qbSize - 1).getVol(), 0 + ""));
        }
        int intDigit = 0;
        if (qb.size() > 0) {
            String str = String.valueOf(qb.get(0).getClose());
            int index = str.indexOf(".");
            if (index != -1) {
                intDigit = index + 1;
            }

        }
        if (isKLine) {//K线图数据
            kLine.setRender(new KLineRender(getActivity(), parentActivity.digits, intDigit));
            entrySet.computeStockIndex();//计算其它附带指标
            kLine.setEntrySet(entrySet);
            KLineRender kr = (KLineRender) kLine.getRender();

            // MACD
            HighlightDrawing macdHighlightDrawing = new HighlightDrawing();
            macdHighlightDrawing.addMarkerView(new YAxisTextMarkerView(BaseUtils.dip2px(getActivity(), 10f), parentActivity.digits));

            StockMACDIndex macdIndex = new StockMACDIndex();
            macdIndex.addDrawing(new MACDDrawing());
            macdIndex.addDrawing(new StockIndexYLabelDrawing());
            macdIndex.addDrawing(macdHighlightDrawing);
            macdIndex.setPaddingTop(BaseUtils.dip2px(getActivity(), 10f));
//            kr.addStockIndex(macdIndex);//指标展示

            //下面两行代码可以控制十字线两端是否有文本展示
            kr.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight, parentActivity.digits));
            kr.addMarkerView(new XAxisTextMarkerView(stockMarkerViewHeight));


        } else {//分时图数据
            kLine.setRender(new TimeLineRender(timeLineTag, realDataSize, parentActivity.digits, intDigit));
            entrySet.computeStockIndex();
            kLine.setEntrySet(entrySet);
            TimeLineRender tr = (TimeLineRender) kLine.getRender();

            tr.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight, parentActivity.digits));
            tr.addMarkerView(new XAxisTextMarkerView(stockMarkerViewHeight));
        }
        kLine.notifyDataSetChanged();

    }


    /**
     * 接收实时价格
     *
     * @param quoteBeen
     */
    public void setRealPrice(RealTimeBean quoteBeen) {
        if (getActivity() == null) return;
        /**
         * 实时修改k线图数据
         */

        Long newTime = quoteBeen.getTime();
        String n1 = BaseUtils.getDateFromMillionSeconds(newTime);

        newTime = BaseUtils.getMillionSecondsFromDateKLine(n1);

        Double market = quoteBeen.getMarket();
        String newData = BaseUtils.millionToDate(quoteBeen.getTime());

        if (newTime - oldTime < timeDiff) {
            oldHigt = Math.max(market, oldHigt);
            oldLow = Math.min(market, oldLow);
            oldClose = market;
            //更改最后一个数据
            changeLastData(newData, quoteBeen.getTime());
        } else {

            oldTime = newTime;
            oldOpenPrice = market;
            oldHigt = market;
            oldLow = market;
            oldClose = market;

            if (timeLineTag == 0) {
                map.put("type", "0");
                kLineDataPresenter.getKLineData(map, className);
            } else {
                //增加一条K线图新数据
                EntrySet entrySett = new EntrySet();
                entrySett.addEntry(new Entry((float) oldOpenPrice, (float) oldHigt, (float) oldLow, (float) oldClose, 0, newData));

                entrySett.computeStockIndex();
                List<Entry> entriess = entrySett.getEntryList();
                kLine.getRender().getEntrySet().addEntries(entriess);
                kLine.notifyDataSetChanged();
            }
        }
    }

    /**
     * 实时更改最后一条数据
     *
     * @param newData
     */
    private void changeLastData(String newData, long newDate2) {

        /**
         *  方法一：对集合认识不到位，会频繁创建对象导致内存抖动
         */
//        EntrySet entrySets = new EntrySet();
//        List<Entry> en = kLine.getRender().getEntrySet().getEntryList();
//        if (timeLineTag == 0) {
//            if (realDataSize > 0 && en.size() > 1) {
//                if (en.size() >= realDataSize)
//                    en.remove(realDataSize - 1);
//                en.add(realDataSize - 1, new Entry((float) oldOpenPrice, (float) oldHigt, (float) oldLow, (float) oldClose, 0, String.valueOf(newDate2)));
//            }
//        } else {
//            if (realDataSize > 0 && en.size() > 1) {
//                en.remove(en.size() - 1);
//                en.add(new Entry((float) oldOpenPrice, (float) oldHigt, (float) oldLow, (float) oldClose, 0, newData));
//            }
//        }
//
//        entrySets.addEntries(en);
//        entrySets.computeStockIndex();
//        kLine.setEntrySet(entrySets);
//        kLine.notifyDataSetChanged();

        /**
         *  方法二：去掉对象的频繁创建，去掉非必要指标的展示，解决了界面卡顿的问题
         */
        EntrySet entrySet = kLine.getRender().getEntrySet();
        List<Entry> en = entrySet.getEntryList();
        if (timeLineTag == 0) {
            if (realDataSize > 0 && en.size() > 1) {
                if (en.size() >= realDataSize)
                    en.remove(realDataSize - 1);
                en.add(realDataSize - 1, new Entry((float) oldOpenPrice, (float) oldHigt, (float) oldLow, (float) oldClose, 0, String.valueOf(newDate2)));
            }
        } else {
            if (realDataSize > 0 && en.size() > 1) {
                en.remove(en.size() - 1);
                en.add(new Entry((float) oldOpenPrice, (float) oldHigt, (float) oldLow, (float) oldClose, 0, newData));
            }
        }

        entrySet.computeMA();
        kLine.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        kLineDataPresenter.detachView();
    }
}
