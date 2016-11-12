package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/12.
 */
public class UserLoginBeen {
    /**
     * account :
     * session :
     */

    public UserBean user;
    /**
     * mobile : 12345678901
     */

    public DataBean data;
    public UserLoginBeen(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }
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

        public UserBean(String account,String session){
            this.account=account;
            this.session=session;
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
        public String mobile;
        public String validcode;

        public DataBean(String mobile,String validcode){
            this.mobile=mobile;
            this.validcode=validcode;
        }
        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getValidcode() {
            return validcode;
        }

        public void setValidcode(String validcode) {
            this.validcode = validcode;
        }
    }
}
