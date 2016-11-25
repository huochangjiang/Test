package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/25.
 */
public class ShengJiRequest {

    public ShengJiRequest(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }

    /**
     * account : 
     * session : 
     */


    public UserBean user;
    /**
     * os : Android
     */

    public DataBean data;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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
        public String os;

        public DataBean(String os) {
            this.os = os;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }
    }
}
