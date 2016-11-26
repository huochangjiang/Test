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

    public StatusBean status;
    public List<DataBean> data;



    public static class StatusBean {
        /**
         * message : 成功获取数据
         * code : 0
         */

        private String message;
        private String code;


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

        public String task_id;
        public String task_title;
        public String task_content;
        public String task_end_date;
        public String task_status;
        public String task_status_name;
        public String task_accept_user_id;
        public String task_accept_user_name;
        public String task_accept_date;
        public String task_finish_user_id;
        public String task_finish_user_name;
        public String task_finish_date;
        public String task_publish_user_id;
        public String task_publish_user_name;
        public String task_publish_date;

    }
}
