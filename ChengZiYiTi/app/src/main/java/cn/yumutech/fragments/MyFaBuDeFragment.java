package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/21.
 */
public class MyFaBuDeFragment extends BaseFragment{
    private View view;
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        view =inflater.inflate(R.layout.my_fa_bu_de_fragemnt,container,false);
        return view;
    }

    @Override
    protected void initViews(View contentView) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {

    }
}
