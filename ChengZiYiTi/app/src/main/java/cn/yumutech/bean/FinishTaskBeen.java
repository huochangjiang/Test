package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class FinishTaskBeen {
    /**
     * user : {"account":"1","session":"1234567890"}
     * data : {"task_id":"1","task_comment":"dsfdsf","photos":[{"photo_suffix":"jpg","photo_content":"322dsdsfdsfd54565"}]}
     */

    private UserBean user;
    private DataBean data;

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
        /**
         * account : 1
         * session : 1234567890
         */

        private String account;
        private String session;

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
        /**
         * task_id : 1
         * task_comment : dsfdsf
         * photos : [{"photo_suffix":"jpg","photo_content":"322dsdsfdsfd54565"}]
         */

        private String task_id;
        private String task_comment;
        private List<PhotosBean> photos;

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
            /**
             * photo_suffix : jpg
             * photo_content : 322dsdsfdsfd54565
             */

            private String photo_suffix;
            private String photo_content;

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
