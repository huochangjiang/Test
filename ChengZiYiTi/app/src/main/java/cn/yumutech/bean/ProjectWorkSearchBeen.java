package cn.yumutech.bean;

/**
 * Created by Administrator on 2017/1/19.
 */
public class ProjectWorkSearchBeen {
    /**
     * user : {"account":"unity","session":"1234567890"}
     * data : {"key":"市本级","offset":"0","row_count":"5"}
     */

    public UserBean user;
    public DataBean data;
    public ProjectWorkSearchBeen(UserBean user,DataBean data){
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
         * key : 市本级
         * offset : 0
         * row_count : 5
         */

        public String key;
        public String offset;
        public String row_count;
        public DataBean(String key,String offset,String row_count){
            this.key=key;
            this.offset=offset;
            this.row_count=row_count;
        }
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public String getRow_count() {
            return row_count;
        }

        public void setRow_count(String row_count) {
            this.row_count = row_count;
        }
    }
}
