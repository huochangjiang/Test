package cn.yumutech.bean;

/**
 * Created by 霍长江 on 2016/11/17.
 */
public class RequestCanshu2 {
    public RequestCanshu2(UserBean user, DataBean data) {
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

        public String groupId;
        public String userIds;
        public String userId;


        public DataBean(String id) {
            this.groupId = id;
        }

        public DataBean(String groupId, String userIds) {
            this.groupId = groupId;
            this.userIds = userIds;
        }

    }
}

