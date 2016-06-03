package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import sunpointed.lqy.weathertest.R;
import sunpointed.lqy.weathertest.Utils.BitmapUtils;

/**
 * Created by lqy on 16/6/1.
 */
public class SuggestView extends View {

    Context mContext;

    int mWindType;
    String mWindString;
    int mWindLevel;

    int mBT;
    int mHumidity;
    int mVisibility;
    int mUV;
    String mUVString;
    int mPa;

    int mWidth;
    int mHeight;
    int mHeightStep;
    int mWidthStep;

    Paint mPaint;

    Bitmap mBmpWind;
    Bitmap mBmpBT;
    Bitmap mBmpHumidity;
    Bitmap mBmpVisibility;
    Bitmap mBmpUV;
    Bitmap mBmpPa;

    public SuggestView(Context context) {
        this(context, null);
    }

    public SuggestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuggestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mWindString = "";
        mUVString = "";

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBmpWind = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_east_wind_icon);
        mBmpBT = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_air_level);
        mBmpHumidity = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_humidity_level);
        mBmpVisibility = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_air_level);
        mBmpUV = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_uv_levle);
        mBmpPa = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_air_level);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mHeightStep = mHeight / 3;
        mWidthStep = mWidth / 2;

        if (mWidthStep / 3 < mHeightStep) {
            mBmpWind = BitmapUtils.scaleImageToFixSize(mBmpWind, mWidthStep / 3, mWidthStep / 3);
            mBmpBT = BitmapUtils.scaleImageToFixSize(mBmpBT, mWidthStep / 3, mWidthStep / 3);
            mBmpHumidity = BitmapUtils.scaleImageToFixSize(mBmpHumidity, mWidthStep / 3, mWidthStep / 3);
            mBmpVisibility = BitmapUtils.scaleImageToFixSize(mBmpVisibility, mWidthStep / 3, mWidthStep / 3);
            mBmpUV = BitmapUtils.scaleImageToFixSize(mBmpUV, mWidthStep / 3, mWidthStep / 3);
            mBmpPa = BitmapUtils.scaleImageToFixSize(mBmpPa, mWidthStep / 3, mWidthStep / 3);
        } else {
            mBmpWind = BitmapUtils.scaleImageToFixSize(mBmpWind, mHeightStep, mHeightStep);
            mBmpBT = BitmapUtils.scaleImageToFixSize(mBmpBT, mHeightStep, mHeightStep);
            mBmpHumidity = BitmapUtils.scaleImageToFixSize(mBmpHumidity, mHeightStep, mHeightStep);
            mBmpVisibility = BitmapUtils.scaleImageToFixSize(mBmpVisibility, mHeightStep, mHeightStep);
            mBmpUV = BitmapUtils.scaleImageToFixSize(mBmpUV, mHeightStep, mHeightStep);
            mBmpPa = BitmapUtils.scaleImageToFixSize(mBmpPa, mHeightStep, mHeightStep);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawText(canvas);

        drawBmp(canvas);

    }

    private void drawText(Canvas canvas) {
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setTextSize(35);
        canvas.drawText(mWindString, mWidthStep / 3, mHeightStep / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.humidity), mWidthStep / 3, mHeightStep + mHeightStep / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.uv), mWidthStep / 3, mHeightStep * 2 + mHeightStep / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.body_temperature), mWidthStep + mWidthStep / 3, mHeightStep / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.visibility), mWidthStep + mWidthStep / 3, mHeightStep + mHeightStep / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.pa), mWidthStep + mWidthStep / 3, mHeightStep * 2 + mHeightStep / 3, mPaint);
        mPaint.setTextSize(50);
        canvas.drawText(mWindLevel + "级", mWidthStep / 3, mHeightStep / 3 * 2, mPaint);
        canvas.drawText(mHumidity + "%", mWidthStep / 3, mHeightStep + mHeightStep / 3 * 2, mPaint);
        canvas.drawText(mUVString, mWidthStep / 3, mHeightStep * 2 + mHeightStep / 3 * 2, mPaint);
        canvas.drawText(mBT + "°", mWidthStep + mWidthStep / 3, mHeightStep / 3 * 2, mPaint);
        canvas.drawText(mVisibility + "千米", mWidthStep + mWidthStep / 3, mHeightStep + mHeightStep / 3 * 2, mPaint);
        canvas.drawText(mPa + "百帕", mWidthStep + mWidthStep / 3, mHeightStep * 2 + mHeightStep / 3 * 2, mPaint);
    }

    private void drawBmp(Canvas canvas) {
        if (mWidthStep / 3 < mHeightStep) {
            canvas.drawBitmap(mBmpWind, 0, (mHeightStep - mWidthStep / 3) / 2, mPaint);
            canvas.drawBitmap(mBmpBT, mWidthStep, (mHeightStep - mWidthStep / 3) / 2, mPaint);
            canvas.drawBitmap(mBmpHumidity, 0, mHeightStep + (mHeightStep - mWidthStep / 3) / 2, mPaint);
            canvas.drawBitmap(mBmpVisibility, mWidthStep, (mHeightStep - mWidthStep / 3) / 2 + mHeightStep, mPaint);
            canvas.drawBitmap(mBmpUV, 0, mHeightStep * 2 + (mHeightStep - mWidthStep / 3) / 2, mPaint);
            canvas.drawBitmap(mBmpPa, mWidthStep, (mHeightStep - mWidthStep / 3) / 2 + mHeightStep * 2, mPaint);
        } else {
            canvas.drawBitmap(mBmpWind, (mWidthStep / 3 - mHeightStep) / 2, 0, mPaint);
            canvas.drawBitmap(mBmpBT, mWidthStep + (mWidthStep / 3 - mHeightStep) / 2, 0, mPaint);
            canvas.drawBitmap(mBmpHumidity, (mWidthStep / 3 - mHeightStep) / 2, mHeightStep, mPaint);
            canvas.drawBitmap(mBmpVisibility, mWidthStep + (mWidthStep / 3 - mHeightStep) / 2, mHeightStep, mPaint);
            canvas.drawBitmap(mBmpUV, (mWidthStep / 3 - mHeightStep) / 2, mHeightStep * 2, mPaint);
            canvas.drawBitmap(mBmpPa, mWidthStep + (mWidthStep / 3 - mHeightStep) / 2, mHeightStep * 2, mPaint);
        }
    }

    public void setData(int windType, int windLevel, int bt, int humidity, int visibility, int uv, int pa) {
        if (windType == 0) {
            mWindString = mContext.getString(R.string.wind_e);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.east_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.east_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 1) {
            mWindString = mContext.getString(R.string.wind_s);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 2) {
            mWindString = mContext.getString(R.string.wind_w);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.west_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.west_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 3) {
            mWindString = mContext.getString(R.string.wind_n);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 4) {
            mWindString = mContext.getString(R.string.wind_ws);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_west_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_west_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 5) {
            mWindString = mContext.getString(R.string.wind_wn);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_west_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_west_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 6) {
            mWindString = mContext.getString(R.string.wind_es);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_east_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.south_east_wind_icon),
                        mHeightStep, mHeightStep);
            }
        } else if (windType == 7) {
            mWindString = mContext.getString(R.string.wind_en);
            if (mWidthStep / 3 < mHeightStep) {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_east_wind_icon),
                        mWidthStep / 3, mWidthStep / 3);
            } else {
                mBmpWind = BitmapUtils.scaleImageToFixSize(
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.north_east_wind_icon),
                        mHeightStep, mHeightStep);
            }
        }

        if (uv == 0) {
            mUVString = mContext.getString(R.string.uv_strong);
        } else if (uv == 1) {
            mUVString = mContext.getString(R.string.uv_middle);
        } else if (uv == 2) {
            mUVString = mContext.getString(R.string.uv_weak);
        }

        mWindType = windType;
        mWindLevel = windLevel;
        mBT = bt;
        mHumidity = humidity;
        mVisibility = visibility;
        mUV = uv;
        mPa = pa;

        postInvalidate();
    }
}
