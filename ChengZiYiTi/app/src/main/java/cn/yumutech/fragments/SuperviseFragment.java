package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;


public class SuperviseFragment extends BaseFragment {
    private View rootView;
    private TextView textView;
    private static SuperviseFragment fragment;

    public SuperviseFragment() {
        // Required empty public constructor
    }
    public static SuperviseFragment newInstance() {
        if(fragment==null)
        fragment = new SuperviseFragment();

        return fragment;
    }


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_supervise, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        textView = (TextView) contentView.findViewById(R.id.two);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        textView.setText("two");
    }
}
