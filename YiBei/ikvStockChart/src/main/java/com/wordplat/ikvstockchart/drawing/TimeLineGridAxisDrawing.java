/*
 * Copyright (C) 2017 WordPlat Open Source Project
 *
 *      https://wordplat.com/InteractiveKLineView/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <p>TimeLineGridAxisDrawing</p>
 * <p>Date: 2017/3/10</p>
 *
 * @author afon
 */

public class TimeLineGridAxisDrawing implements IDrawing {

    private Paint xLabelPaint; // X 轴标签的画笔
    private Paint yLabelPaint; // Y 轴标签的画笔
    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint gridPaint; // k线图网格线画笔
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics(); // 用于 labelPaint 计算文字位置
    //    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00000");//++++++++++++++++++++
    private DecimalFormat decimalFormatter;//++++++++++++++++++++

    private final DecimalFormat decimalFormatterTwo = new DecimalFormat("0.00");//++++++++++++++++++++++++++


    private final RectF chartRect = new RectF(); // 分时图显示区域
    private AbstractRender render;
    private SizeColor sizeColor;

    private final float[] pointCache = new float[2];
    private final float[] valueCache = new float[5];
    private float lineHeight;
    private float lineWidth;

    private int entrySetSize;

    private int tag;//用于判断是日分时还是分时  -1:分时 0：日分时+++++++++++++++++++++
    private int digits;//++++++++++++小数点后位数
    private int intDigit;//+++++++++++小数点及小数点前位数
    private String measureStr;//++++++++

    //++++++++++++++++++++++++++++++++++++++++++
    public TimeLineGridAxisDrawing() {

    }

