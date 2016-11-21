package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class FinishTask {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"task_id":"id","task_title":"任务 1","task_content":"任务内容 1","task_end_date":"2016-11-16","task_status":"responsing","task_status_name":"待接受","task_accept_user_id":"1","task_accept_user_name":"zhangsan","task_accept_date":"2016-11-16 11:12:13","task_finish_user_id":"2","task_finish_user_name":"lisi","task_finish_date":"2016-11-16 11:12:18","task_comment":{"taskcomment_content":"sdfdsfsdf","taskcomment_user_id":"1","taskcomment_user_name":"zhangsan","taskcomment_date":"2016-11-16 11:12:13","photos":[{"photo_path":"http://sdfdsfdsf","photo_thumbnail_path":"http://sdfdsfs"}]}}]
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
         * task_comment : {"taskcomment_content":"sdfdsfsdf","taskcomment_user_id":"1","taskcomment_user_name":"zhangsan","taskcomment_date":"2016-11-16 11:12:13","photos":[{"photo_path":"http://sdfdsfdsf","photo_thumbnail_path":"http://sdfdsfs"}]}
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
        private TaskCommentBean task_comment;

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

        public TaskCommentBean getTask_comment() {
            return task_comment;
        }

        public void setTask_comment(TaskCommentBean task_comment) {
            this.task_comment = task_comment;
        }

        public static class TaskCommentBean {
            /**
             * taskcomment_content : sdfdsfsdf
             * taskcomment_user_id : 1
             * taskcomment_user_name : zhangsan
             * taskcomment_date : 2016-11-16 11:12:13
             * photos : [{"photo_path":"http://sdfdsfdsf","photo_thumbnail_path":"http://sdfdsfs"}]
             */

            private String taskcomment_content;
            private String taskcomment_user_id;
            private String taskcomment_user_name;
            private String taskcomment_date;
            private List<PhotosBean> photos;

            public String getTaskcomment_content() {
                return taskcomment_content;
            }

            public void setTaskcomment_content(String taskcomment_content) {
                this.taskcomment_content = taskcomment_content;
            }

            public String getTaskcomment_user_id() {
                return taskcomment_user_id;
            }

            public void setTaskcomment_user_id(String taskcomment_user_id) {
                this.taskcomment_user_id = taskcomment_user_id;
            }

            public String getTaskcomment_user_name() {
                return taskcomment_user_name;
            }

            public void setTaskcomment_user_name(String taskcomment_user_name) {
                this.taskcomment_user_name = taskcomment_user_name;
            }

            public String getTaskcomment_date() {
                return taskcomment_date;
            }

            public void setTaskcomment_date(String taskcomment_date) {
                this.taskcomment_date = taskcomment_date;
            }

            public List<PhotosBean> getPhotos() {
                return photos;
            }

            public void setPhotos(List<PhotosBean> photos) {
                this.photos = photos;
            }

            public static class PhotosBean {
                /**
                 * photo_path : http://sdfdsfdsf
                 * photo_thumbnail_path : http://sdfdsfs
                 */

                private String photo_path;
                private String photo_thumbnail_path;

                public String getPhoto_path() {
                    return photo_path;
                }

                public void setPhoto_path(String photo_path) {
                    this.photo_path = photo_path;
                }

                public String getPhoto_thumbnail_path() {
                    return photo_thumbnail_path;
                }

                public void setPhoto_thumbnail_path(String photo_thumbnail_path) {
                    this.photo_thumbnail_path = photo_thumbnail_path;
                }
            }
        }
    }
}
