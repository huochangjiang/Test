package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/16.
 */
public class PublishTask {
    /**
     * status : {"message":"发布成功","code":"0"}
     * data :
     */

    public StatusBean status;
    public DataBeen data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public DataBeen getData() {
        return data;
    }

    public void setData(DataBeen data) {
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
    public static class DataBeen{

    }
}
