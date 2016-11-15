package cn.yumutech.bean;

/**
 * Created by 霍长江 on 2016/11/15.
 */
public class DepartList {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * dept_id : 2
     * dept_name : 安保部
     * dept_parent_id : 1
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
        public String dept_id;
        public String dept_name;
        public String dept_parent_id;

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }

        public String getDept_parent_id() {
            return dept_parent_id;
        }

        public void setDept_parent_id(String dept_parent_id) {
            this.dept_parent_id = dept_parent_id;
        }
    }
}
