package com.zyf.sdwzandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.zyf.sdwzandroid.R;
import com.zyf.sdwzandroid.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 46442 on 2018/4/21.
 */

public class NotifyDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notify_details;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String content = intent.getStringExtra("content");
        setData(username,content);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String username = intent.getStringExtra("username");
        String content = intent.getStringExtra("content");
        setData(username,content);
    }

    public void setData(String username,String content) {
        if (!TextUtils.isEmpty(username))tvUsername.setText(username);
        if (!TextUtils.isEmpty(content))tvContent.setText(content);

    }


}
