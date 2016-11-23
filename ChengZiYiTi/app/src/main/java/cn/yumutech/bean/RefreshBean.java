package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/23.
 */
public class RefreshBean {

    /**
     * message : 群创建成功
     * code : 0
     */

    public StatusBean status;
    /**
     * groupId : seryeyrye-23432-ssss
     * groupName : qun1
     * create_user_id : 1
     * create_user_name : unity
     * create_user_mobile : 12345678901
     * create_user_logo_path : http://sdfdsfsdf/ssss
     * create_date : 2016-11-15 12:04:33
     * users : [{"userId":"1","user_name":"unity","user_mobile":"12345678901","user_logo_path":"http://sdfdsfsdf/ssss"}]
     */

    public DataBean data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class StatusBean {
        public String message;
        public String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class DataBean {
        public String groupId;
        public String groupName;
        public String create_user_id;
        public String create_user_name;
        public String create_user_mobile;
        public String create_user_logo_path;
        public String create_date;
        /**
         * userId : 1
         * user_name : unity
         * user_mobile : 12345678901
         * user_logo_path : http://sdfdsfsdf/ssss
         */

        public List<UsersBean> users;

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

        public String getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(String create_user_id) {
            this.create_user_id = create_user_id;
        }

        public String getCreate_user_name() {
            return create_user_name;
        }

        public void setCreate_user_name(String create_user_name) {
            this.create_user_name = create_user_name;
        }

        public String getCreate_user_mobile() {
            return create_user_mobile;
        }

        public void setCreate_user_mobile(String create_user_mobile) {
            this.create_user_mobile = create_user_mobile;
        }

        public String getCreate_user_logo_path() {
            return create_user_logo_path;
        }

        public void setCreate_user_logo_path(String create_user_logo_path) {
            this.create_user_logo_path = create_user_logo_path;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean {
            public String userId;
            public String user_name;
            public String user_mobile;
            public String user_logo_path;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_mobile() {
                return user_mobile;
            }

            public void setUser_mobile(String user_mobile) {
                this.user_mobile = user_mobile;
            }

            public String getUser_logo_path() {
                return user_logo_path;
            }

            public void setUser_logo_path(String user_logo_path) {
                this.user_logo_path = user_logo_path;
            }
        }
    }
}
