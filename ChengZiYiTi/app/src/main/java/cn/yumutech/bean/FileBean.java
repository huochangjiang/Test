package cn.yumutech.bean;


import cn.yumutech.tree.been.TreeNodeId;
import cn.yumutech.tree.been.TreeNodeLabel;
import cn.yumutech.tree.been.TreeNodePid;

public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;

	public FileBean(int _id, int parentId, String name)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
	}

}
