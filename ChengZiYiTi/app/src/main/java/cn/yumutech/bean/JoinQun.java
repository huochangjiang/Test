package cn.yumutech.bean;

/**
 * Created by huo on 2016/11/14.
 */
public class JoinQun {

    /**
     * message : 成功加入群
     * code : 0
     */

    public StatusBean status;
    /**
     * status : {"message":"成功加入群","code":"0"}
     * data : 
     */
    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
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
