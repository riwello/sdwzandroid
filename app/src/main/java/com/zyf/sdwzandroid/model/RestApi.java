package com.zyf.sdwzandroid.model;

import com.zyf.sdwzandroid.model.entity.Collect;
import com.zyf.sdwzandroid.model.entity.FileInfo;
import com.zyf.sdwzandroid.model.entity.News;
import com.zyf.sdwzandroid.model.entity.Notification;
import com.zyf.sdwzandroid.model.entity.User;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 46442 on 2018/3/12.
 */

public interface RestApi {
    String HOST = "http://10.0.2.2:8111";


    @GET("/user/register")
    Flowable<User> register(@Query("username") String account, @Query("password") String password);

    @GET("/user/login")
    Flowable<User> login(@Query("username") String account, @Query("password") String password);

    @GET("/news/list")
    Flowable<List<News>> getNews(@Query("page") int page, @Query("size") int size, @Query("type") String newsType);

    @GET("/news/details")
    Flowable<ResponseBody> getNewsDetails(@Query("id") int id);

    @GET("/news/search")
    Flowable<List<News>> searchNews(@Query("page") int page, @Query("size") int size, @Query("word") String word);

    @GET("/collect/add")
    Flowable<ResponseBody> collectionNews(@Query("username") String username, @Query("newsid") int newsId);

    @GET("/collect/getcollect")
    Flowable<Collect> getCollect(@Query("username") String username, @Query("newsid") int newsId);

    @GET("/collect/collectList")
    Flowable<List<News>> getCollectList(@Query("username") String username);

    @GET("/notify/all")
    Flowable<List<Notification>> getNotificationList();

    @GET("/notify/add")
    Flowable<ResponseBody> addNotification(@Query("username") String username, @Query("content") String content);

    @GET("/user/updatename")
    Flowable<ResponseBody> setName(@Query("username") String username, @Query("name") String name);

    @GET("/user/updateclassname")
    Flowable<ResponseBody> setClassName(@Query("username") String username, @Query("classname") String classname);
    @GET("/news/banner")
    Flowable<List<String>> getBanner();


    @POST("/file/upload")
    Flowable<ResponseBody> uploadFile(@Body RequestBody Body);

    @GET("/collect/delete")
    Flowable<ResponseBody> cancelCollect(@Query("username") String username, @Query("newsid") int newsId);

    @GET("/file/filelist")
    Flowable<List<FileInfo>> getFileList();
}
