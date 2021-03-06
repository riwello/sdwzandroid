package com.zyf.sdwzandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.sdwzandroid.App;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.Collect;
import com.zyf.sdwzandroid.model.entity.News;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/14.
 */

public class CollectActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<News, BaseViewHolder> mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        //设置recyclerview的布局方式(水平垂直)
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //初始化适配器
        mAdapter = new BaseQuickAdapter<News, BaseViewHolder>(R.layout.item_news) {
            @Override
            protected void convert(BaseViewHolder helper, News item) {
                //布局绑定数据
                helper.setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd").format(item.getTime()));
            }
        };
        //设置适配器
        recyclerview.setAdapter(mAdapter);

        //设置每个item的点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的item的数据
                News item = mAdapter.getItem(position);
                //跳转到新闻详情activity
                Intent intent = new Intent(CollectActivity.this, NewsDetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("newsId", item.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        //用http get请求 获取收藏列表数据
        String username = App.getInstance().getUser().getUsername();
        HttpMethods.getInstance().getRestApi().getCollectList(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> news) throws Exception {
                        mAdapter.replaceData(news);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
        ;

    }

}
