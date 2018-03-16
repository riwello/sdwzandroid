package com.zyf.sdwzandroid.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/14.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_affirm)
    EditText etPasswordAffirm;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        register();

    }

    private void register() {
        if (etAccount.length() == 0) {
            etAccount.setError("请输入帐号");
            return;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("请输入密码");
            return;
        }
        if (etPasswordAffirm.length() == 0) {
            etPasswordAffirm.setError("再输入一次密码");
            return;
        }
        String username = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        String PasswordAffirm = etPasswordAffirm.getText().toString();
        if (!password.equals(PasswordAffirm)) {
            etPassword.setText("");
            etPasswordAffirm.setText("");
            etPassword.setError("两次密码不一样");
            return;
        }
        showLoding();
        //http请求  注册
        HttpMethods.getInstance().getRestApi().register(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        showToas("注册成功");
                        hideLoding();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        hideLoding();
                        showToas("注册失败");
                    }
                });
    }
}
