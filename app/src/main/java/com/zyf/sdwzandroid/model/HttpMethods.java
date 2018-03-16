package com.zyf.sdwzandroid.model;

/**
 * Created by 46442 on 2018/3/12.
 * 封装的http请求工具类
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class HttpMethods {


    private final Charset UTF8 = Charset.forName("UTF-8");
    private static final int DEFAULT_TIMEOUT = 30 * 1000;




    private RestApi restApi;


    public RestApi getRestApi() {
        return restApi;
    }

    //构造方法私有
    private HttpMethods() {

        restApi =createRefrofig(RestApi.HOST).create(RestApi.class);
    }


    private Retrofit createRefrofig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .create();
        return new Retrofit.Builder()
                .client(createOkhttpClient(baseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl).build();
    }


    private OkHttpClient createOkhttpClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);



        OkHttpClient.Builder buider = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);


        buider.addInterceptor(interceptor);
        return buider.build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
