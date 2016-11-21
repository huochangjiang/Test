package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class DepartListNew {
    public status status;
    public List<data> data;
    public static class status{
        public String message;
        public String code;
    }
    public static class data{
        public String dept_id;
        public String dept_name;
        public String dept_parent_id;
    }
}
