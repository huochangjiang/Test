package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/21.
 */
public class AcceptTaskBeen {
    /**
     * user : {"account":"1","session":"1234567890"}
     * data : {"task_id":"1"}
     */

    public UserBean user;
    public DataBean data;
    public AcceptTaskBeen(UserBean user,DataBean data){
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
         * account : 1
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
         * task_id : 1
         */

        public String task_id;
        public DataBean(String task_id){
            this.task_id=task_id;
        }
        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }
    }
}
