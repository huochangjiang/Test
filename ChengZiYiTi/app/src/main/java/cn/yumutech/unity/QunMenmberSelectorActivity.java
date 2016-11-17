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
import cn.yumutech.bean.UserAboutPerson;

public class QunMenmberSelectorActivity extends BaseActivity {


    private TextView tv_quer;
    private ListView listView;
    private List<UserAboutPerson.DataBean> mDatas = new ArrayList<>();
    private MyMenmberAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qun_menmber_selector;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tv_quer = (TextView) findViewById(R.id.tv_qure);
        listView = (ListView) findViewById(R.id.listview);

        mAdapter = new MyMenmberAdapter(App.getContext().mApbutPerson, this);
        listView.setAdapter(mAdapter);
      controlTitle(findViewById(R.id.back));

    }

    String mIds;

    @Override
    protected void initData() {
        mAdapter.setLisener(new MyMenmberAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
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

    private String getMemberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
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
