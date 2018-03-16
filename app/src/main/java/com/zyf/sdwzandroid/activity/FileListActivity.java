package com.zyf.sdwzandroid.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.FileInfo;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/15.
 */

public class FileListActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<FileInfo, BaseViewHolder> mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file_list;
    }

    @Override
    protected void initView() {
        //设置recyclerview的布局方式(水平垂直)
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //初始化适配器
        mAdapter = new BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_file) {

            @Override
            protected void convert(BaseViewHolder helper, FileInfo item) {
                //布局绑定数据
                helper.setText(R.id.tv_file_name, item.getFileName())
                        .setText(R.id.tv_size, Formatter.formatFileSize(FileListActivity.this, item.getSize()))
                        .addOnClickListener(R.id.btn_download)
                ;


            }
        };
        //设置适配器
        recyclerview.setAdapter(mAdapter);
    }


    @Override
    protected void initData() {
        //http get请求获取文件列表
        HttpMethods.getInstance().getRestApi().getFileList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FileInfo>>() {
                    @Override
                    public void accept(List<FileInfo> fileInfos) throws Exception {
                        mAdapter.replaceData(fileInfos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        //设置每个item的点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的item的数据
                FileInfo item = mAdapter.getItem(position);

                //下载文件
                downloadFile(item.getUrl(), item.getFileName());
            }
        });
        //6.0读写sd卡权限
        verifyStoragePermissions(this);
    }

    //下载器
    private DownloadManager downloadManager;
    //上下文




    /**
     * 下载文件
     * @param url  文件地址
     * @param name 文件名
     */
    public void downloadFile(String url, String name) {

        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(true);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载文件");
        request.setVisibleInDownloadsUi(true);
        Log.e("file", "downloadFile: "+ getFilesDir().getPath());


        //设置下载的路径
        request.setDestinationInExternalPublicDir(getFilesDir().getPath(), name);

        //获取DownloadManager
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        long enqueue = downloadManager.enqueue(request);


    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    /**
     * 检查外置卡读取权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }



}
