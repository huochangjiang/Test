package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuContactFragment extends BaseFragment{

    private static CommuContactFragment fragment;
    private View contactView;
    public CommuContactFragment() {
        // Required empty public constructor
    }
    public static CommuContactFragment newInstance() {
        if(fragment==null)
            fragment = new CommuContactFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);
        return contactView;
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
