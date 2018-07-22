package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by JALOR on 2018/2/7.
 */


//拓展BmobUser的Bean
public class MyUser extends BmobUser {
      private String School;
      private String Grade;
      private String Classes;
      private String Realname;
      private BmobFile userpic;
      private String userpic_url;
      private String UDID;
      private String power;

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }


    public String getClasses() {
        return Classes;
    }

    public void setClasses(String classes) {
        Classes = classes;
    }

    public String getRealname() {
        return Realname;
    }

    public void setRealname(String realname) {
        Realname = realname;
    }

    public BmobFile getUserpic() {
        return userpic;
    }

    public void setUserpic(BmobFile userpic) {
        this.userpic = userpic;
    }

    public String getUserpic_url() {
        return userpic_url;
    }

    public void setUserpic_url(String userpic_url) {
        this.userpic_url = userpic_url;
    }



    public String getUDID() {
        return UDID;
    }

    public void setUDID(String UDID) {
        this.UDID = UDID;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
