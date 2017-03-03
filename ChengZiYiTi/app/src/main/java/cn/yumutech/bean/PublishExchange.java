package cn.yumutech.bean;

/**
 * Created by Administrator on 2017/3/2.
 */
public class PublishExchange {

    /**
     * status : {"message":"发布成功","code":"0"}
     * data : {}
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
         * message : 发布成功
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
    }
}
