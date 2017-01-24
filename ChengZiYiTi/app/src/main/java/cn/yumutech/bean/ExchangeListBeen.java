package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/13.
 */
public class ExchangeListBeen {
    /**
     * account :
     * session :
     */

    public UserBean user;
    /**
     * classify :
     */

    public DataBean data;
    public ExchangeListBeen(UserBean user, DataBean data) {
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
        public String classify;
        public String key;
        public String offset;
        public String row_count;

        public DataBean(String classify,String key,String offset,String row_count){
            this.classify=classify;
            this.key=key;
            this.offset=offset;
            this.row_count=row_count;
        }
        public String getClassify() {
            return classify;
        }

        public void setClassify(String id) {
            this.classify = id;
        }
        public String getOffset(){
            return offset;
        }
        public void setOffset(String offset){
            this.offset=offset;
        }
        public String getRow_count(){
            return row_count;
        }
        public void setRow_count(String row_count){
            this.row_count=row_count;
        }
    }
}
