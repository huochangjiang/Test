package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Allen on 2016/11/26.
 */
public class ModuleClassifyList {
    public status status;
    public List<data> data;
    public static class status{
        public String message;
        public String code;
    }
    public static class data{
        public String value;
        public String key;
    }
}
