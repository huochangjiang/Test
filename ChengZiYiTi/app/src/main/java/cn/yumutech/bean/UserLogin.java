package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/12.
 */
public class UserLogin {
    public status status;
    public data data;
    public static class status{
        public String message;
        public String code;
    }
    public static class data{
        public String id;
        public String birthday;
        public String nickname;
        public String address;
        public String token;
        public String logo_path;
        public String gender;
        public String mobile;
        public String dept_id;
        public String dept_name;
        public String publish_task_flag;
    }
}
