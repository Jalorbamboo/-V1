package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by JALOR on 2018/2/17.
 */

//用户等级的Bean
public class Power extends BmobObject {

    private MyUser User;
    private String right;


    public MyUser getUser() {
        return User;
    }

    public void setUser(MyUser user) {
        User = user;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
