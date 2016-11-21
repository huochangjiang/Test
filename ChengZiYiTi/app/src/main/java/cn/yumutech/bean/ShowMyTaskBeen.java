package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/21.
 */
public class ShowMyTaskBeen {
    /**
     * user : {"account":"1","session":"1234567890"}
     * data : {"offset":"0","row_count":"5"}
     */

    private UserBean user;
    private DataBean data;

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

        private String offset;
        private String row_count;

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
