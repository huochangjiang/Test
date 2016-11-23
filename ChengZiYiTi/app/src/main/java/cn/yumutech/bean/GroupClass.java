package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/13.
 */
public class GroupClass {
    public String name;
    public String dept_id;
    public String dept_parent_id;

    public GroupClass(String name,String dept_id,String dept_parent_id) {
        this.name = name;
        this.dept_id = dept_id;
        this.dept_parent_id=dept_parent_id;
    }
}
