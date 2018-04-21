package com.zyf.sdwzandroid.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.activity.MainActivity;
import com.zyf.sdwzandroid.activity.NotifyDetailsActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by 46442 on 2018/4/2.
 */

public class WebSocketService extends WebSocketListener {
    private String TAG = "WebSocket";
    private WebSocket mWebSocket;
    public static final int DEFAULT_TIMEOUT = 10 * 1000;

    private boolean isShowTradeMeg = false;//是否打印trade的消息

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final WebSocketService INSTANCE = new WebSocketService();
    }

    //获取单例
    public static WebSocketService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private WebSocketService() {
    }


    public void connection() {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Request request = new Request.Builder()
                        .url("ws://10.0.2.2:8111/websocket")
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .build();

                client.newWebSocket(request, WebSocketService.this);
                client.dispatcher().executorService().shutdown();

                e.onNext("");
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        mWebSocket = webSocket;
        Log.i(TAG, "onOpen: request url : " + response.request().url());


    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
//        Log.d(TAG, "onMessage:" + text);
        Log.d(TAG, "onMessage:" + text);
        showNotifictionIcon(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "onMessage: bytes= " + bytes.toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e(TAG, "onClosing: code= " + code + " ,reason= " + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.e(TAG, "onClosed: code= " + code + " ,reason= " + reason);
//        connection("");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAG, "onFailure: Throwable= " + t.getMessage());
//        ResponseBody body = response.body();
//        try {
//            Log.e(TAG, "onFailure: body= " + body.string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        t.printStackTrace(new PrintStream(baos));
//        String exception = baos.toString();
//        Log.e(TAG, exception);

    }

    public static void showNotifictionIcon(String msg) {
        if (TextUtils.isEmpty(msg))return;
        com.zyf.sdwzandroid.model.entity.Notification notification1 = new Gson().fromJson(msg, com.zyf.sdwzandroid.model.entity.Notification.class);
        Context context = App.getInstance();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("111", "msg", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false); //是否在桌面icon右上角展示小红点
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "111");
        } else {
            builder = new NotificationCompat.Builder(App.getInstance());
        }

        Intent intent = new Intent(App.getInstance(), NotifyDetailsActivity.class);
        intent.putExtra("username",notification1.getUsername());
        intent.putExtra("content",notification1.getContent());
        PendingIntent pi = PendingIntent.getActivity(
                App.getInstance(),
                100,intent
               ,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        builder.setSmallIcon(R.drawable.ic_news);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_news));
        builder.setContentTitle("消息通知");   //通知栏标题
        builder.setContentText(notification1.getContent());     //通知栏内容
        builder.setContentIntent(pi);
        builder.setTicker("新消息"); //显示在状态栏
        Notification notification = builder.build();
        mNotificationManager.notify(111, notification);


    }

}
