package com.zyf.sdwzandroid.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 46442 on 2018/3/12.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder mUnBinder;
    public Activity mContext;
    public ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            return inflater.inflate(getLayoutId(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        progressDialog  =new ProgressDialog(mContext);
        mUnBinder = ButterKnife.bind(this, view);
        initView();
        initData();
    }

    public abstract int  getLayoutId();
    public abstract void initView();
    public abstract void initData();






    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    public void showProgress() {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }


    }


    public void hideProgress() {
        progressDialog.cancel();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
        progressDialog=null;
        mUnBinder.unbind();

    }
}
