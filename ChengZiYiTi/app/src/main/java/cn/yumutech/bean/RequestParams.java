package cn.yumutech.bean;

/**
 * Created by 霍长江 on 2016/11/17.
 */
public class RequestParams {
    public RequestParams(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }


    public UserBean user;


    public DataBean data;
    public static class UserBean {
        public String account;
        public String session;

        public UserBean(String account, String session) {
            this.account = account;
            this.session = session;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }
    }

    public static class DataBean {
        public String dept_id;
        public String userIds;
        public String groupId;
        public String groupName;
        public DataBean(String id) {
            this.dept_id = id;
        }
        public DataBean(String userIds, String groupId, String groupName) {
            this.userIds = userIds;
            this.groupId = groupId;
            this.groupName = groupName;
        }
    }
}
