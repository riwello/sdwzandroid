package com.zyf.sdwzandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.activity.NewsDetailsActivity;
import com.zyf.sdwzandroid.base.BaseFragment;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.model.entity.News;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/12.
 * 新闻子页面
 */

public class NewsTabFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private String newsType;
    private BaseQuickAdapter<News, BaseViewHolder> mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.frgment_news_tab;
    }

    @Override
    public void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<News, BaseViewHolder>(R.layout.item_news) {

            @Override
            protected void convert(BaseViewHolder helper, News item) {
                helper.setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd").format(item.getTime()))
                ;
            }
        };
        recyclerview.setAdapter(mAdapter);
//下拉刷新监听
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsData();
            }
        });
    }

    int page = 1;
    int size = 10;

    @Override
    public void initData() {

        //http get 请求获取新闻列表
        getNewsData();

        //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                News item = mAdapter.getItem(position);

                //跳转到详情
                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("newsId", item.getId());
                startActivity(intent);
            }
        });
    }


    public void getNewsData() {
        HttpMethods.getInstance().getRestApi().getNews(page, size, newsType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> news) throws Exception {
                        //设置新闻列表数据
                        mAdapter.replaceData(news);
                        refresh.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    ;

    //设置新闻类型
    public Fragment setType(String newsType) {
        this.newsType = newsType;

        return this;
    }

}
