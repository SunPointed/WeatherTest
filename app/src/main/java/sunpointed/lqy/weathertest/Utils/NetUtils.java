package sunpointed.lqy.weathertest.Utils;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lqy on 16/5/31.
 */
public class NetUtils {

    public static final String APKID = "dc4180935056e7aa20b0f4d66efce8a9";

    public interface CityService {
        @GET("/data/2.5/weather")
        Call<ResponseBody> getInfo(@Query("q") String city,
                                   @Query("appid") String appid);
    }

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    public static CityService cityService = retrofit.create(CityService.class);
}
