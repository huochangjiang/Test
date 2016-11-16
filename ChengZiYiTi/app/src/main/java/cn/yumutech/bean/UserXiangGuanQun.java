package cn.yumutech.bean;

/**
 * Created by 霍长江 on 2016/11/16.
 */
public class UserXiangGuanQun {

    /**
     * message : 群创建成功
     * code : 0
     */

    public StatusBean status;
   
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
    }
}
