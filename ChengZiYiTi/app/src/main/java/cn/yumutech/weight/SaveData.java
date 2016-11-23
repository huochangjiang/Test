package cn.yumutech.weight;

import java.util.List;
import java.util.Map;

import cn.yumutech.bean.GroupClass;
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
    public List<GroupClass> taskToChildGroups;
    //保存指派员工的信息
    public String taskZhiPaiToWho;
    public Map<Integer, UserAboutPerson.DataBean> zhiPaiBeen;
//    public List<List<ChindClass>> chindDatas;
}
