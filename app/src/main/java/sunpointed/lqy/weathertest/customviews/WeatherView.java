package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import sunpointed.lqy.weathertest.R;

/**
 * Created by lqy on 16/5/30.
 */
public class WeatherView extends View implements SensorEventListener {

    public static final int SUNSHINE = 0;
    public static final int CLOUD_SUN = 1;
    public static final int CLOUDY = 2;
    public static final int RAIN = 3;

    private static final int LINE_COUNT = 50;

    int mWeatherStyle;

    Paint mPaint;

    int mWidth;
    int mHeight;

    float mRainCenterX;
    float mRainCenterY;
    int mRainCount;
    int mRainPositionX[];
    int mRainPositionY[];
    int mRainLineNow[];
    int mRainLineCount[];

    float mOthersX;
    float mOthersY;

    SensorManager mManger;
    Sensor mSensor;

    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherView);

        mWeatherStyle = typedArray.getInt(R.styleable.WeatherView_weather, 0);

        mRainCount = typedArray.getInt(R.styleable.WeatherView_rain_count, 100);
        mRainPositionX = new int[mRainCount];
        mRainPositionY = new int[mRainCount];
        mRainLineNow = new int[mRainCount];
        mRainLineCount = new int[mRainCount];

        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mManger = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mWeatherStyle == SUNSHINE) {
            drawSunshine(canvas);
        } else if (mWeatherStyle == CLOUD_SUN) {
            drawCloudSun(canvas);
        } else if (mWeatherStyle == CLOUDY) {
            drawCloudy(canvas);
        } else if (mWeatherStyle == RAIN) {
            drawRain(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mOthersX = 0;
        mOthersY = mHeight / 3;
        mRainCenterX = mWidth / 2;
        mRainCenterY = mHeight / 2;
        for (int i = 0; i < mRainCount; i++) {
            mRainPositionX[i] = (int) (Math.random() * mWidth);
            mRainPositionY[i] = (int) (Math.random() * mHeight);
            mRainLineNow[i] = (int) (Math.random() * mRainCount);
            mRainLineCount[i] = LINE_COUNT + (int) (Math.random() * mRainCount);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        mRainCenterX -= (int)x;
        mRainCenterY += (int)y;

        postInvalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void drawSunshine(Canvas canvas) {
        mPaint.setColor(0xFFFF9800);
        mPaint.setStrokeWidth(3);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
    }

    private void drawCloudSun(Canvas canvas) {

    }

    private void drawCloudy(Canvas canvas) {

    }

    private void drawRain(Canvas canvas) {
        mPaint.setColor(0xFF303F9F);
        mPaint.setStrokeWidth(3);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        float pX = 0, pY = 0, aX = 0, aY = 0;
        for (int i = 0; i < mRainCount; i++) {
            if (i % 2 == 0) {
                mPaint.setColor(0xFF448AFF);
            } else {
                mPaint.setColor(0xFFFF9800);
            }

            float dx = (mRainPositionX[i] - mRainCenterX) / mRainLineCount[i];
            float dy = (mRainPositionY[i] - mRainCenterY) / mRainLineCount[i];

            pX = mRainPositionX[i] - dx * (mRainLineNow[i]);
            pY = mRainPositionY[i] - dy * (mRainLineNow[i]);

            aX = pX - dx;
            aY = pY - dy;

            canvas.drawLine(pX, pY, aX, aY, mPaint);
        }
        for (int i = 0; i < mRainCount; i++) {
            if (mRainLineNow[i] > mRainLineCount[i] - LINE_COUNT / 2) {
                mRainLineNow[i] = 1;
            } else {
                mRainLineNow[i]++;
            }
        }
    }

    public void onResume() {
        if (mManger != null) {
            mManger.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onPause() {
        if (mManger != null) {
            mManger.unregisterListener(this);
        }
    }

    public void setWeatherStyle(int weatherStyle) {
        mWeatherStyle = weatherStyle;
        postInvalidate();
    }
}
