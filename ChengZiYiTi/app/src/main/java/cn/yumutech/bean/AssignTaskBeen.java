package cn.yumutech.bean;

/**
 * Created by Allen on 2016/11/25.
 */
public class AssignTaskBeen {
    public UserBeen user;
    public DataBeen data;
    public AssignTaskBeen(UserBeen user,DataBeen data){
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
        public String task_id;
        public String assigns;
//        public String director;
//        public String supporter;
        public DataBeen(String task_id,String assigns){
            this.task_id=task_id;
            this.assigns=assigns;
        }
    }
}
