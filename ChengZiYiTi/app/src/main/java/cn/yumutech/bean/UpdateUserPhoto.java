package cn.yumutech.bean;

/**
 * Created by Administrator on 2016/11/30.
 */
public class UpdateUserPhoto {

    /**
     * status : {"message":"更新成功","code":"0"}
     * data : {"id":"2","birthday":"2016-11-07 00:00:00","nickname":"unity","address":"成都市","token":"dTWbSOMY+79UOC095NrcA+qwM2YMZhjwj73CHIoeLPmofwulFRHovsrypRI3mKElHihlXFsGwcMosnljA+j45A==","logo_path":" http://localhost:8080/unity /userfiles/images/c7cdf915-52ad-4d94-94cd-215cba8a822b.jpg","gender":"","mobile":"12345678901","dept_id":"2","dept_name":"安保部"}
     */

    public StatusBean status;
    public DataBean data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class StatusBean {
        /**
         * message : 更新成功
         * code : 0
         */

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
        /**
         * id : 2
         * birthday : 2016-11-07 00:00:00
         * nickname : unity
         * address : 成都市
         * token : dTWbSOMY+79UOC095NrcA+qwM2YMZhjwj73CHIoeLPmofwulFRHovsrypRI3mKElHihlXFsGwcMosnljA+j45A==
         * logo_path :  http://localhost:8080/unity /userfiles/images/c7cdf915-52ad-4d94-94cd-215cba8a822b.jpg
         * gender :
         * mobile : 12345678901
         * dept_id : 2
         * dept_name : 安保部
         */

        public String id;
        public String birthday;
        public String nickname;
        public String address;
        public String token;
        public String logo_path;
        public String gender;
        public String mobile;
        public String dept_id;
        public String dept_name;

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }
    }
}
