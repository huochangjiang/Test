package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;


public class PersonMailstFragment extends BaseFragment {

    private View rootView;


    private static PersonMailstFragment fragment;

    public PersonMailstFragment() {
        // Required empty public constructor
    }


    public static PersonMailstFragment newInstance() {
        if(fragment==null){
            fragment=new PersonMailstFragment();
        }
        return fragment;
    }


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_person_mailst, container, false);
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
