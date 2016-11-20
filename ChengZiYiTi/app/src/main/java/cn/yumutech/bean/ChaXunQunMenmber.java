package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/14.
 */
public class ChaXunQunMenmber {
    public StatusBean status;
    public DataBean data;
    public static class StatusBean {
        public String message;
        public String code;
    }
    public static class DataBean {
        public String groupId;
        public String groupName;
        public String create_date;
        public String create_user_logo_path;
        public String create_user_id;
        public String create_user_mobile;
        public String create_user_name;
        public List<UsersBean> users;
        public static class UsersBean {
            public String user_name;
            public String user_mobile;
            public String userId;
            public String user_logo_path;
        }
    }
}
