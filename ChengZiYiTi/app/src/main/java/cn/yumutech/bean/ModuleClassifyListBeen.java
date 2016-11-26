package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/26.
 */
public class ModuleClassifyListBeen {
    public UserBeen user;
    public DataBeen data;
    public ModuleClassifyListBeen(UserBeen user,DataBeen data){
        this.user=user;
        this.data=data;
    }
    public static class UserBeen{
        public String account;
        public String session;
        public UserBeen(String account,String session){
            this.account=account;
            this.session=session;
        }
    }
    public static class DataBeen{
        public String classify;
        public DataBeen(String classify){
            this.classify=classify;
        }
    }
}
