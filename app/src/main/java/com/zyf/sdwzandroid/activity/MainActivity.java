package com.zyf.sdwzandroid.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;
import com.zyf.sdwzandroid.fragment.NewsFragment;
import com.zyf.sdwzandroid.fragment.NotificationFragment;
import com.zyf.sdwzandroid.fragment.PersonalFrament;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private int currentTabIndex = 0;
    private List<Fragment> mFragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        //初始化底部导航栏
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_news, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_notification, "通知"))
                .addItem(new BottomNavigationItem(R.drawable.ic_personal, "个人"))
                .initialise();
        //初始化fragment
        mFragments.add(new NewsFragment());
        mFragments.add(new NotificationFragment());
        mFragments.add(new PersonalFrament());

        //默认加载第集合里一个fragment(newsfragment)
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, mFragments.get(0)).commit();

    }

    @Override
    protected void initData() {
        bottomNavigationBar //设置底部导航按钮点击事件
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        int index = 0;

                        switch (position) {
                            case 0:
                                index = 0;
                                break;
                            case 1:
                                index = 1;
                                break;
                            case 2:
                                index = 2;
                                break;
                            case 3:
                                index = 3;
                                break;
                        }
                    //根据点击按钮 的角标  来切换fragment
                        if (currentTabIndex != index) {
                            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                            trx.hide(mFragments.get(currentTabIndex));
                            if (!mFragments.get(index).isAdded()) {
                                trx.add(R.id.fl_container, mFragments.get(index));
                            }
                            trx.show(mFragments.get(index)).commit();
                        }
                        currentTabIndex = index;
                    }

                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                });
    }

    //初始化标题栏搜索按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //搜索栏按钮点击事件
        switch (item.getItemId()) {
            case R.id.action_search:
               startActivity(new Intent(this,SearchActivity.class));
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
