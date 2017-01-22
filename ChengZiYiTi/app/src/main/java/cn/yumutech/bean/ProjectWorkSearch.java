package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public class ProjectWorkSearch {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"progress":"90","summary":"su","id":"2","classify":"市本级","title":"zhaowei","publish_date":"2017-01-16 16:11:26"},{"progress":"15","summary":"1313","id":"3","classify":"市本级","title":"ceshi1","publish_date":"2017-01-16 16:10:48"}]
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
         * progress : 90
         * summary : su
         * id : 2
         * classify : 市本级
         * title : zhaowei
         * publish_date : 2017-01-16 16:11:26
         */

        public String progress;
        public String summary;
        public String id;
        public String classify;
        public String title;
        public String publish_date;

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

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
    }
}
