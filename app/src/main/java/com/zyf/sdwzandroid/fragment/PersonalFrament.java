package com.zyf.sdwzandroid.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.activity.CollectActivity;
import com.zyf.sdwzandroid.activity.FileListActivity;
import com.zyf.sdwzandroid.base.BaseFragment;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 46442 on 2018/3/12.
 */

public class PersonalFrament extends BaseFragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_class_name)
    LinearLayout llClassName;
    @BindView(R.id.tv_my_collect)
    TextView tvMyCollect;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.tv_file_list)
    TextView tvFileList;

    private User user;

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frgment_personal;
    }

    @Override
    public void initData() {
        user = App.getInstance().getUser();
        String name = user.getName();
        String classname = user.getClassname();
        if (TextUtils.isEmpty(name)) {
            tvName.setText("未设置");
        } else {
            tvName.setText(name);
        }

        if (TextUtils.isEmpty(classname)) {
            tvClassName.setText("未设置");
        } else {
            tvClassName.setText(classname);
        }


        tvMyCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CollectActivity.class));
            }
        });

        tvFileList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,FileListActivity.class));
            }
        });

        llName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetNameDialog();
            }
        });

        llClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetClassNameDialog();
            }
        });

    }

    public void showSetNameDialog() {
        final EditText editText = new EditText(mContext);
        new AlertDialog.Builder(mContext)
                .setTitle("设置名字")
                .setView(editText)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        if (editText.length() == 0) editText.setError("内容不能为空");
                        final String newName = editText.getText().toString();

                        HttpMethods.getInstance().getRestApi().setName(user.getUsername(), newName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<ResponseBody>() {
                                    @Override
                                    public void accept(ResponseBody responseBody) throws Exception {
                                        Toast.makeText(mContext, "设置名字成功!", Toast.LENGTH_SHORT).show();
                                        tvName.setText(newName);
                                        dialogInterface.dismiss();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                    }
                                })
                        ;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void showSetClassNameDialog() {
        final EditText editText = new EditText(mContext);
        new AlertDialog.Builder(mContext)
                .setTitle("设置班级")
                .setView(editText)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        if (editText.length() == 0) editText.setError("内容不能为空");

                        final String newClassName = editText.getText().toString();
                        HttpMethods.getInstance().getRestApi().setClassName(user.getUsername(), newClassName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<ResponseBody>() {
                                    @Override
                                    public void accept(ResponseBody responseBody) throws Exception {
                                        Toast.makeText(mContext, "设置班级成功!", Toast.LENGTH_SHORT).show();
                                        tvClassName.setText(newClassName);
                                        dialogInterface.dismiss();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }



}
