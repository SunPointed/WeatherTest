package sunpointed.lqy.weathertest.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private static final int ANGLE = 360 / 9;
    private static final int COLOR_ARRAY[] = {
            0xFFFF5722, 0xFFFF6722, 0xFFFF7722, 0xFFFF8722,
            0xFFFF9722, 0xFFFFA722, 0xFFFFB722, 0xFFFFC722,
            0xFFFFD722};

    int mWeatherStyle;

    Paint mPaint;

    int mCloudSunX;
    int mCloudSunY;
    int mCloudSunRadius;

    int mWidth;
    int mHeight;

    int mCloudX[];
    int mCloudY[];
    int mCloudRadius[];
    int mCloudXRange;
    int mCloudYRange;

    Path mSunshinePath;
    int mSunshineX[];
    int mSunshineY[];
    float mSunshineStartAngle;
    int mSunshineLenthX;
    int mSunshineLenthY;
    int mLightAngle;
    int mPAngle;

    float mRainCenterX;
    float mRainCenterY;
    int mRainCount;
    int mRainPositionX[];
    int mRainPositionY[];
    int mRainLineNow[];
    int mRainLineCount[];

    int mDisAlpha;

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

        mSunshinePath = new Path();

        mSunshineX = new int[9];
        mSunshineY = new int[9];
        mSunshineStartAngle = (float) (Math.random() * 360);
        mSunshineLenthX = 0;
        mSunshineLenthY = 0;
        mLightAngle = 60;
        mPAngle = mLightAngle;

        mCloudX = new int[8];
        mCloudY = new int[8];
        mCloudRadius = new int[8];
        mCloudXRange = 0;
        mCloudYRange = 0;

        mDisAlpha = 0;

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
        mRainCenterX = mWidth / 2;
        mRainCenterY = mHeight / 2;

        for (int i = 0; i < mRainCount; i++) {
            mRainPositionX[i] = (int) (Math.random() * mWidth);
            mRainPositionY[i] = (int) (Math.random() * mHeight);
            mRainLineNow[i] = (int) (Math.random() * mRainCount);
            mRainLineCount[i] = LINE_COUNT + (int) (Math.random() * mRainCount);
        }

        for (int i = 0; i < 8; i++) {
            if (i == 7) {
                mCloudX[i] = mWidth;
                mCloudY[i] = mHeight / 6;
                mCloudRadius[i] = mHeight / 7;
            } else {
                mCloudX[i] = (int) (Math.random() * mWidth);
                mCloudY[i] = i % 2 == 0 ? -100 : -150;
                mCloudRadius[i] = (int) (mWidth / 4 + Math.random() * mWidth / 3);
            }
        }

        mCloudSunX = mWidth / 5;
        mCloudSunY = mHeight / 7;
        mCloudSunRadius = mHeight / 10;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (mWeatherStyle == RAIN) {
                mRainCenterX -= (int) x * 5;
                mRainCenterY += (int) y * 5;
            } else if (mWeatherStyle == SUNSHINE) {
                mSunshineLenthX -= (int) x;
                if (mSunshineLenthX < -mWidth) {
                    mSunshineLenthX = -mWidth;
                } else if (mSunshineLenthX > 0) {
                    mSunshineLenthX = 0;
                }
                mSunshineLenthY += (int) y;
                if (mSunshineLenthY < 0) {
                    mSunshineLenthY = 0;
                } else if (mSunshineLenthY > mHeight / 2) {
                    mSunshineLenthY = mHeight / 2;
                }
                mSunshineStartAngle += 0.2;

                mLightAngle = (int) (mPAngle - mSunshineLenthX/(float)mWidth * 60);
            } else if (mWeatherStyle == CLOUDY || mWeatherStyle == CLOUD_SUN) {
                mCloudXRange -= (int) x;
                if (mCloudXRange < -100) {
                    mCloudXRange = -100;
                } else if (mCloudXRange > 100) {
                    mCloudXRange = 100;
                }

                mCloudYRange += (int) y;
                if (mCloudYRange < -200) {
                    mCloudYRange = -200;
                } else if (mCloudYRange > mHeight / 4) {
                    mCloudYRange = mHeight / 4;
                }

                if (mCloudXRange > -100 && mCloudXRange < 100) {
                    for (int i = 0; i < 8; i++) {
                        mCloudX[i] -= (int) x;
                    }
                    mCloudSunX -= (int) x;
                }

                if (mCloudYRange > -200 && mCloudYRange < mHeight / 4) {
                    for (int i = 0; i < 8; i++) {
                        mCloudY[i] += (int) y;
                    }
                    mCloudSunY += (int) y;
                }
            }

            postInvalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void drawSunshine(Canvas canvas) {
        mPaint.setColor(0xFF03A9F4);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        if (mDisAlpha != 255) {
            int pRadius = mWidth / 7 + mWidth / 10 * 8;
            int pX = mWidth + mSunshineLenthX;
            int pY = 0 + mSunshineLenthY;
            float sAngle = mSunshineStartAngle;
            int alpha = 60;

            //9个9边形组成太阳效果
            for (int i = 8; i > -1; i--) {
                mPaint.setColor(COLOR_ARRAY[i]);
                mPaint.setAlpha(alpha);
                for (int j = 0; j < 9; j++) {
                    mSunshineX[j] = (int) (pX + pRadius * Math.cos((sAngle + ANGLE * j) * Math.PI / 180));
                    mSunshineY[j] = (int) (pY + pRadius * Math.sin((sAngle + ANGLE * j) * Math.PI / 180));
                    if (j == 0) {
                        mSunshinePath.moveTo(mSunshineX[j], mSunshineY[j]);
                    } else {
                        mSunshinePath.lineTo(mSunshineX[j], mSunshineY[j]);
                    }
                }
                canvas.drawPath(mSunshinePath, mPaint);
                alpha += 195 / 10;
                pRadius -= mWidth / 10;
                sAngle += ANGLE / 2;
                mSunshinePath.reset();
            }

            pRadius = mWidth / 7 + mWidth / 10 * 4;
            mPaint.setAlpha(255);
            mPaint.setColor(0xFFFFFFFF);
            pX = (int) (pX - pRadius * Math.cos(mLightAngle * Math.PI / 180));
            pY = (int) (pY + pRadius * Math.sin(mLightAngle * Math.PI / 180));
            for (int i = 0; i < 3; i++) {
                pRadius = mWidth / ((3 - i) * 10);
                for (int j = 0; j < 6; j++) {
                    mSunshineX[j] = (int) (pX + pRadius * Math.cos((sAngle + mPAngle * j) * Math.PI / 180));
                    mSunshineY[j] = (int) (pY + pRadius * Math.sin((sAngle + mPAngle * j) * Math.PI / 180));
                    if (j == 0) {
                        mSunshinePath.moveTo(mSunshineX[j], mSunshineY[j]);
                    } else {
                        mSunshinePath.lineTo(mSunshineX[j], mSunshineY[j]);
                    }
                }
                pX -= mWidth / 5 * (i + 1) * Math.cos(mLightAngle * Math.PI / 180);
                pY += mWidth / 5 * (i + 1) * Math.sin(mLightAngle * Math.PI / 180);
                canvas.drawPath(mSunshinePath, mPaint);
                sAngle += ANGLE / 2;
                mSunshinePath.reset();
            }

            mPaint.setColor(0xFF03A9F4);
            mPaint.setAlpha(mDisAlpha);
            canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        }
    }

    private void drawCloudSun(Canvas canvas) {
        mPaint.setColor(0xFF00BCD4);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        if (mDisAlpha != 255) {
            int alpha = 120;

            // 8个圆组成云层
            for (int i = 7; i > -1; i--) {
                mPaint.setColor(0xFFFFFFFF);
                mPaint.setAlpha(alpha);
                canvas.drawCircle(mCloudX[i], mCloudY[i], mCloudRadius[i], mPaint);
                alpha = i % 2 == 0 ? 120 : 150;
                if (i == 2) {
                    mPaint.setAlpha(150);
                    mPaint.setColor(0xFFFFEB3B);
                    canvas.drawCircle(mCloudSunX, mCloudSunY, mCloudSunRadius, mPaint);
                }
            }

            mPaint.setColor(0xFF00BCD4);
            mPaint.setAlpha(mDisAlpha);
            canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        }
    }

    private void drawCloudy(Canvas canvas) {
        mPaint.setColor(0xFF8DB6CD);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        if (mDisAlpha != 255) {
            int alpha = 100;

            // 8个圆组成云层
            for (int i = 7; i > -1; i--) {
                mPaint.setColor(i % 2 == 0 ? 0xFF778899 : 0xFFFFFFFF);
                mPaint.setAlpha(alpha);
                canvas.drawCircle(mCloudX[i], mCloudY[i], mCloudRadius[i], mPaint);
                alpha = 1 % 2 == 0 ? 70 : 100;
            }

            mPaint.setColor(0xFF8DB6CD);
            mPaint.setAlpha(mDisAlpha);
            canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        }
    }

    private void drawRain(Canvas canvas) {
        mPaint.setColor(0xFF303F9F);
        mPaint.setStrokeWidth(3);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        if (mDisAlpha != 255) {
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

            mPaint.setColor(0xFF303F9F);
            mPaint.setAlpha(mDisAlpha);
            canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
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

    public void disappear(int alpha) {
        if (alpha < 0) {
            mDisAlpha = 0;
        } else if (alpha > 255) {
            mDisAlpha = 255;
        } else {
            mDisAlpha = alpha;
        }
        postInvalidate();
    }
}
