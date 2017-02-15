package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Allen on 2016/11/26.
 */
public class ModuleClassifyList {
    public status status;
    public List<data> data;
//    public ModuleClassifyList(status status,List<data> data){
//        this.status=status;
//        this.data=data;
//    }
    public static class status{
        public String message;
        public String code;
        public status(String message,String code){
            this.message=message;
            this.code=code;
        }
    }
    public static class data{
        public String value;
        public String key;
        public data(String value,String key){
            this.value=value;
            this.key=key;
        }
    }
}
