package com.zyf.sdwzandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.Collect;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 46442 on 2018/3/12.
 */

public class NewsDetailsActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    private int newsId;
    private String title;
    private String username;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
         username = App.getInstance().getUser().getUsername();
        //获取点击新闻列表item时 intent过来的数据
        newsId = getIntent().getIntExtra("newsId", -1);
        title = getIntent().getStringExtra("title");

        tvTitle.setText(title);

        //收藏按钮点击事件
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivCollect.isSelected()){//收藏过
                    cancelCollect();
                }else {//没收藏过
                    collectNews();
                }

            }
        });
        //返回按钮点击事件
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭这个activity
                finish();
            }
        });

        //获取新闻详情
        HttpMethods.getInstance().getRestApi().getNewsDetails(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        webview.loadDataWithBaseURL(null, string, "text/html", "utf-8", null);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });



        //获取当前新闻是否被收藏

        HttpMethods.getInstance().getRestApi().getCollect(username, newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Collect>() {
                    @Override
                    public void accept(Collect collect) throws Exception {
                        //判断是否有这个收藏
                      ivCollect.setSelected(collect!=null);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void cancelCollect() {

        HttpMethods.getInstance().getRestApi().cancelCollect(username,newsId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                ivCollect.setSelected(false);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        })
        ;
    }

    /**
     * 发送收藏新闻请求
     */
    private void collectNews() {
        String username = App.getInstance().getUser().getUsername();
        HttpMethods.getInstance().getRestApi().collectionNews(username, newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        showToas("收藏成功");
                        ivCollect.setSelected(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
        ;


    }


}
