package cn.yumutech.bean;

import java.util.List;

/**
 * Created by huo on 2016/11/8.
 */
public class LeaderActivitys {


    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        public String message;
        public String code;
    }
    public static class DataBean {
        public String summary;
        public String id;
        public String classify;
        public String title;
        public String publish_date;
        public String logo_path;
        public String original;
    }
}
