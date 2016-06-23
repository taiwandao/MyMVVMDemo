package base.brian.com.mymvvmdemo.network.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dingzhihu on 15/5/7.
 */
public class RetrofitProvider {

    private static Retrofit retrofit;
    private static volatile OkHttpClient mOkHttpClient;

    private RetrofitProvider() {
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new ApiTypeAdapterFactory("data"))
                    .create();
            initOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(mOkHttpClient)
                    .build();
        }
        return retrofit;

    }

    private final static String tag = RetrofitProvider.class.getSimpleName();
    static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
//            Log.i(tag, String.format("response %s",response.body().string()));
            return response;
        }
    };

    // 配置OkHttpClient
    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitProvider.class) {
                if (mOkHttpClient == null) {
                    HttpLoggingInterceptor log = new HttpLoggingInterceptor();
                    log.setLevel(HttpLoggingInterceptor.Level.BODY);
                    mOkHttpClient = new OkHttpClient.Builder().addInterceptor(log)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();
                }
            }
        }
    }

}
