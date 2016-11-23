package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/23.
 */
public class ShuaXinQunXinXi {

    /**
     * account : 1
     * session : 1234567890
     */

    private UserBean user;

    public ShuaXinQunXinXi(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }

    /**
     * groupId : 12321312
     * groupName : 群1
     */

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
        public UserBean(String account, String session) {
            this.account = account;
            this.session = session;
        }

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
        public DataBean(String groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
        }

        private String groupId;
        private String groupName;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}
