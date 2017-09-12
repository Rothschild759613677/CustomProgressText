package com.moonsky.customprogresstext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 自定义进度条---带进度文本
 * Created by Nick on 2017/9/11.
 */

public class CustomProgress extends View {

    private Paint paint;

    private static final int DEFAULT_FONT_SIZE = 18;
    private static final int CIRCLE_ARC_WIDTH = 10;
    private int maxProgress;
    private int startAngle;
    private int fontSize;
    private int fontColor;
    private int circleArcColor;
    private int circleArcWidth;
    private int circleColor;

    private int currentProgress;

    private ProgressListener progressListener;

    public CustomProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgress);
        maxProgress = typedArray.getInt(R.styleable.CustomProgress_maxProgress, 100);
        startAngle = typedArray.getInt(R.styleable.CustomProgress_startAngle, -45);
        fontSize = typedArray.getDimensionPixelSize(R.styleable.CustomProgress_fontSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE, getResources().getDisplayMetrics()));

        fontColor = typedArray.getColor(R.styleable.CustomProgress_fontColor, getResources().getColor(R.color.colorAccent));
        circleArcColor = typedArray.getColor(R.styleable.CustomProgress_circleArcColor, getResources().getColor(R.color.colorAccent));
        circleArcWidth = typedArray.getDimensionPixelSize(R.styleable.CustomProgress_circleArcWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CIRCLE_ARC_WIDTH, getResources().getDisplayMetrics()));
        circleColor = typedArray.getColor(R.styleable.CustomProgress_circleColor, getResources().getColor(R.color.colorPrimary));

        typedArray.recycle();
        init();
    }

    private RectF rectF;

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int center = width / 2;
        float radius = center - circleArcWidth / 2;

        //1-画背景圆环
        paint.setStrokeWidth(circleArcWidth);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center, center, radius, paint);

        //2-画进度文本
        paint.setTextSize(fontSize);
        paint.setColor(fontColor);
        paint.setStrokeWidth(0);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        String content = currentProgress + "%";
        //用于获取文本的宽度以及计算基线值
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseLineX = center - paint.measureText(content) / 2;
        float baseLineY = center + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(content, baseLineX, baseLineY, paint);

        //3-画圆弧
        paint.setColor(circleArcColor);
        paint.setStrokeWidth(circleArcWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);//圆帽
        paint.setStyle(Paint.Style.STROKE);
//        rectF.set(circleArcWidth / 2, circleArcWidth / 2, center + radius, center + radius);
        rectF.set(circleArcWidth / 2, circleArcWidth / 2, width - circleArcWidth / 2, width - circleArcWidth / 2);
        float sweepAngle = currentProgress * 360 / maxProgress;
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);

    }

    public void setCurrentProgress(int progressValue) {
        if (progressValue < 0) {
            throw new IllegalArgumentException("The progress value is not less then zero");
        }

        if (progressValue >= maxProgress) {
            progressValue = maxProgress;
            progressListener.complete();
        }
        this.currentProgress = progressValue;
        postInvalidate();
    }


    public interface ProgressListener {
        void complete();
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

}
