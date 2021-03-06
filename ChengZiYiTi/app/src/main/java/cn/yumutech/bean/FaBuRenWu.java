package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/15.
 */
public class FaBuRenWu {

    /**
     * message : 发布成功
     * code : 0
     */

    public StatusBean status;
    /**
     * status : {"message":"发布成功","code":"0"}
     * data : 
     */

    public String data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
