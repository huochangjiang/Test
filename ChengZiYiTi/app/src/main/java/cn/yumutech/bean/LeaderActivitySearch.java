package cn.yumutech.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public class LeaderActivitySearch {
    /**
     * status : {"message":"成功获取数据","code":"0"}
     * data : [{"summary":"习近平指出新准则新条例","id":"3","classify":"省级","title":"习近平指出新准则新条例的最鲜明特点2","publish_date":"2016-11-07 18:10:56","logo_path":"http://localhost:8080/unity/userfiles/images/d4365dc8-66ad-42b2-a00c-33b49eac8562.jpg","original":"环球网2"},{"summary":"<p>\r\n\t231231<\/p>\r\n...","id":"2","classify":"省级","title":"12312","publish_date":"2016-11-07 17:30:35","logo_path":"http://localhost:8080/unity/userfiles/images/e97ff60e-808c-4801-a8cc-08635ce7599c.jpg","original":"23213"}]
     */

    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * message : 成功获取数据
         * code : 0
         */

        public String message;
        public String code;


    }

    public static class DataBean {
        /**
         * summary : 习近平指出新准则新条例
         * id : 3
         * classify : 省级
         * title : 习近平指出新准则新条例的最鲜明特点2
         * publish_date : 2016-11-07 18:10:56
         * logo_path : http://localhost:8080/unity/userfiles/images/d4365dc8-66ad-42b2-a00c-33b49eac8562.jpg
         * original : 环球网2
         */

        public String summary;
        public String id;
        public String classify;
        public String title;
        public String publish_date;
        public String logo_path;
        public String original;
    }
}
