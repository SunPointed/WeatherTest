package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sunpointed.lqy.weathertest.R;
import sunpointed.lqy.weathertest.Utils.BitmapUtils;

/**
 * Created by lqy on 16/6/7.
 */
public class TitleBar extends View {

    Context mContext;
    float mPositionX;
    TitleBarSetting mSetting;

    int mWidth;
    int mHeight;

    String mCity;
    String mDate;
    String mDay;

    Paint mPaint;

    Bitmap mLocation;
    Bitmap mMore;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDay = "";
        mDate = "";
        mCity = "";
        mLocation = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_gps);
        mMore = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.more_setting);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mLocation = BitmapUtils.scaleImageToFixSize(mLocation, mHeight / 2, mHeight / 3 * 2);
        mMore = BitmapUtils.scaleImageToFixSize(mMore, mHeight / 3 * 2, mHeight / 3 * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(0xFFFFFFFF);

        canvas.drawBitmap(mLocation, mHeight / 6, mHeight / 6, mPaint);
        mPaint.setTextSize(50);
        canvas.drawText(mCity, mHeight / 3 * 2, mHeight / 3 * 2, mPaint);
        mPaint.setTextSize(35);
        canvas.drawText(mDate + "  " + mDay, mHeight + mHeight / 3 * 2, mHeight / 3 * 2, mPaint);
        canvas.drawBitmap(mMore, mWidth - mHeight, mHeight / 6, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mPositionX = event.getX();
        } else if (action == MotionEvent.ACTION_UP) {
            if (mPositionX > mWidth - mHeight) {
                if (mSetting != null) {
                    mSetting.more();
                }
            }
        }
        return true;
    }

    public void setCity(String city) {
        if (city != null) {
            mCity = city;
            postInvalidate();
        }
    }

    public void setDate(String date) {
        if (date != null) {
            mDate = date;
            postInvalidate();
        }
    }

    public void setDay(String day) {
        if (day != null) {
            mDay = day;
            postInvalidate();
        }
    }

    public void setMore(TitleBarSetting setting) {
        mSetting = setting;
    }

    public interface TitleBarSetting {
        void more();
    }
}
