package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/10.
 */
public class PrijectDetaisl {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * id : 1
     * summary : 123
     * amount : 100
     * content : <p>
     123123</p>

     * classify : 资阳
     * title : 项目工作
     * publish_date : 2016-11-08  14:14:02
     * logo_path : http://localhost:8080/unity/userfiles/images/692afad2-af4a-47f7-b191-b87cacc2a63a.jpg
     * original : 123
     * type : 总体规划
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
        public String amount;
        public String content;
        public String classify;
        public String title;
        public String publish_date;
        public String logo_path;
        public String original;
        public String type;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
