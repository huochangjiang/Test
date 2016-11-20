package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/12.
 */
public class UserToken {
    public status status;
    public data data;
    public static class status{
        public String message;
        public String code;
    }
    public static class data{
        public String token;

    }
}
