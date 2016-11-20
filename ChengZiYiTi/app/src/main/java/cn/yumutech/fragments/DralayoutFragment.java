package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;


public class DralayoutFragment extends BaseFragment {
    private View rootView;
    public DralayoutFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DralayoutFragment newInstance() {
        DralayoutFragment fragment = new DralayoutFragment();
        return fragment;
    }


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_dralayout, container, false);
        return rootView;
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
