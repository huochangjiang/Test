package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserBean;
import cn.yumutech.unity.R;

/**
 * Created by 霍长江 on 2016/11/16.
 */
public class MyMenmberAdapter extends BaseAdapter{

    private List<UserAboutPerson.DataBean> mDatas;
    private Context mContext;
    private int type;
    private Map<Integer,UserAboutPerson.DataBean> maps=new HashMap<>();
    public MyMenmberAdapter(List<UserAboutPerson.DataBean> data,Context context){
        this.mDatas=data;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
    getIds ids;
    public void setLisener(getIds id){
        this.ids=id;
    }
    @Override
    public UserAboutPerson.DataBean getItem(int position) {
        return mDatas.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        final int index=position;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.member_item,null);
            vh.cb= (CheckBox) convertView.findViewById(R.id.cb);
            vh.tv= (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(mDatas.get(position).nickname);
        if(mDatas.get(index).type == UserBean.TYPE_CHECKED){
            vh.cb.setChecked(true);
        }else{
            vh.cb.setChecked(false);
        }
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDatas.get(index).type==UserBean.TYPE_CHECKED){
                    mDatas.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null){
                        if(maps!=null&&maps.size()>0) {
                            maps.remove(index);
                            ids.getMenmberIds(maps);
                        }
                    }
                }else{
                    mDatas.get(index).type=UserBean.TYPE_CHECKED;
                    if(ids!=null){
                            maps.put(index,mDatas.get(index));
                            ids.getMenmberIds(maps);
                    }
                }
            }
        });


        return convertView;
    }
    public class ViewHolder{
        public CheckBox cb;
        public TextView tv;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,UserAboutPerson.DataBean> beans);
    }

}
