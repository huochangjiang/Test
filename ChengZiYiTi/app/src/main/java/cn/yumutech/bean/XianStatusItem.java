package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class XianStatusItem {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : {"id":"1","summary":"12321","content":"<p>\r\n\t12321<\/p>\r\n","title":"12321","publish_date":"2016-11-08 12:46:00","logo_path":"http://localhost:8080/unity/userfiles/images/3f5b6cb9-fa04-4455-90de-de69ceb0fdc4.jpg","original":"21321"}
     */

    public StatusBean status;
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
        /**
         * message : 成功获取数据
         * code : 0
         */

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
        /**
         * id : 1
         * summary : 12321
         * content : <p>
         12321</p>

         * title : 12321
         * publish_date : 2016-11-08 12:46:00
         * logo_path : http://localhost:8080/unity/userfiles/images/3f5b6cb9-fa04-4455-90de-de69ceb0fdc4.jpg
         * original : 21321
         */

        public String id;
        public String summary;
        public String content;
        public String title;
        public String publish_date;
        public String logo_path;
        public String original;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }
}
