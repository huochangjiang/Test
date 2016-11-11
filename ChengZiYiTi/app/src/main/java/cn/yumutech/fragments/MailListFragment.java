package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;


public class MailListFragment extends BaseFragment {
    private View rootView;
    private TextView textView;
    private static MailListFragment fragment;

    public MailListFragment() {
        // Required empty public constructor
    }
    public static MailListFragment newInstance() {
        if(fragment==null)
        fragment = new MailListFragment();

        return fragment;
    }


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_mail_list, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        textView = (TextView) contentView.findViewById(R.id.three);

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        textView.setText("three");
    }
}
