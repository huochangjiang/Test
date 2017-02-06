package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ShowTaskDetail {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * task_publish_user_name : 赵伟
     * task_publish_user_id : 2
     * task_publish_date : 2016-12-08 10:22:11
     * assign : [{},{"assignee_user_mobile":"","assigner_user_mobile":"18010640123","assignee_user_name":"","assign_date":"2016-12-08 10:22:11","assigner_user_name":"赵伟","assigner_user_id":"2","assignee_user_id":"23","assignee_user_type":"supporter"}]
     * task_content : 爸爸
     * task_end_date : 2016-12-08 00:00:00
     * task_finish_user_id : 7
     * task_id : 118
     * task_status : finished
     * task_comment : {"photos":[{"photo_type":"jpg","photo_path":"http://182.254.167.232:20080/unity/userfiles/images/task/photo/6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg","photo_thumbnail_path":"http://182.254.167.232:20080/unity/userfiles/images/task/thumbnail/thumbnail_6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg"}],"files":[{"file_path":"http://182.254.167.232:20080/unity/userfiles/file/task/tbs_download_lock_file3690620170122144104.txt","file_name":"tbs_download_lock_file3690620170122144104.txt","file_type":"txt"}],"taskcomment_user_id":"7","taskcomment_user_name":"霍长江","taskcomment_date":"","taskcomment_content":"推荐"}
     * task_status_name : 已完成
     * task_finish_date : 2017-01-22 14:41:02
     * task_accept_date : 2016-12-21 10:10:37
     * task_title : lll
     * task_finish_user_name : 霍长江
     * task_accept_user_name : 霍长江
     * task_accept_user_id : 7
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
        public String task_publish_user_name;
        public String task_publish_user_id;
        public String task_publish_date;
        public String task_content;
        public String task_end_date;
        public String task_finish_user_id;
        public String task_id;
        public String task_status;
        /**
         * photos : [{"photo_type":"jpg","photo_path":"http://182.254.167.232:20080/unity/userfiles/images/task/photo/6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg","photo_thumbnail_path":"http://182.254.167.232:20080/unity/userfiles/images/task/thumbnail/thumbnail_6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg"}]
         * files : [{"file_path":"http://182.254.167.232:20080/unity/userfiles/file/task/tbs_download_lock_file3690620170122144104.txt","file_name":"tbs_download_lock_file3690620170122144104.txt","file_type":"txt"}]
         * taskcomment_user_id : 7
         * taskcomment_user_name : 霍长江
         * taskcomment_date : 
         * taskcomment_content : 推荐
         */

        public TaskCommentBean task_comment;
        public String task_status_name;
        public String task_finish_date;
        public String task_accept_date;
        public String task_title;
        public String task_finish_user_name;
        public String task_accept_user_name;
        public String task_accept_user_id;
        public List<AssignBean> assign;

        public String getTask_publish_user_name() {
            return task_publish_user_name;
        }

        public void setTask_publish_user_name(String task_publish_user_name) {
            this.task_publish_user_name = task_publish_user_name;
        }

        public String getTask_publish_user_id() {
            return task_publish_user_id;
        }

        public void setTask_publish_user_id(String task_publish_user_id) {
            this.task_publish_user_id = task_publish_user_id;
        }

        public String getTask_publish_date() {
            return task_publish_date;
        }

        public void setTask_publish_date(String task_publish_date) {
            this.task_publish_date = task_publish_date;
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

        public String getTask_finish_user_id() {
            return task_finish_user_id;
        }

        public void setTask_finish_user_id(String task_finish_user_id) {
            this.task_finish_user_id = task_finish_user_id;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_status() {
            return task_status;
        }

        public void setTask_status(String task_status) {
            this.task_status = task_status;
        }

        public TaskCommentBean getTask_comment() {
            return task_comment;
        }

        public void setTask_comment(TaskCommentBean task_comment) {
            this.task_comment = task_comment;
        }

        public String getTask_status_name() {
            return task_status_name;
        }

        public void setTask_status_name(String task_status_name) {
            this.task_status_name = task_status_name;
        }

        public String getTask_finish_date() {
            return task_finish_date;
        }

        public void setTask_finish_date(String task_finish_date) {
            this.task_finish_date = task_finish_date;
        }

        public String getTask_accept_date() {
            return task_accept_date;
        }

        public void setTask_accept_date(String task_accept_date) {
            this.task_accept_date = task_accept_date;
        }

        public String getTask_title() {
            return task_title;
        }

        public void setTask_title(String task_title) {
            this.task_title = task_title;
        }

        public String getTask_finish_user_name() {
            return task_finish_user_name;
        }

        public void setTask_finish_user_name(String task_finish_user_name) {
            this.task_finish_user_name = task_finish_user_name;
        }

        public String getTask_accept_user_name() {
            return task_accept_user_name;
        }

        public void setTask_accept_user_name(String task_accept_user_name) {
            this.task_accept_user_name = task_accept_user_name;
        }

        public String getTask_accept_user_id() {
            return task_accept_user_id;
        }

        public void setTask_accept_user_id(String task_accept_user_id) {
            this.task_accept_user_id = task_accept_user_id;
        }

        public List<AssignBean> getAssign() {
            return assign;
        }

        public void setAssign(List<AssignBean> assign) {
            this.assign = assign;
        }

        public static class TaskCommentBean {
            public String taskcomment_user_id;
            public String taskcomment_user_name;
            public String taskcomment_date;
            public String taskcomment_content;
            /**
             * photo_type : jpg
             * photo_path : http://182.254.167.232:20080/unity/userfiles/images/task/photo/6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg
             * photo_thumbnail_path : http://182.254.167.232:20080/unity/userfiles/images/task/thumbnail/thumbnail_6a508b8b-af82-49b6-be59-3ad9e75242ce.jpg
             */

            public List<PhotosBean> photos;
            /**
             * file_path : http://182.254.167.232:20080/unity/userfiles/file/task/tbs_download_lock_file3690620170122144104.txt
             * file_name : tbs_download_lock_file3690620170122144104.txt
             * file_type : txt
             */

            public List<FilesBean> files;

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

            public String getTaskcomment_content() {
                return taskcomment_content;
            }

            public void setTaskcomment_content(String taskcomment_content) {
                this.taskcomment_content = taskcomment_content;
            }

            public List<PhotosBean> getPhotos() {
                return photos;
            }

            public void setPhotos(List<PhotosBean> photos) {
                this.photos = photos;
            }

            public List<FilesBean> getFiles() {
                return files;
            }

            public void setFiles(List<FilesBean> files) {
                this.files = files;
            }

            public static class PhotosBean {
                public String photo_type;
                public String photo_path;
                public String photo_thumbnail_path;

                public String getPhoto_type() {
                    return photo_type;
                }

                public void setPhoto_type(String photo_type) {
                    this.photo_type = photo_type;
                }

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

            public static class FilesBean {
                public String file_path;
                public String file_name;
                public String file_type;

                public String getFile_path() {
                    return file_path;
                }

                public void setFile_path(String file_path) {
                    this.file_path = file_path;
                }

                public String getFile_name() {
                    return file_name;
                }

                public void setFile_name(String file_name) {
                    this.file_name = file_name;
                }

                public String getFile_type() {
                    return file_type;
                }

                public void setFile_type(String file_type) {
                    this.file_type = file_type;
                }
            }
        }

        public static class AssignBean {
            public String assignee_user_mobile;
            public String assigner_user_mobile;
            public String assignee_user_name;
            public String assign_date;
            public String assigner_user_name;
            public String assigner_user_id;
            public String assignee_user_id;
            public String assignee_user_type;

        }
    }
}
