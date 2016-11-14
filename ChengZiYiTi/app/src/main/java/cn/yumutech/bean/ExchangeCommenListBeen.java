package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/14.
 */
public class ExchangeCommenListBeen {
    /**
     * account :
     * session :
     */

    public UserBean user;
    /**
     * exchange_id :文章Id
     * offset:分页查询ID
     * row_count：评论每页显示行数
     */

    public DataBean data;
    public ExchangeCommenListBeen(UserBean user, DataBean data) {
        this.user = user;
        this.data = data;
    }
    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class UserBean {
        public String account;
        public String session;

        public UserBean(String account,String session){
            this.account=account;
            this.session=session;
        }
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }
    }

    public static class DataBean {
        public String exchange_id;
        public String offset;
        public String row_count;

        public DataBean(String exchange_id,String offset,String row_count){
            this.exchange_id=exchange_id;
            this.offset=offset;
            this.row_count=row_count;
        }
        public String getExchange_id() {
            return exchange_id;
        }

        public void setExchange_id(String exchange_id) {
            this.exchange_id = exchange_id;
        }
        public String getOffset(){
            return offset;
        }
        public void setOffset(String offset){
            this.offset=offset;
        }
        public String getRow_count(){
            return row_count;
        }
        public void setRow_count(String row_count){
            this.row_count=row_count;
        }
    }
}
