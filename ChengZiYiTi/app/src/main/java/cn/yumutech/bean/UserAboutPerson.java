package cn.yumutech.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/11/17.
 */
public class UserAboutPerson {

    /**
     * message : 成功获取数据
     * code : 0
     */

    public StatusBean status;
    /**
     * id : 3
     * birthday : 2016-11-07 00:00:00
     * dept_id : 8
     * token : BA30MmLDRs2dkX6H0DJaIVGbghjKETbpRsSl+S9JAABjX3dwulUDiBHqnd4qY/k2JVDJasMpjUI=
     * address : cd
     * nickname : 霍长江
     * logo_path : http://182.254.167.232:20080/unity/userfiles/images/portrait.jpg
     * gender : 1
     * dept_name : 综合部
     * mobile : 18215595271
     */

    public List<DataBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class StatusBean {
        public String message;
        public String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class DataBean {
        public static final int TYPE_CHECKED = 1;
        public static final int TYPE_NOCHECKED = 0;
        public int type;
        public boolean isCance;
        public String id;
        public String birthday;
        public String dept_id;
        public String token;
        public String address;
        public String nickname;
        public String logo_path;
        public String gender;
        public String dept_name;
        public String mobile;

        public boolean isCance() {
            return isCance;
        }

        public void setCance(boolean cance) {
            isCance = cance;
        }

        public int getType(){
            return type;
        }
        public void setType(int type){
            this.type=type;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
