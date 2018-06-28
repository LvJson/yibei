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

package com.wordplat.ikvstockchart.marker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

import com.wordplat.ikvstockchart.align.YMarkerAlign;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;
import com.wordplat.ikvstockchart.utils.BaseUtils;

import java.text.DecimalFormat;

/**
 * <p>YAxisTextMarkerView</p>
 * <p>Date: 2017/3/23</p>
 *
 * @author afon
 */

public class YAxisTextMarkerView implements IMarkerView {
    private static final String TAG = "YAxisTextMarkerView";

    private Paint markerTextPaint;
    private Paint markerBorderPaint;

    private final RectF contentRect = new RectF();
    private AbstractRender render;

    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00000");
    private final float[] pointCache = new float[2];
    private final int height;
    private final RectF markerInsets = new RectF(0, 0, 0, 0);
    private float inset = 0;
    private YMarkerAlign yMarkerAlign;
    private int digits = -1;

    public YAxisTextMarkerView(int height, int digits) {
        this.height = height;
        this.digits = digits;
    }

    @Override
    public void onInitMarkerView(RectF contentRect, AbstractRender render) {
        contentRect.right = contentRect.right + 120;//+++++++++++++
        this.contentRect.set(contentRect);
        this.render = render;
        final SizeColor sizeColor = render.getSizeColor();

        if (markerTextPaint == null) {
            markerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            markerTextPaint.setTextAlign(Paint.Align.CENTER);
        }

//        markerTextPaint.setColor(sizeColor.getMarkerTextColor());//+++++++++++++++++++++
        markerTextPaint.setColor(Color.WHITE);//+++++++++++++++++++++


        markerTextPaint.setTextSize(sizeColor.getMarkerTextSize());
        markerTextPaint.getFontMetrics(fontMetrics);

        if (markerBorderPaint == null) {
            markerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            markerBorderPaint.setStyle(Paint.Style.STROKE);+++++++++++++++++++++
            markerBorderPaint.setStyle(Paint.Style.FILL);

        }

        markerBorderPaint.setStrokeWidth(sizeColor.getMarkerBorderSize());
//        markerBorderPaint.setColor(sizeColor.getMarkerBorderColor());//+++++++++++++++++++++++++
        markerBorderPaint.setColor(Color.rgb(81, 82, 83));//+++++++++++++++++++++++++

        inset = markerBorderPaint.getStrokeWidth() / 2;

        yMarkerAlign = sizeColor.getYMarkerAlign();
    }

    @Override
    public void onDrawMarkerView(Canvas canvas, float highlightPointX, float highlightPointY) {
        if (contentRect.top < highlightPointY && highlightPointY < contentRect.bottom) {
            pointCache[0] = 0;
            pointCache[1] = highlightPointY;

            render.invertMapPoints(null, pointCache);
            String value = decimalFormatter.format(pointCache[1]);

            float width = markerTextPaint.measureText(value) + 50;

            highlightPointY = highlightPointY - height / 2;
            if (highlightPointY < contentRect.top) {
                highlightPointY = contentRect.top;
            }
            if (highlightPointY > contentRect.bottom - height) {
                highlightPointY = contentRect.bottom - height;
            }

            if (yMarkerAlign == YMarkerAlign.LEFT) {
                markerInsets.left = contentRect.left + inset;

            } else if (yMarkerAlign == YMarkerAlign.RIGHT) {
                markerInsets.left = contentRect.right - width + inset;

            } else if (highlightPointX < contentRect.left + contentRect.width() / 3) {
//                markerInsets.left = contentRect.right - width + inset;//right+++++++++++++++++
                markerInsets.left = contentRect.right - 120;
            } else {
                markerInsets.left = contentRect.left + inset;//left

            }
            markerInsets.top = highlightPointY + inset;
            markerInsets.right = markerInsets.left + width - inset * 2;
            markerInsets.bottom = markerInsets.top + height - inset * 2;
            canvas.drawRect(markerInsets, markerBorderPaint);//+++++++++++++++
            int textLength = getTextBounds(BaseUtils.getDigitsData(value, digits), markerTextPaint).width();

            if (highlightPointX < contentRect.left + contentRect.width() / 3) {
                canvas.drawText(BaseUtils.getDigitsData(value, digits),
                        markerInsets.left + textLength / 2 + 5,
                        (markerInsets.top + markerInsets.bottom - fontMetrics.top - fontMetrics.bottom) / 2,
                        markerTextPaint);
            } else {
                canvas.drawText(BaseUtils.getDigitsData(value, digits),
                        markerInsets.left + (markerInsets.right - markerInsets.left) / 2,
                        (markerInsets.top + markerInsets.bottom - fontMetrics.top - fontMetrics.bottom) / 2,
                        markerTextPaint);
            }
//            canvas.drawRect(markerInsets, markerBorderPaint);//++++++++++++++++++++++

            canvas.clipRect(markerInsets, Region.Op.XOR);
        }
    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }
}
