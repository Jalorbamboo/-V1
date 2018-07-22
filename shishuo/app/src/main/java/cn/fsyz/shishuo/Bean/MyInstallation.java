package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

/**
 * Created by JALOR on 2018/2/11.
 */

//安装率以及推送用到的Bean
public class MyInstallation extends BmobInstallation {
    private BmobUser User;

    public BmobUser getUser() {
        return User;
    }

    public void setUser(BmobUser user) {
        User = user;
    }
}
