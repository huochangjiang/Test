package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/16.
 */
public class PublishTaskBeen {
    /**
     * user : {"account":"1","session":"1234567890"}
     * data : {"task_title":"任务 1","task_content":"任务内容 1","task_end_date":"2016-11-16","director":"2","supporter":"3"}
     */

    public UserBean user;
    public DataBean data;
    public PublishTaskBeen(UserBean user,DataBean data){
        this.user=user;
        this.data=data;
    }
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

        public String account;
        public String session;
        public UserBean(String account,String session){
            this.account=account;
            this.session=session;
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
        /**
         * task_title : 任务 1
         * task_content : 任务内容 1
         * task_end_date : 2016-11-16
         * director : 2
         * supporter : 3
         */

        private String task_title;
        private String task_content;
        private String task_end_date;
        private String task_projectwork_id;
//        private String director;
//        private String supporter;
        private String assigns;
        public DataBean(String task_title,String task_content,String task_end_date,String assigns,String task_projectwork_id){
            this.task_title=task_title;
            this.task_content=task_content;
            this.task_end_date=task_end_date;
            this.assigns=assigns;
            this.task_projectwork_id=task_projectwork_id;
//            this.director=director;
//            this.supporter=supporter;
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

//        public String getDirector() {
//            return director;
//        }
//
//        public void setDirector(String director) {
//            this.director = director;
//        }
//
//        public String getSupporter() {
//            return supporter;
//        }
//
//        public void setSupporter(String supporter) {
//            this.supporter = supporter;
//        }
    }
}
