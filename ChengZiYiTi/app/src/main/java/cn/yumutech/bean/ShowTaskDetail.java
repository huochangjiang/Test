package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ShowTaskDetail {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"task_id":"id","task_title":"任务 1","task_content":"任务内容 1","task_end_date":"2016-11-16","task_status":"responsing","task_status_name":"待接受"}]
     */

    public StatusBean status;
    public DataBean data;


    public static class StatusBean {
        /**
         * message : 成功获取数据
         * code : 0
         */

        public String message;
        public String code;


    }

    public static class DataBean {
        /**
         * task_id : id
         * task_title : 任务 1
         * task_content : 任务内容 1
         * task_end_date : 2016-11-16
         * task_status : responsing
         * task_status_name : 待接受
         */

        public String task_id;
        public String task_title;
        public String task_content;
        public String task_end_date;
        public String task_status;
        public String task_status_name;


    }
}
