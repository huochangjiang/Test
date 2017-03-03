package cn.yumutech.bean;

/**
 * Created by Administrator on 2017/3/2.
 */
public class PublishExchangeBeen {

    /**
     * user : {"account":"1","session":"1234567890"}
     * data : {"summary":"123213","content":"<p>\r\n\t123213<\/p>\r\n","title":"12312"}
     */

    public UserBean user;
    public DataBean data;

    public PublishExchangeBeen(UserBean user,DataBean data){
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
         * summary : 123213
         * content : <p>
         123213</p>

         * title : 12312
         */

        public String summary;
        public String content;
        public String title;
        public DataBean(String summary,String content,String title){
            this.summary=summary;
            this.content=content;
            this.title=title;
        }
        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
