package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class BumenStatusListBeen {
    /**
     * user : {"account":"unity","session":"1234567890"}
     * data : {"offset":"0","row_count":"5"}
     */

    public UserBean user;
    public DataBean data;
    public BumenStatusListBeen(UserBean user,DataBean data){
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
         * offset : 0
         * row_count : 5
         */

        public String offset;
        public String row_count;
        public DataBean(String offset,String row_count){
            this.offset=offset;
            this.row_count=row_count;
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
