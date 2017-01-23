package cn.yumutech.bean;

/**
 * Created by Administrator on 2017/1/23.
 */
public class TaskNotificationItemBeen {

    /**
     * user : {"account":"unity","session":"1234567890"}
     * data : {"id":"1"}
     */

    public UserBean user;
    public DataBean data;

    public TaskNotificationItemBeen(UserBean user,DataBean data){
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
        /**
         * id : 1
         */

        public String id;
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
