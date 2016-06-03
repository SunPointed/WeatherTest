package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import sunpointed.lqy.weathertest.R;
import sunpointed.lqy.weathertest.Utils.BitmapUtils;

/**
 * Created by lqy on 16/6/1.
 */
public class AirQualityView extends View {

    public static final int[] SECTION_COLORS = {Color.GREEN, Color.YELLOW, Color.RED, 0xFFFF00FF};

    Context mContext;

    String mAirCondition;
    int mCondition;
    int mPM;

    int mWidth;
    int mHeight;
    int mLength;

    Paint mPaint;
    LinearGradient mShader;
    Path mPath;
    Bitmap mLeaf;

    public AirQualityView(Context context) {
        this(context, null);
    }

    public AirQualityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirQualityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mAirCondition = "中";
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mLeaf = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_air_level);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawLeft(canvas);

        drawRight(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mLength = mWidth / 7;
        if(mLength < mHeight) {
            mLeaf = BitmapUtils.scaleImageToFixSize(mLeaf, mLength, mLength);
        }else{
            mLeaf = BitmapUtils.scaleImageToFixSize(mLeaf, mHeight, mHeight);
        }
    }

    private void drawLeft(Canvas canvas) {
        if(mLength < mHeight) {
            canvas.drawBitmap(mLeaf, 0, Math.abs(mHeight - mLength) / 2, mPaint);
        } else {
            canvas.drawBitmap(mLeaf, Math.abs(mHeight - mLength) / 2, 0, mPaint);
        }
    }

    private void drawRight(Canvas canvas) {

        mPaint.setColor(0xFFFFFFFF);
        mPaint.setTextSize(35);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mContext.getString(R.string.air_condition), mLength + mLength / 2, mHeight / 3, mPaint);
        canvas.drawText(mContext.getString(R.string.pm), mLength * 3 + mLength / 2, mHeight / 3, mPaint);

        mPaint.setTextSize(50);
        canvas.drawText(mAirCondition, mLength * 2 + mLength / 2, mHeight / 3, mPaint);
        canvas.drawText(mPM + "", mLength * 4 + mLength / 2, mHeight / 3, mPaint);

        mPath.reset();
        mPath.moveTo(mLength + mCondition * mLength * 6 / 4 + 10, mHeight / 16 * 9);
        mPath.lineTo(mLength + mCondition * mLength * 6 / 4, mHeight / 4 * 3);
        mPath.lineTo(mLength + mCondition * mLength * 6 / 4 + 20, mHeight / 4 * 3);
        canvas.drawPath(mPath, mPaint);

        mPaint.setShader(mShader);
        mShader = new LinearGradient(mLength, mHeight / 2, mWidth, mHeight / 16 * 9, SECTION_COLORS, null, Shader.TileMode.MIRROR);
        canvas.drawRect(mLength, mHeight / 2, mWidth, mHeight / 16 * 9, mPaint);
        mPaint.setShader(null);
    }

    public void setData(int airCondition, int pm) {
        if (airCondition == 0) {
            mAirCondition = mContext.getString(R.string.great);
        } else if (airCondition == 1) {
            mAirCondition = mContext.getString(R.string.good);
        } else if (airCondition == 2) {
            mAirCondition = mContext.getString(R.string.general);
        } else if (airCondition == 3) {
            mAirCondition = mContext.getString(R.string.bad);
        } else {
            return;
        }

        mCondition = airCondition;
        mPM = pm;
        postInvalidate();
    }
}
