package com.zyf.sdwzandroid.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseFragment;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.Notification;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 46442 on 2018/3/12.
 */

public class NotificationFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    BaseQuickAdapter<Notification, BaseViewHolder> adapter;

    @Override
    public int getLayoutId() {
        return R.layout.frgment_notification;
    }

    @Override
    public void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<Notification, BaseViewHolder>(R.layout.item_notification) {

            @Override
            protected void convert(BaseViewHolder helper, Notification item) {
                helper.setText(R.id.tv_author, "发布者:"+item.getUsername())
                        .setText(R.id.tv_content, item.getContent())
                        .setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(item.getTime()));
            }
        };
        recyclerview.setAdapter(adapter);


        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

    }

    public void showDialog() {
        final EditText editText = new EditText(mContext);
        new AlertDialog.Builder(mContext)
                .setTitle("发布消息")
                .setView(editText)
                .setPositiveButton("发布", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        if (editText.length()==0)editText.setError("内容不能为空");
                        String username = App.getInstance().getUser().getUsername();

                        HttpMethods.getInstance().getRestApi().addNotification(username,editText.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                                getNotificationList();
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


    @Override
    public void initData() {
        getNotificationList();
    }

    public void getNotificationList(){
        HttpMethods.getInstance().getRestApi().getNotificationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Notification>>() {
                    @Override
                    public void accept(List<Notification> notifications) throws Exception {
                        adapter.replaceData(notifications);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }


}
