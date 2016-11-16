package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyMenmberAdapter;
import cn.yumutech.bean.UserBean;

public class QunMenmberSelectorActivity extends BaseActivity {


    private TextView tv_quer;
    private ListView listView;
    private List<UserBean> mDatas = new ArrayList<>();
    private MyMenmberAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qun_menmber_selector;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tv_quer = (TextView) findViewById(R.id.tv_qure);
        listView = (ListView) findViewById(R.id.listview);
        mDatas.add(new UserBean("cc", "3"));
        mDatas.add(new UserBean("cc1", "4"));
        mDatas.add(new UserBean("cc1", "5"));
        mDatas.add(new UserBean("cc1", "6"));
        mDatas.add(new UserBean("cc1", "7"));
        mDatas.add(new UserBean("cc1", "8"));
        mDatas.add(new UserBean("cc1", "9"));
        mDatas.add(new UserBean("cc1", "10"));
        mDatas.add(new UserBean("cc1", "11"));
        mDatas.add(new UserBean("cc1", "12"));
        mDatas.add(new UserBean("cc1", "13"));
        mDatas.add(new UserBean("cc1", "14"));
        mDatas.add(new UserBean("cc1", "15"));
        mDatas.add(new UserBean("cc1", "16"));
        mDatas.add(new UserBean("cc1", "17"));
        mDatas.add(new UserBean("cc1", "18"));
        mDatas.add(new UserBean("cc1", "19"));
        mDatas.add(new UserBean("cc1", "20"));
        mDatas.add(new UserBean("cc1", "21"));
        mDatas.add(new UserBean("cc1", "22"));
        mDatas.add(new UserBean("cc1", "23"));
        mAdapter = new MyMenmberAdapter(mDatas, this);
        listView.setAdapter(mAdapter);
controlTitle(findViewById(R.id.back));

    }

    String mIds;

    @Override
    protected void initData() {
        mAdapter.setLisener(new MyMenmberAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserBean> beans) {
                mIds = getMemberIds(beans);
                Log.e("info",mIds+"--------");
            }
        });
        tv_quer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIds!=null&&!mIds.equals("")) {
                    Intent intent = new Intent(QunMenmberSelectorActivity.this, CreateQunZhuActivity.class);
                    intent.putExtra("id", mIds);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(QunMenmberSelectorActivity.this, "请选择成员", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initListeners() {
    }

    List<String> iids = new ArrayList<>();

    private String getMemberIds(Map<Integer, UserBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserBean val = (UserBean) entry.getValue();
            iids.add(val.id);
        }
        for (int i = 0; i < iids.size(); i++) {

            if (i == iids.size() - 1) {
                sb.append(iids.get(i));
            } else {
                sb.append(iids.get(i) + ",");
            }
        }
        return sb.toString();
    }
}
