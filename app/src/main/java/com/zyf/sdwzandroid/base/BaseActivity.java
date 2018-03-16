package com.zyf.sdwzandroid.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 46442 on 2018/3/12.
 * 封装的activity父类
 * 抽取重复代码
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        initView();
        initData();
    }

    public abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    public void showLoding() {
        if (progressDialog != null) progressDialog.show();
    }

    public void hideLoding() {
        if (progressDialog != null) progressDialog.cancel();
    }

    public void showToas(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
    }
}
