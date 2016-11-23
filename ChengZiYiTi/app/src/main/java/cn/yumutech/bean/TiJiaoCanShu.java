package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/23.
 */
public class TiJiaoCanShu {

    public TiJiaoCanShu(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }

    /**
     * account : 1
     * session : 1234567890
     */

    public UserBean user;
    /**
     * task_id : 1
     * task_comment : dsfdsf
     * photos : [{"photo_suffix":"jpg","photo_content":"322dsdsfdsfd54565"}]
     */

    public DataBean data;

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
        public String account;
        public String session;

        public UserBean(String account, String session) {
            this.account = account;
            this.session = session;
        }

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
        public String task_id;
        public String task_comment;
        public DataBean(){

        }
        public DataBean(String task_id, String task_comment, List<PhotosBean> photos) {
            this.task_id = task_id;
            this.task_comment = task_comment;
            this.photos = photos;
        }

        /**
         * photo_suffix : jpg
         * photo_content : 322dsdsfdsfd54565
         */

        public List<PhotosBean> photos;

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_comment() {
            return task_comment;
        }

        public void setTask_comment(String task_comment) {
            this.task_comment = task_comment;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class PhotosBean {
            public String photo_suffix;
            public String photo_content;

            public PhotosBean(String photo_suffix, String photo_content) {
                this.photo_suffix = photo_suffix;
                this.photo_content = photo_content;
            }

            public String getPhoto_suffix() {
                return photo_suffix;
            }

            public void setPhoto_suffix(String photo_suffix) {
                this.photo_suffix = photo_suffix;
            }

            public String getPhoto_content() {
                return photo_content;
            }

            public void setPhoto_content(String photo_content) {
                this.photo_content = photo_content;
            }
        }
    }
}
