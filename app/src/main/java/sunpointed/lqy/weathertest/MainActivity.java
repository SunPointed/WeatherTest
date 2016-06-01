package sunpointed.lqy.weathertest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import sunpointed.lqy.weathertest.Beans.WeatherDataBean;
import sunpointed.lqy.weathertest.Utils.NetUtils;
import sunpointed.lqy.weathertest.customviews.WeatherView;

public class MainActivity extends AppCompatActivity {

//    key: dc4180935056e7aa20b0f4d66efce8a9 name: SunPointed

    WeatherView mWeatherView;
    int mWeatherStyle;

    ScrollView mScrollView;

    WeatherDataBean mBean;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        mWeatherView = (WeatherView) findViewById(R.id.wv);
        mWeatherStyle = 3;
        gson = new Gson();

        Call<ResponseBody> call = NetUtils.cityService.getInfo("chengdu,CN", NetUtils.APKID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    mBean = gson.fromJson(data, WeatherDataBean.class);
                    Log.i("lqy",data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWeatherStyle = (mWeatherStyle + 1) % 4;
            mWeatherView.setWeatherStyle(mWeatherStyle);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
