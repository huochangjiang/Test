package cn.yumutech.weight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.Poeple;
import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.bean.UserAboutPerson;

/**
 * Created by 霍长江 on 2016/6/21.
 */
public class SaveData {
    public static SaveData instance;

    private SaveData() {
    }

    public static SaveData getInstance() {
        if (instance == null) {
            instance = new SaveData();
        }
        return instance;
    }
    /**
     * 保存评论回复的被回复人ID
     */
    public String receiver_User_ID;
    /**
     * 保存联系人的部门id
     */
    public String Dept_Id;
    //保存所有部门
    public List<GroupClass> taskToChildGroups=new ArrayList<>();
    //保存自己id一下的部门
    public List<GroupClass> underTaskToChildGroups=new ArrayList<>();
    //創建聯係人選擇的id
    public String createDetpt_id;
    //添加群成員的id
    public String qunMenmberId;
    /**
     * 保存被可以被指派的所有员工信息
     */
    public List<UserAboutPerson.DataBean> allEmployees=new ArrayList<>();
    //保存指派员工的信息
    public String taskZhiPaiToWho;
    public Map<Integer, UserAboutPerson.DataBean> zhiPaiBeen;
    //保存取出的主办人和协办人信息
    public List<Poeple> twoPeople=new ArrayList<>();
//    public List<List<ChindClass>> chindDatas;
    //查看完成任务时的信息
    public ShowTaskDetail showTaskComplete;
    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort(String data) {
//        String dtStart = "2010-10-15T09:27:37Z";
        String datetime = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = formatter.parse(data);
            System.out.println(date1);

            datetime = formatter.format(date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        String dateString = formatter.format(data);
        return datetime;
    }

    //点击部门是否有权限。、指派那儿
    public boolean isPermissions=true;
    //联系人那儿点击部门是否有权限
    public boolean isContactPermissions=true;
    /**
     * 指派人哪儿的数据
     */
    //保存跳转到指派页面时的type，判断其是从哪个页面跳转过来的
    public String type="0";
    //保存从详情页面跳转过来时的task_id
    public String task_id;
    //保存树形的数据顺序
    public  List<GroupClass> shuXingData=new ArrayList<>();
    //保存是不是详情页接受键点击后返回的
    public Boolean isJieshou=false;
}
