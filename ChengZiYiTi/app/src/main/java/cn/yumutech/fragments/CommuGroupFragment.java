package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuGroupFragment extends BaseFragment {
    private static CommuGroupFragment fragment;
    private View groupView;
    public CommuGroupFragment() {
        // Required empty public constructor
    }
    public static CommuGroupFragment newInstance() {
        if(fragment==null)
            fragment = new CommuGroupFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        groupView = inflater.inflate(R.layout.fragment_commu_group, container, false);
        return groupView;
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
