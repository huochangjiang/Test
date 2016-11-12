package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/12.
 */
public class UserTokenBeen {

    public data data;
    public UserTokenBeen(data data){
        this.data=data;
    }
    public data getData(){
        return data;
    }
    public void setData(data data){
        this.data=data;
    }
    public static class data{
        public String account;
        public String session;
        public data(String account,String session){
            this.account=account;
            this.session=session;
        }
        public String getAccount(){
            return account;
        }
        public void setAccount(String account){
            this.account=account;
        }
        public String getSession(){
            return session;
        }
        public void setSession(String session){
            this.session=session;
        }
    }
}
