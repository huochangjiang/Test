package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class ExchangeCommenList {
    public status status;
    public List<data> data;
    public static class status{
        public String message;
        public String code;
    }
    public static class data{
        public String exchange_id;
        public String comment_id;
        public String content;
        public String publish_user_id;
        public String publish_user_name;
        public String publish_date;
        public String receiver_user_id;
        public String receiver_user_name;
    }
}
