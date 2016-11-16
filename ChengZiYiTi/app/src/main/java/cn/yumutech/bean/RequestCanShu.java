package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/8.
 */
public class RequestCanShu {

    public RequestCanShu(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }


    public UserBean user;
   

    public DataBean data;

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

        public UserBean(String account, String session) {
            this.account = account;
            this.session = session;
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
        public String offset;
        public String row_count;
        public String id;
        public String userIds;
        public String groupName;
        public String userId;


        public DataBean(String userIds, String groupName) {
            this.userIds = userIds;
            this.groupName = groupName;
        }

        public DataBean(String id) {
            this.id = id;
        }

        public DataBean(String classify, String offset, String row_count) {
            this.classify = classify;
            this.offset = offset;
            this.row_count = row_count;
        }



        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
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
