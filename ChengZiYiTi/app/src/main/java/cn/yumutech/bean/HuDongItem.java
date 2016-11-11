package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/11.
 */
public class HuDongItem {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * id : 1
     * summary : 123213
     * comment_count : 0
     * content : <p>
     123213</p>

     * classify : 国内
     * title : 12312
     * browser_count : 0
     * publish_date : 2016-11-09  15:37:44
     * logo_path : http://182.254.167.232:20080/unity/userfiles/images/d242df32-c44f-4593-aa8c-bed49123bd87.jpg
     * original : 123213123
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
        public String id;
        public String summary;
        public String comment_count;
        public String content;
        public String classify;
        public String title;
        public String browser_count;
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

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
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

        public String getBrowser_count() {
            return browser_count;
        }

        public void setBrowser_count(String browser_count) {
            this.browser_count = browser_count;
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
