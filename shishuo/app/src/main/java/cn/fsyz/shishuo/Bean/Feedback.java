package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by JALOR on 2018/2/1.
 */
//反馈的bean
public class Feedback extends BmobObject {
    private String QQ;
    private String problem;
    private String call;
    private MyUser User;

    public String getCall() {
        return call;
    }

    public String getProblem() {
        return problem;
    }

    public String getQQ() {
        return QQ;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public MyUser getUser() {
        return User;
    }

    public void setUser(MyUser user) {
        User = user;
    }
}
