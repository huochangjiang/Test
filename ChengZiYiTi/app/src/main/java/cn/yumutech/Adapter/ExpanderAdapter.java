package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ChindClass;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/13.
 */
public class ExpanderAdapter extends BaseExpandableListAdapter{
    public List<GroupClass> mGroupClsass;
    public List<List<ChindClass>> mChindClass;
    public Context mContext;
    public ExpanderAdapter(Context context, List<GroupClass> data, List<List<ChindClass>> datas){
        this.mGroupClsass=data;
        this.mChindClass=datas;
        this.mContext=context;
    }

    public void dataChange(List<GroupClass> data, List<List<ChindClass>> datas){
        this.mGroupClsass=data;
        this.mChindClass=datas;
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return mGroupClsass.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mChindClass.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupClsass.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mChindClass.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(mContext, R.layout.group_item,null);
            vh.textView= (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.textView.setText(mGroupClsass.get(i).name);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(mContext, R.layout.chind_item,null);
            vh.textView= (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.textView.setText(mChindClass.get(i).get(i1).name);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    class ViewHolder {
        TextView textView;
    }
}
