package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class TaskNotifiList {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"summary":"习近平指出新准则新条例","id":"3","classify":"督查通报","title":"习近平指出新准则新条例的最鲜明特点2","publish_date":"2016-11-07 18:10:56","original":"环球网2"},{"summary":"<p>\r\n\t231231<\/p>\r\n...","id":"2","classify":"督查通报","title":"12312","publish_date":"2016-11-07 17:30:35","original":"23213"}]
     */

    public StatusBean status;
    public List<DataBean> data;

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
         * summary : 习近平指出新准则新条例
         * id : 3
         * classify : 督查通报
         * title : 习近平指出新准则新条例的最鲜明特点2
         * publish_date : 2016-11-07 18:10:56
         * original : 环球网2
         */

        public String summary;
        public String id;
        public String classify;
        public String title;
        public String publish_date;
        public String original;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
