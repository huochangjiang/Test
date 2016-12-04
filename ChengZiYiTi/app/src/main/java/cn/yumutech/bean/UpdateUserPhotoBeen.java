package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/30.
 */
public class UpdateUserPhotoBeen {

    /**
     * user : {"account":"","session":""}
     * data : {"photo_suffix":"jpg","photo_content":"234eeerere8973hh9eehj9w3hj923h4"}
     */

    private UserBean user;
    private DataBean data;

    public UpdateUserPhotoBeen(UserBean user,DataBean data){
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
         * account :
         * session :
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
         * photo_suffix : jpg
         * photo_content : 234eeerere8973hh9eehj9w3hj923h4
         */

        private String photo_suffix;
        private String photo_content;

        public DataBean(String photo_suffix,String photo_content){
            this.photo_suffix=photo_suffix;
            this.photo_content=photo_content;
        }
        public String getPhoto_suffix() {
            return photo_suffix;
        }

        public void setPhoto_suffix(String photo_suffix) {
            this.photo_suffix = photo_suffix;
        }

        public String getPhoto_content() {
            return photo_content;
        }

        public void setPhoto_content(String photo_content) {
            this.photo_content = photo_content;
        }
    }
}
