package com.zyf.sdwzandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.User;
import com.zyf.sdwzandroid.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/12.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        String username = SPUtils.getString(SPUtils.USER_NAME, "");
        String pwd = SPUtils.getString(SPUtils.PWD, "");
        etAccount.setText(username);
        etPassword.setText(pwd);
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    private void login() {
        if (etAccount.length() == 0) {
            etAccount.setError("请输入用户名");
            return;
        }
        if (etPassword.length() == 0) {
            etAccount.setError("请输入密码");
            return;
        }
        final String account = etAccount.getText().toString();
        final String password = etPassword.getText().toString();
        showLoding();

        //http 请求  登录
        HttpMethods.getInstance().getRestApi().login(account, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        hideLoding();

                        SPUtils.putString(SPUtils.USER_NAME,account);
                        SPUtils.putString(SPUtils.PWD,password);
                        //设置用户数据到全局变量
                        App.getInstance().setUser(user);
                        //跳转到主页
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoding();
                        throwable.printStackTrace();
                        showToas("登录失败");
                    }
                });
    }


}
