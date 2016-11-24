package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserBean;
import cn.yumutech.unity.App;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/24.
 */
public class MyJoinMenberAdpater extends BaseAdapter{
    private List<UserAboutPerson.DataBean> mDatas;
    private Context mContext;
    private int type;
    private Map<Integer,UserAboutPerson.DataBean> maps=new HashMap<>();
    public MyJoinMenberAdpater(List<UserAboutPerson.DataBean> data,Context context){
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
        final ViewHolder vh;
        final int index=position;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.member_item,null);
            vh.cb= (ImageView) convertView.findViewById(R.id.cb);
            vh.tv= (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(mDatas.get(position).nickname);
        if(mDatas.get(index).type == UserBean.TYPE_CHECKED){
            vh.cb.setImageResource(R.drawable.story_selector);
        }else{
            vh.cb.setImageResource(R.drawable.story_wei);

        }
        if(mDatas.get(index).id.equals(App.getContext().getLogo("logo").data.id)){
            vh.cb.setImageResource(R.drawable.story_selector);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mDatas.get(index).type==UserBean.TYPE_CHECKED){
                        mDatas.get(index).type=UserBean.TYPE_NOCHECKED;
                            if(maps!=null&&maps.size()>0) {
                                vh.cb.setImageResource(R.drawable.story_wei);
                                maps.remove(index);
                                ids.getMenmberIds(maps);
                        }
                    }else {
                           mDatas.get(index).type = UserBean.TYPE_CHECKED;
                            vh.cb.setImageResource(R.drawable.story_selector);
                            maps.put(index, mDatas.get(index));
                            ids.getMenmberIds(maps);
                }
            }
        });
        return convertView;
    }
    public class ViewHolder{
        public ImageView cb;
        public TextView tv;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,UserAboutPerson.DataBean> beans);
    }
}
