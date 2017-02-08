package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;


public class PersonFragment extends BaseFragment {
    private View rootView;
    private TextView textView;
    private static PersonFragment fragment;

    public PersonFragment() {
        // Required empty public constructor
    }

    public static PersonFragment newInstance() {
        if(fragment==null)
        fragment = new PersonFragment();
        return fragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_person, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        textView = (TextView) contentView.findViewById(R.id.four);
    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initDatas() {
        textView.setText("four");
    }
}
