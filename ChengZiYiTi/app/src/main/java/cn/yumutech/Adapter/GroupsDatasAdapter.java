package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.GroupClass;
import cn.yumutech.unity.R;

/**
 */
   public class GroupsDatasAdapter extends BaseAdapter{
    public Context context;
    public List<GroupClass> mData;
    public GroupsDatasAdapter(Context context,List<GroupClass> mData){
        this.context=context;
        this.mData=mData;
    }
    public void dataChange(List<GroupClass> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public List<GroupClass> getItem(int i) {
        return mData;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(context, R.layout.group_item,null);
            vh.textView= (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.textView.setText(mData.get(i).name);
        return view;
    }
    class ViewHolder {
        TextView textView;
    }
}
