package sunpointed.lqy.weathertest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import sunpointed.lqy.weathertest.Beans.WeatherDataBean;
import sunpointed.lqy.weathertest.Beans.WeatherShowBean;
import sunpointed.lqy.weathertest.Utils.BitmapUtils;
import sunpointed.lqy.weathertest.Utils.NetUtils;
import sunpointed.lqy.weathertest.customviews.AirQualityView;
import sunpointed.lqy.weathertest.customviews.SuggestView;
import sunpointed.lqy.weathertest.customviews.TitleBar;
import sunpointed.lqy.weathertest.customviews.WeatherShowView;
import sunpointed.lqy.weathertest.customviews.WeatherView;

public class MainActivity extends AppCompatActivity {

//    key: dc4180935056e7aa20b0f4d66efce8a9 name: SunPointed

    TitleBar mTitleBar;
    RelativeLayout.LayoutParams mTitleParams;
    int mPreToolLeft;
    int mPreToolRight;

    WeatherView mWeatherView;
    int mWeatherStyle;

    ScrollView mScrollView;
    RelativeLayout.LayoutParams mScrollParams;
    float mScrollX;
    float mScrollY;
    int mPreScrollLeft;
    int mPreScrollRight;

    WeatherShowView mWeatherShowView;
    AirQualityView mAirQualityView;
    SuggestView mSuggestView;

    WeatherDataBean mBean;
    Gson gson;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
//        getData();

        setContentView(R.layout.activity_main);

        mTitleBar = (TitleBar) findViewById(R.id.tb);
        mTitleBar.setCity("成都");
        mTitleBar.setDate("6/9");
        mTitleBar.setDay("星期六");
        mTitleBar.setMore(new TitleBar.TitleBarSetting() {
            @Override
            public void more() {
                showPopupWindow(mTitleBar);
            }
        });
        mTitleParams = (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollParams = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();
        mPreScrollLeft = mScrollParams.leftMargin;
        mPreScrollRight = mScrollParams.rightMargin;
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float x = event.getX();
                float y = event.getY();

                if (action == MotionEvent.ACTION_DOWN) {
                    mScrollX = event.getX();
                    mScrollY = event.getY();

                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (Math.abs(mScrollX - x) > Math.abs(mScrollY - y)) {
                        if (mScrollX - x > 0) {
                            mScrollParams.rightMargin += mScrollX - x;
                            mScrollParams.leftMargin -= mScrollX - x;
                            mTitleParams.rightMargin += mScrollX - x;
                            mTitleParams.leftMargin -= mScrollX - x;
                        } else {
                            mScrollParams.rightMargin += mScrollX - x;
                            mScrollParams.leftMargin -= mScrollX - x;
                            mTitleParams.rightMargin += mScrollX - x;
                            mTitleParams.leftMargin -= mScrollX - x;
                        }
                        mScrollView.setLayoutParams(mScrollParams);
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    if (Math.abs(mScrollParams.rightMargin - mPreScrollRight) > mScrollView.getWidth() / 2) {
                        // TODO: 16/6/4
                    } else {
                        mScrollParams.rightMargin = mPreScrollRight;
                        mScrollParams.leftMargin = mPreScrollLeft;
                        mTitleParams.rightMargin = mPreToolRight;
                        mTitleParams.leftMargin = mPreScrollLeft;
                    }
                    mScrollView.setLayoutParams(mScrollParams);
                }
                return false;
            }
        });

        ArrayList<WeatherShowBean> arrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            WeatherShowBean bean = new WeatherShowBean();
            bean.date = "6月" + i;
            bean.day = "星期" + i;
            bean.weather = i % 4;
            bean.high = (int) (20 + Math.random() * 10);
            bean.low = (int) (5 + Math.random() * 10);
            arrayList.add(bean);
        }
        mWeatherShowView = (WeatherShowView) findViewById(R.id.wsv);
        mWeatherShowView.setShowList(arrayList);

        mAirQualityView = (AirQualityView) findViewById(R.id.aqv);
        mAirQualityView.setData(1, 77);

        mSuggestView = (SuggestView) findViewById(R.id.sv);
        mSuggestView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSuggestView.setData(2, 4, 27, 66, 5, 2, 900);
            }
        }, 2000);

        mWeatherView = (WeatherView) findViewById(R.id.wv);
        mWeatherStyle = 3;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeatherView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWeatherView.onPause();
    }
    
    private void setTitle(String city, String date, String day){
        mTitleBar.setCity(city);
        mTitleBar.setDate(date);
        mTitleBar.setDay(day);
    }

    private void getData(){
        Call<ResponseBody> call = NetUtils.cityService.getInfo("chengdu,CN", NetUtils.APKID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    mBean = gson.fromJson(data, WeatherDataBean.class);
                    Log.i("lqy", data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void showPopupWindow(View view) {

        if(!(view instanceof TitleBar)){
            return;
        }

        TitleBar titleBar = (TitleBar) view;

        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_window, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                titleBar.getHeight() / 3 * 2, titleBar.getHeight() * 2, true);

//        popupWindow.setAnimationStyle(R.style.pop);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.icon_show);

        ImageView share = (ImageView) contentView.findViewById(R.id.menu_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ImageView city = (ImageView) contentView.findViewById(R.id.menu_city);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ImageView setting = (ImageView) contentView.findViewById(R.id.menu_settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.kongbai));

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, view.getWidth() - titleBar.getHeight(), view.getHeight() + titleBar.getHeight() / 2);
        share.startAnimation(animation);
        city.startAnimation(animation);
        setting.startAnimation(animation);
    }
}
