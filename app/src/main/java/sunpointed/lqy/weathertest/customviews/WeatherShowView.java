package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import sunpointed.lqy.weathertest.Beans.WeatherDataBean;
import sunpointed.lqy.weathertest.Beans.WeatherShowBean;

/**
 * Created by lqy on 16/6/1.
 */
public class WeatherShowView extends View {

    Paint mPaint;
    Path mTempPath;

    int mWidth;
    int mHeight;
    int mStepLength;
    int mTempStep;

    private int tempMin;
    private int tempMax;

    ArrayList<WeatherShowBean> mList;

    public WeatherShowView(Context context) {
        this(context, null);
    }

    public WeatherShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mList = new ArrayList<>();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTempPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setAlpha(0);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        mPaint.setStrokeWidth(3);
        mPaint.setAlpha(128);
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(mStepLength * 2 * (i + 1), 0, mStepLength * 2 * (i + 1), mHeight, mPaint);
        }

        mPaint.setAlpha(255);

        drawText(canvas);

        drawWeather(canvas);

        drawTemperature(canvas);
    }

    private void drawTemperature(Canvas canvas) {
        if (mList.size() != 0) {
            mTempStep = (mHeight / 3) / (tempMax - tempMin);
            mPaint.setTextSize(35);
            mPaint.setColor(0xFFFFFFFF);
            mTempPath.reset();
            for (int i = 0; i < 6; i++) {
                WeatherShowBean bean = mList.get(i);
                canvas.drawText(bean.high + "°", mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.high) * mTempStep - 20, mPaint);
                canvas.drawCircle(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.high) * mTempStep, 5, mPaint);
                canvas.drawText(bean.low + "°", mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.low) * mTempStep - 20, mPaint);
                canvas.drawCircle(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.low) * mTempStep, 5, mPaint);
                if (i == 0) {
                    mTempPath.moveTo(0, mHeight - Math.abs(bean.high) * mTempStep + 30);
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.high) * mTempStep);
                } else if (i == 5) {
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.high) * mTempStep);
                    mTempPath.lineTo(mWidth, mHeight - Math.abs(bean.high) * mTempStep + 30);
                } else {
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.high) * mTempStep);
                }
            }
            for (int i = 5; i > -1; i--) {
                WeatherShowBean bean = mList.get(i);
                if (i == 0) {
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.low) * mTempStep);
                    mTempPath.lineTo(0, mHeight - Math.abs(bean.low) * mTempStep + 30);
                } else if (i == 5) {
                    mTempPath.lineTo(mWidth, mHeight - Math.abs(bean.low) * mTempStep + 30);
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.low) * mTempStep);
                } else {
                    mTempPath.lineTo(mStepLength + mStepLength * 2 * i, mHeight - Math.abs(bean.low) * mTempStep);
                }
            }
            mPaint.setAlpha(128);
            canvas.drawPath(mTempPath, mPaint);
        }
    }

    private void drawWeather(Canvas canvas) {
        if (mList.size() != 0) {
            for (int i = 0; i < 6; i++) {
                WeatherShowBean bean = mList.get(i);
                drawWeather(canvas, bean.weather, i);
            }
        }
    }

    private void drawWeather(Canvas canvas, int type, int position) {
        if (type == 0) {
            int cx = mStepLength + mStepLength * 2 * position;
            int cy = mHeight / 3 - mStepLength / 2;
            int r = mStepLength / 2;
            float angle = 22.5f;
            for (int i = 2; i > -1; i--) {
                mTempPath.reset();
                if (i == 0) {
                    mPaint.setColor(0xFFFF5722);
                } else if (i == 1) {
                    mPaint.setColor(0xFFFF7722);
                } else if (i == 2) {
                    mPaint.setColor(0xFFFFB722);
                }
                for (int j = 0; j < 8; j++) {
                    if (j == 0) {
                        mTempPath.moveTo(cx + r * (i + 1) / 3 * (float) Math.cos((angle + 45 * j) * Math.PI / 180), cy + r * (i + 1) / 3 * (float) Math.sin((angle + 45 * j) * Math.PI / 180));
                    } else {
                        mTempPath.lineTo(cx + r * (i + 1) / 3 * (float) Math.cos((angle + 45 * j) * Math.PI / 180), cy + r * (i + 1) / 3 * (float) Math.sin((angle + 45 * j) * Math.PI / 180));
                    }
                }
                angle += 22.5f;
                canvas.drawPath(mTempPath, mPaint);
            }
        } else if (type == 1) {
            int cx = mStepLength + mStepLength * 2 * position;
            int cy = mHeight / 3 - mStepLength / 2;
            cy = (mHeight / 3 - cy) / 2 + cy;
            int r = mStepLength / 2;
            r = r / 2;
            float angle = 10f;
            mTempPath.reset();
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    mTempPath.moveTo(cx + r * (float) Math.cos((angle + 45 * i) * Math.PI / 180), cy + r * (float) Math.sin((angle + 45 * i) * Math.PI / 180));
                } else {
                    mTempPath.lineTo(cx + r * (float) Math.cos((angle + 45 * i) * Math.PI / 180), cy + r * (float) Math.sin((angle + 45 * i) * Math.PI / 180));
                }
            }
            mPaint.setColor(0xFFFFB722);
            canvas.drawPath(mTempPath, mPaint);

            drawCloud(canvas, position);
        } else if (type == 2) {
            int cx = mStepLength + mStepLength * 2 * position;
            int cy = mHeight / 3 - mStepLength / 2;
            cy = (mHeight / 3 - cy) / 2 + cy;
            int r = mStepLength / 2;
            r = r / 2;
            mPaint.setColor(0xFF607D8B);
            canvas.drawCircle(cx, cy, r, mPaint);

            drawCloud(canvas, position);
        } else if (type == 3) {
            int cx = mStepLength + mStepLength * 2 * position;
            cx = cx - mStepLength / 2;
            int cy = mHeight / 3 - mStepLength / 2;
            cy = (mHeight / 3 - cy) / 2 + cy;
            int r = mStepLength / 2;
            r = r / 4;
            for (int i = 0; i < 4; i++) {
                if (i == 1) {
                    mPaint.setColor(0xFFFBC02D);
                } else {
                    mPaint.setColor(0xFFFFFFFF);
                }
                canvas.drawLine(cx + i * mStepLength / 6, mHeight / 3, cx + (i + 1) * mStepLength / 6, cy, mPaint);
            }

            drawCloud(canvas, position);
        }
    }

    private void drawCloud(Canvas canvas, int position) {
        int cx = mStepLength + mStepLength * 2 * position;
        int cy = mHeight / 3 - mStepLength / 2;
        int r = mStepLength / 2;
        r = r / 3 * 2;
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setAlpha(128);
        canvas.drawCircle(cx + 20, cy - 5, r / 3 * 2, mPaint);
        mPaint.setAlpha(255);
        canvas.drawCircle(cx - 30, cy + 5, r / 3 * 2, mPaint);
        canvas.drawCircle(cx - 10, cy - 10, r, mPaint);
    }

    private void drawText(Canvas canvas) {
        if (mList.size() != 0) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            for (int i = 0; i < 6; i++) {
                mPaint.setTextSize(35);
                canvas.drawText(mList.get(i).date, mStepLength + mStepLength * 2 * i, mHeight / 12, mPaint);
                mPaint.setTextSize(40);
                canvas.drawText(mList.get(i).day, mStepLength + mStepLength * 2 * i, mHeight / 6, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mStepLength = mWidth / 12;
    }

    public void setShowList(ArrayList<WeatherShowBean> list) {
        mList = list;

        int size = mList.size();
        for (int i = 0; i < size; i++) {
            WeatherShowBean bean = mList.get(i);
            if (i == 0) {
                tempMax = bean.high;
                tempMin = bean.low;
            } else {
                tempMax = tempMax > bean.high ? tempMax : bean.high;
                tempMin = tempMin < bean.low ? tempMin : bean.low;
            }
        }

        postInvalidate();
    }
}
