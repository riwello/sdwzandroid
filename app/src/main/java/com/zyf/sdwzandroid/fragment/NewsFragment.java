package com.zyf.sdwzandroid.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.adapter.CommonPagerAdapter;
import com.zyf.sdwzandroid.base.BaseFragment;
import com.zyf.sdwzandroid.model.HttpMethods;
import com.zyf.sdwzandroid.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 46442 on 2018/3/12.
 */

public class NewsFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.banner)
    Banner banner;


    @Override
    public int getLayoutId() {
        return R.layout.frgment_news;
    }

    @Override
    public void initData() {
        //viewpager加载每个新闻tab子页面
        String[] titles = new String[]{"学院要闻", "校园动态", "媒体聚焦", "学院公告", "文正讲坛", "E海报"};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new NewsTabFragment().setType("xyyw"));
        fragments.add(new NewsTabFragment().setType("xydt"));
        fragments.add(new NewsTabFragment().setType("mtjj"));
        fragments.add(new NewsTabFragment().setType("xygg"));
        fragments.add(new NewsTabFragment().setType("wzjt"));
        fragments.add(new NewsTabFragment().setType("ehb"));
        //设置适配器
        viewpager.setAdapter(new CommonPagerAdapter(getChildFragmentManager(), fragments, titles));

        //tablayout 和viewpager 绑定
        tabLayout.setupWithViewPager(viewpager);

        //轮播图
        banner.setImageLoader(new GlideImageLoader());
        List<Integer  > imgas=new ArrayList<>();
        imgas.add(R.drawable.a);
        imgas.add(R.drawable.b);
        imgas.add(R.drawable.c);
        banner.setImages(imgas);
        banner.start();



    }

    @Override
    public void initView() {

    }


}
