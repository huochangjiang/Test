package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class BumenStatusItemBeen {
    /**
     * user : {"account":"unity","session":"1234567890"}
     * data : {"id":"1"}
     */

    private UserBean user;
    private DataBean data;
    public BumenStatusItemBeen(UserBean user,DataBean data){
        this.user=user;
        this.data=data;
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
        /**
         * account : unity
         * session : 1234567890
         */

        private String account;
        private String session;
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
        /**
         * id : 1
         */

        private String id;
        public DataBean(String id){
            this.id=id;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
