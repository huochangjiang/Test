package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class ShowMyTask {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"task_id":"id","task_title":"任务 1","task_content":"任务内容 1","task_end_date":"2016-11-16","task_status":"responsing","task_status_name":"待接受","task_accept_user_id":"1","task_accept_user_name":"zhangsan","task_accept_date":"2016-11-16 11:12:13","task_finish_user_id":"2","task_finish_user_name":"lisi","task_finish_date":"2016-11-16 11:12:18"}]
     */

    private StatusBean status;
    private List<DataBean> data;

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
        /**
         * message : 成功获取数据
         * code : 0
         */

        private String message;
        private String code;

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
        /**
         * task_id : id
         * task_title : 任务 1
         * task_content : 任务内容 1
         * task_end_date : 2016-11-16
         * task_status : responsing
         * task_status_name : 待接受
         * task_accept_user_id : 1
         * task_accept_user_name : zhangsan
         * task_accept_date : 2016-11-16 11:12:13
         * task_finish_user_id : 2
         * task_finish_user_name : lisi
         * task_finish_date : 2016-11-16 11:12:18
         */

        private String task_id;
        private String task_title;
        private String task_content;
        private String task_end_date;
        private String task_status;
        private String task_status_name;
        private String task_accept_user_id;
        private String task_accept_user_name;
        private String task_accept_date;
        private String task_finish_user_id;
        private String task_finish_user_name;
        private String task_finish_date;

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

        public String getTask_accept_user_id() {
            return task_accept_user_id;
        }

        public void setTask_accept_user_id(String task_accept_user_id) {
            this.task_accept_user_id = task_accept_user_id;
        }

        public String getTask_accept_user_name() {
            return task_accept_user_name;
        }

        public void setTask_accept_user_name(String task_accept_user_name) {
            this.task_accept_user_name = task_accept_user_name;
        }

        public String getTask_accept_date() {
            return task_accept_date;
        }

        public void setTask_accept_date(String task_accept_date) {
            this.task_accept_date = task_accept_date;
        }

        public String getTask_finish_user_id() {
            return task_finish_user_id;
        }

        public void setTask_finish_user_id(String task_finish_user_id) {
            this.task_finish_user_id = task_finish_user_id;
        }

        public String getTask_finish_user_name() {
            return task_finish_user_name;
        }

        public void setTask_finish_user_name(String task_finish_user_name) {
            this.task_finish_user_name = task_finish_user_name;
        }

        public String getTask_finish_date() {
            return task_finish_date;
        }

        public void setTask_finish_date(String task_finish_date) {
            this.task_finish_date = task_finish_date;
        }
    }
}
