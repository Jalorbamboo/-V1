package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Jalor on 2018/3/20.
 */

//启动页用到的bean
public class Splash extends BmobObject {
    private BmobFile Haibao;
    private String No;

    public BmobFile getHaibao() {
        return Haibao;
    }

    public void setHaibao(BmobFile haibao) {
        Haibao = haibao;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }
}