    public TimeLineGridAxisDrawing(int tag, int digits, int intDigit) {
        this.tag = tag;
        this.digits = digits;
        this.intDigit = intDigit;
        StringBuilder builder = new StringBuilder();
        builder.append("0.0");
        for (int i = 0; i < digits - 1; i++) {
            builder.append("0");
        }
        decimalFormatter = new DecimalFormat(builder.toString());

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < digits + intDigit; i++) {
            str.append("0");
        }
        measureStr = str.toString();
    }
    //++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = render;
        this.sizeColor = render.getSizeColor();

        if (xLabelPaint == null) {
            xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        xLabelPaint.setTextSize(sizeColor.getXLabelSize());
        xLabelPaint.setColor(sizeColor.getXLabelColor());
        xLabelPaint.getFontMetrics(fontMetrics);

        if (yLabelPaint == null) {
            yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        yLabelPaint.setTextSize(sizeColor.getYLabelSize());

        if (axisPaint == null) {
            axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            axisPaint.setStyle(Paint.Style.STROKE);
        }

        if (gridPaint == null) {
            gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            gridPaint.setStyle(Paint.Style.STROKE);
        }

        axisPaint.setStrokeWidth(sizeColor.getAxisSize());
        axisPaint.setColor(sizeColor.getAxisColor());

        gridPaint.setStrokeWidth(sizeColor.getGridSize());
        gridPaint.setColor(sizeColor.getGridColor());
        //++++++++++++++++++++++++
//        if (render.getEntrySet() != null && render.getEntrySet().getEntryList().size() > 0) {
//            double yLabel = render.getEntrySet().getEntryList().get(0).getClose();
//            float width = yLabelPaint.measureText(decimalFormatter.format(yLabel));
//            contentRect.right = contentRect.right - width;//+++++++++++++
//        } else {
//            float width = yLabelPaint.measureText("0.00000");
//            contentRect.right = contentRect.right - width;//+++++++++++++
//        }

        if (intDigit == -1 || digits == -1) {
            float width = yLabelPaint.measureText("0.00000");
            contentRect.right = contentRect.right - width;//+++++++++++++

        } else {
            float width = yLabelPaint.measureText(measureStr);
            contentRect.right = contentRect.right - width;//+++++++++++++
        }
        //+++++++++++++++++++++++++

        chartRect.set(contentRect);

        lineHeight = chartRect.height() / 4;
        lineWidth = chartRect.width() / 4;
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        entrySetSize = entrySet.getEntryList().size();
        // 绘制 最外层大框框
        canvas.drawRect(chartRect, axisPaint);

        // 绘制 三条横向网格线
        for (int i = 0; i < 3; i++) {
            float lineTop = chartRect.top + (i + 1) * lineHeight;
            canvas.drawLine(chartRect.left, lineTop, chartRect.right, lineTop, gridPaint);
        }

        int entrySize = entrySet.getEntryList().size();//++++++++++
        int total = entrySize / 5;//++++++++++
        String xFirstTag = entrySet.getEntryList().get(0).getXLabel();//+++++++++++++++
        long time = 60 * 60 * 1000;
        long xMillion = Long.parseLong(xFirstTag);
        for (int i = 0; i < 5; i++) {
            float lineLeft = chartRect.left + i * lineWidth;
            if (i != 0 && i != 4) {
                // 绘制 三条竖向网格线
                canvas.drawLine(lineLeft, chartRect.top, lineLeft, chartRect.bottom, gridPaint);
                xLabelPaint.setTextAlign(Paint.Align.CENTER);
            } else if (i == 0) {
                xLabelPaint.setTextAlign(Paint.Align.LEFT);
            } else {
                xLabelPaint.setTextAlign(Paint.Align.RIGHT);
            }
            //+++++++++++++++++++++++++++++
            String xLabal = "";
            if (tag == -1) {
                if (i == 0) {
                    xLabal = millionToDate(xMillion) + ":00";//++++++++++
                } else if (i == 1) {
                    xLabal = millionToDate(xMillion + 2 * time) + ":00";//++++++++++

                } else if (i == 2) {
                    xLabal = millionToDate(xMillion + 4 * time) + ":00";//++++++++++
                } else if (i == 3) {
                    xLabal = millionToDate(xMillion + 6 * time) + ":00";//++++++++++
                } else if (i == 4) {
                    xLabal = millionToDate(xMillion + 8 * time) + ":00";//++++++++++
                }

            } else if (tag == 0) {
                if (i == 0) {
                    xLabal = "08:00";//++++++++++
                } else if (i == 1) {
                    xLabal = "14:00";//++++++++++
                } else if (i == 2) {
                    xLabal = "20:00";//++++++++++
                } else if (i == 3) {
                    xLabal = "02:00";//++++++++++
                } else if (i == 4) {
                    xLabal = "08:00";//++++++++++
                }

            }
            //+++++++++++++++++++++++++++++

            // 绘制 X 轴 label
            if (entrySetSize > 0) {
                canvas.drawText(
                        xLabal,
                        lineLeft,
                        chartRect.bottom + render.getSizeColor().getXLabelSize(),
                        xLabelPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas canvas) {
        if (entrySetSize < 1) {
            return;
        }
        // 绘制 Y 轴左边 label
        for (int i = 0; i < 5; i++) {
            float lineTop = chartRect.top + i * lineHeight;
            pointCache[1] = lineTop;
            render.invertMapPoints(pointCache);
            String value = decimalFormatter.format(pointCache[1]);
            if (i == 0) {
                pointCache[0] = lineTop - fontMetrics.top;
            } else if (i == 4) {
                pointCache[0] = lineTop - fontMetrics.bottom;
            } else {
                pointCache[0] = lineTop + fontMetrics.bottom;
            }

            if (i == 2) {
                yLabelPaint.setColor(sizeColor.getNeutralColor());
            } else if (i > 2) {
                yLabelPaint.setColor(sizeColor.getDecreasingColor());
            } else {
                yLabelPaint.setColor(sizeColor.getIncreasingColor());
            }
            yLabelPaint.setTextAlign(Paint.Align.LEFT);
//            canvas.drawText(value, chartRect.left + 5, pointCache[0], yLabelPaint);//++++++++++++++++++++++
            canvas.drawText(value, chartRect.right + 10, pointCache[0], yLabelPaint);
            valueCache[i] = pointCache[1];
        }

        // 绘制 Y 轴右边 label
        for (int i = 0; i < 5; i++) {
            float percent = (valueCache[i] - valueCache[2]) / valueCache[2] * 100;
            String value = decimalFormatterTwo.format(percent);
            float lineTop = chartRect.top + i * lineHeight;

            if (i == 0) {
                pointCache[0] = lineTop - fontMetrics.top;
            } else if (i == 4) {
                pointCache[0] = lineTop - fontMetrics.bottom;
            } else {
                pointCache[0] = lineTop + fontMetrics.bottom;
            }

            if (i == 2) {
                yLabelPaint.setColor(sizeColor.getNeutralColor());
            } else if (i > 2) {
                yLabelPaint.setColor(sizeColor.getDecreasingColor());
            } else {
                yLabelPaint.setColor(sizeColor.getIncreasingColor());
            }
            yLabelPaint.setTextAlign(Paint.Align.LEFT);//++++++++  RIGHT-->LEFT
//            canvas.drawText(value + "%", chartRect.right - 5 + 95, pointCache[0], yLabelPaint);//+++++++++++++++  +95
            canvas.drawText(value + "%", chartRect.left + 5, pointCache[0], yLabelPaint);//+++++++++++++++

        }
    }


    /**
     * 毫秒转日期++++++++++++++++
     *
     * @param million
     * @return
     */
    public static String millionToDate(long million) {
        DateFormat formatter = new SimpleDateFormat("HH");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }
}
