package cn.yumutech.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupListAdapter;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.QunMenmberSelectorActivity;
import cn.yumutech.unity.R;
import cn.yumutech.unity.TaShanZhiShiActivity;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuGroupFragment extends BaseFragment implements View.OnClickListener{
    private static CommuGroupFragment fragment;
    private View groupView;
    private ListView listview;
    private GroupListAdapter adapter;
    private RelativeLayout two;
    private List<String> data=new ArrayList<>();

    private String[] mData={"东京不热1群","东京不热2群","东京不热3群","东京不热4群","东京不热5群","东京不热6群",
            "东京不热7群","东京不热8群","东京不热9群","东京不热10群","东京不热11群","东京不热12群","东京不热13群",
            "东京不热14群","东京不热15群","东京不热16群","东京不热17群","东京不热18群","东京不热19群"};
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
        for (int i=0;i<mData.length;i++){
            data.add(mData[i]);
        }
        listview= (ListView) contentView.findViewById(R.id.listview);
        adapter=new GroupListAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        two= (RelativeLayout) contentView.findViewById(R.id.two);
        two.setOnClickListener(this);
        RelativeLayout  one= (RelativeLayout) contentView.findViewById(R.id.one);
        one.setOnClickListener(this);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.two:
                Intent intent=new Intent();
                intent.setClass(getActivity(), TaShanZhiShiActivity.class);
                startActivity(intent);
                break;
            case R.id.one:
                Intent intent1=new Intent();
                intent1.setClass(getActivity(), QunMenmberSelectorActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
