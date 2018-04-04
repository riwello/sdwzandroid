package com.zyf.sdwzandroid;

import android.app.Application;

import com.zyf.sdwzandroid.model.WebSocketService;
import com.zyf.sdwzandroid.model.entity.User;

/**
 * Created by 46442 on 2018/3/12.
 * 自定义application
 */

public class App extends Application {
    public static App instance;
    public static App getInstance(){
        return instance;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;

        WebSocketService.getInstance().connection();
    }
}
