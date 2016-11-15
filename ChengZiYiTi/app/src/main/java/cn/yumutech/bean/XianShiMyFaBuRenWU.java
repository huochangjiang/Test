package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/15.
 */
public class XianShiMyFaBuRenWU {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * task_id : id
     * task_title : 任务1
     * task_content : 任务内容1
     * task_end_date : 2016-11-16
     * task_status : responsing
     * task_status_name : 待接受
     */

    public List<DataBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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
        public String task_id;
        public String task_title;
        public String task_content;
        public String task_end_date;
        public String task_status;
        public String task_status_name;

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_title() {
            return task_title;
        }

        public void setTask_title(String task_title) {
            this.task_title = task_title;
        }

        public String getTask_content() {
            return task_content;
        }

        public void setTask_content(String task_content) {
            this.task_content = task_content;
        }

        public String getTask_end_date() {
            return task_end_date;
        }

        public void setTask_end_date(String task_end_date) {
            this.task_end_date = task_end_date;
        }

        public String getTask_status() {
            return task_status;
        }

        public void setTask_status(String task_status) {
            this.task_status = task_status;
        }

        public String getTask_status_name() {
            return task_status_name;
        }

        public void setTask_status_name(String task_status_name) {
            this.task_status_name = task_status_name;
        }
    }
}
