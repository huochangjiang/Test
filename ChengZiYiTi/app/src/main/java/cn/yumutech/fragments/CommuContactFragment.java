package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import cn.yumutech.Adapter.ExpanderAdapter;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuContactFragment extends BaseFragment{

    private static CommuContactFragment fragment;
    private View contactView;
    private ExpandableListView expandableListView;

    private ExpanderAdapter mAdapter;
//    private List<ChindClass> chindClasses1=new ArrayList<>();


    public CommuContactFragment() {

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
        initExtra();
        expandableListView = (ExpandableListView) contentView.findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, DralayoutFragment.newInstance()).commitAllowingStateLoss();


    }

    /**
     * 获取初始化数据
     */
    private void initExtra(){

    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initDatas() {


//        groupsDatas.add(new GroupClass("资阳市干部"));
//        groupsDatas.add(new GroupClass("资阳市干部"));
//        ChindClass chanData= new ChindClass("cc",R.drawable.next);
//        ChindClass chanData12= new ChindClass("cc1",R.drawable.next);
//        ChindClass chanData3= new ChindClass("cc2",R.drawable.next);
//        ChindClass chanData4= new ChindClass("cc3",R.drawable.next);
//        ChindClass chanData5= new ChindClass("cc4",R.drawable.next);
//        ChindClass chanData6= new ChindClass("cc5",R.drawable.next);
//        chindClasses.add(chanData);
//        chindClasses.add(chanData12);
//        chindClasses.add(chanData3);
//        chindClasses.add(chanData4);
//        chindClasses.add(chanData5);
//        chindClasses.add(chanData6);
//        chindDatas.add(chindClasses);
         mAdapter=new ExpanderAdapter(mActivity, SaveData.getInstance().groupsDatas,SaveData.getInstance().chindDatas);
        expandableListView.setAdapter(mAdapter);
    }




}
