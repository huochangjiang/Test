package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/15.
 */
public class AddPingLunBeen {
    public userBeen user;
    public dataBeen data;
    public AddPingLunBeen(userBeen user,dataBeen data){
        this.user=user;
        this.data=data;
    }
    public userBeen getUser(){
        return user;
    }
    public void setUser(userBeen user){
        this.user=user;
    }
    public dataBeen getData(){
        return data;
    }
    public void setData(dataBeen data){
        this.data=data;
    }
    public static class userBeen{
        public String account;
        public String session;
        public userBeen(String account,String session){
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
    public static class dataBeen{
        public String exchange_id;
        public String publish_user_id;
        public String content;
        public String receiver_user_id;
        public dataBeen(String exchange_id,String publish_user_id,String content,String receiver_user_id){
            this.exchange_id=exchange_id;
            this.publish_user_id=publish_user_id;
            this.content=content;
            this.receiver_user_id=receiver_user_id;
        }
        public String getExchange_id(){
            return exchange_id;
        }
        public void setExchange_id(String exchange_id){
            this.exchange_id=exchange_id;
        }
        public String getPublish_user_id(){
            return publish_user_id;
        }
        public void setPublish_user_id(String publish_user_id){
            this.publish_user_id=publish_user_id;
        }
        public String getContent(){
            return content;
        }
        public void setContent(String content){
            this.content=content;
        }
        public String getReceiver_user_id(){
            return receiver_user_id;
        }
        public void setReceiver_user_id(String receiver_user_id){
            this.receiver_user_id=receiver_user_id;
        }
    }
}
