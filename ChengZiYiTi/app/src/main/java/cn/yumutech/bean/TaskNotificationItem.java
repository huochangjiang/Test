package cn.yumutech.bean;

/**
 * Created by Administrator on 2017/1/23.
 */
public class TaskNotificationItem {

    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : {"id":"1","summary":"12321","content":"<p>\r\n\t12321<\/p>\r\n","classify":"督查通报","title":"12321","publish_date":"2016-11-08 12:46:00","original":"21321"}
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

         * classify : 督查通报
         * title : 12321
         * publish_date : 2016-11-08 12:46:00
         * original : 21321
         */

        public String id;
        public String summary;
        public String content;
        public String classify;
        public String title;
        public String publish_date;
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

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
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

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }
}
