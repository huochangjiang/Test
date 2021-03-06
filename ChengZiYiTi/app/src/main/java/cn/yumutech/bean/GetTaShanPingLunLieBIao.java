package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/14.
 */
public class GetTaShanPingLunLieBIao {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * content : 提高自身修养很重要，不然跟不上党的节奏，会落伍的
     * comment_id : 4
     * receiver_user_name : unity
     * publish_user_id : 1
     * publish_date : 2016-11-14 14:39:01
     * receiver_user_id : 2
     * publish_user_name : admin
     * exchange_id : 6
     */

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
        public String content;
        public String comment_id;
        public String receiver_user_name;
        public String publish_user_id;
        public String publish_date;
        public String receiver_user_id;
        public String publish_user_name;
        public String exchange_id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getReceiver_user_name() {
            return receiver_user_name;
        }

        public void setReceiver_user_name(String receiver_user_name) {
            this.receiver_user_name = receiver_user_name;
        }

        public String getPublish_user_id() {
            return publish_user_id;
        }

        public void setPublish_user_id(String publish_user_id) {
            this.publish_user_id = publish_user_id;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getReceiver_user_id() {
            return receiver_user_id;
        }

        public void setReceiver_user_id(String receiver_user_id) {
            this.receiver_user_id = receiver_user_id;
        }

        public String getPublish_user_name() {
            return publish_user_name;
        }

        public void setPublish_user_name(String publish_user_name) {
            this.publish_user_name = publish_user_name;
        }

        public String getExchange_id() {
            return exchange_id;
        }

        public void setExchange_id(String exchange_id) {
            this.exchange_id = exchange_id;
        }
    }
}
