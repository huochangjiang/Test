package cn.yumutech.weight;

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
}
