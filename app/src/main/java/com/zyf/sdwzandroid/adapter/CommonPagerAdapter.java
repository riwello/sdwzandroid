package com.zyf.sdwzandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zyf.sdwzandroid.base.BaseFragment;

import java.util.List;

/**
 * Created by 46442 on 2018/3/12.
 */

public class CommonPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private List<Fragment> fragments;


    public CommonPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
