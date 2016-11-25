package cn.yumutech.weight;

/**
 * Created by Allen on 2016/11/26.
 */
public class GroupClassInt {
    public String name;
    public int dept_id;
    public int dept_parent_id;

    public GroupClassInt(String name,int dept_id,int dept_parent_id) {
        this.name = name;
        this.dept_id = dept_id;
        this.dept_parent_id=dept_parent_id;
    }
}
