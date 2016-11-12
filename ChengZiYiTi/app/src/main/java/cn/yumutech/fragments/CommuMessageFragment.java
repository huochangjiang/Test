package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuMessageFragment extends BaseFragment{
    private static CommuMessageFragment fragment;
    private View messageView;
    public CommuMessageFragment() {
        // Required empty public constructor
    }
    public static CommuMessageFragment newInstance() {
        if(fragment==null)
            fragment = new CommuMessageFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        messageView = inflater.inflate(R.layout.fragment_commu_message, container, false);
        return messageView;
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
