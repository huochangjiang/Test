package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.GroupClass;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

	public boolean isadd;
	public  List<GroupClass> mData1=new ArrayList<>();
	public  List<GroupClass> mData=new ArrayList<>();
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
							 int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.id_treenode_icon);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.id_treenode_label);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1) {
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}
//		if(SaveData.getInstance().shuXingData.size()==SaveData.getInstance().taskToChildGroups.size()){
//			isadd=true;
//		}
//		if(isadd){
		SaveData.getInstance().shuXingData.add(new GroupClass(node.getName(), node.getId() + "", node.getpId() + ""));
//	}

		viewHolder.label.setText(node.getName());
		
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView icon;
		TextView label;
	}

}
