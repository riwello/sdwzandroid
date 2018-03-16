package com.zyf.sdwzandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.model.HttpMethods;
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

public class SearchActivity extends BaseActivity {


    @BindView(R.id.et_search_input)
    EditText etSearchInput;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<News, BaseViewHolder> mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.frgment_search;
    }

    @Override
    public void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<News, BaseViewHolder>(R.layout.item_news) {

            @Override
            protected void convert(BaseViewHolder helper, News item) {
                helper.setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd").format(item.getTime()));
            }
        };
        recyclerview.setAdapter(mAdapter);


    }

    @Override
    public void initData() {
        //搜索按钮点击事件
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = etSearchInput.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    etSearchInput.setError("请输入关键字");
                    return;
                }
                search(keyword);
            }
        });


        //item 点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                News item = mAdapter.getItem(position);

                Intent intent = new Intent(SearchActivity.this, NewsDetailsActivity.class);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("newsId",item.getId());
                startActivity(intent);
            }
        });
    }

    int page = 1;
    int size = 10;

    /**
     * http请求 搜索新闻
     * @param word 关键字
     */
    public void search(String word) {
        showLoding();
        HttpMethods.getInstance().getRestApi().searchNews(page, size, word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> newsList) throws Exception {

                        if (newsList==null&&newsList.size()==0)showToas("搜索不到新闻!");

                        mAdapter.replaceData(newsList);
                        hideLoding();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        showToas("搜索失败");
                        hideLoding();
                    }
                })
        ;
    }

}
