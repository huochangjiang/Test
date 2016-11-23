package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/23.
 */
public class DeviceTokenUpLoad {

    /**
     * account : 1
     * session : 1234567890
     */

    public UserBean user;

    public DeviceTokenUpLoad(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }

    /**
     * device_token_type : Android
     * device_token : 123123213123123
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
        public UserBean(String account, String session) {
            this.account = account;
            this.session = session;
        }

        public String account;
        public String session;

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
        public DataBean(String device_token_type, String device_token) {
            this.device_token_type = device_token_type;
            this.device_token = device_token;
        }

        public String device_token_type;
        public String device_token;

        public String getDevice_token_type() {
            return device_token_type;
        }

        public void setDevice_token_type(String device_token_type) {
            this.device_token_type = device_token_type;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }
    }
}
