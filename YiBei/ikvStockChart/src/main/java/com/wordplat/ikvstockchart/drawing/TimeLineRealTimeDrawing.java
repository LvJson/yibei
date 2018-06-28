package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;
import com.wordplat.ikvstockchart.utils.BaseUtils;

/**
 * Created by jcdev1 on 2017/5/15.
 */

public class TimeLineRealTimeDrawing implements IDrawing {
    private Paint candlePaint; // 蜡烛图画笔
    private Paint linePaint;//虚线图画笔
    private AbstractRender render;
    private int realDataSize;

    private float rectHeight;
    private float rectWidth;

    private float candleSpace = 0.1f; // entry 与 entry 之间的间隙，默认 0.1f (10%)
    private final RectF kLineRect = new RectF(); // K 线图显示区域
    private Entry entrys;//++++++++++++++
    private final float[] newCandleRectBuffer = new float[4];//++++++++++++++

    private int digits = -1;

    public TimeLineRealTimeDrawing(int realDataSize, int digits) {
        this.realDataSize = realDataSize;
        this.digits = digits;
    }

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = render;
        this.rectHeight = contentRect.bottom;//contentRect.bottom:网格底部的Y坐标，即到屏幕顶部的距离
        this.rectWidth = contentRect.right;
        kLineRect.set(contentRect);

        final SizeColor sizeColor = render.getSizeColor();
        if (candlePaint == null) {
            candlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            candlePaint.setStyle(Paint.Style.STROKE);
            candlePaint.setStrokeWidth(sizeColor.getCandleBorderSize());
        }
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(2);
            linePaint.setColor(Color.rgb(16, 53, 177));
        }

    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        canvas.save();
//        canvas.clipRect(kLineRect);

        if (maxIndex > 0) {
//            float[] newCandleRectBuffer = new float[4];
            int entrySize = entrySet.getEntryList().size();
            entrys = entrySet.getEntryList().get(realDataSize-1);

            newCandleRectBuffer[0] = realDataSize - 1 + candleSpace;
            newCandleRectBuffer[2] = entrySize - candleSpace;
            newCandleRectBuffer[1] = entrys.getOpen();
            newCandleRectBuffer[3] = entrys.getClose();
            render.mapPoints(newCandleRectBuffer);

        }
        canvas.restore();

    }

    @Override
    public void onDrawOver(Canvas canvas) {
        if (entrys != null) {
            if (newCandleRectBuffer[3] > rectHeight) {
                drawCurrentStateMap(canvas, entrys.getClose(), newCandleRectBuffer[0], rectHeight);
            } else {
                drawCurrentStateMap(canvas, entrys.getClose(), newCandleRectBuffer[0], newCandleRectBuffer[3]);
            }
        }
    }

    /**
     * 标识最新股价图-横线
     *
     * @param canvas
     * @param text
     * @param yCoord
     */
    private void drawCurrentStateMap(Canvas canvas, double text, float xCoord, float yCoord) {
//        yCoord = yCoord + 20;//为了好看，做整体向下平易操作
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        linePaint.setPathEffect(effects);
        Path path1 = new Path();
        path1.moveTo(xCoord, yCoord);//替换0-->(rectWidth - 200)
        path1.lineTo(kLineRect.right, yCoord);
        canvas.drawPath(path1, linePaint);

//        RectF rf = new RectF(screenWidth - 100, yCoord - 20, screenWidth - 20, yCoord + 20);
        RectF rf = new RectF(kLineRect.right, yCoord - 15, kLineRect.right + 120, yCoord + 15);


        candlePaint.setStyle(Paint.Style.FILL);
        candlePaint.setColor(Color.rgb(16, 53, 177));
        canvas.drawRect(rf, candlePaint);

        candlePaint.setColor(Color.WHITE);
        candlePaint.setTextSize(18);
        canvas.drawText(BaseUtils.getDigitsData(text, digits), kLineRect.right + 20, yCoord + 5, candlePaint);

    }
}
