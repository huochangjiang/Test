package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/13.
 */
public class ExchangeItemBeen {
    /**
     * account :
     * session :
     */

    public UserBean user;
    /**
     * mobile : 12345678901
     */

    public DataBean data;
    public ExchangeItemBeen(UserBean user, DataBean data) {
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
        public String id;

        public DataBean(String id){
            this.id=id;
        }
        public String getMobile() {
            return id;
        }

        public void setMobile(String id) {
            this.id = id;
        }
    }
}
