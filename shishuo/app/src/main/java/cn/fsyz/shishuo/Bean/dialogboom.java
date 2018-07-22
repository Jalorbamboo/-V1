package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jalor on 2018/3/10.
 */

//弹窗的bean
public class dialogboom extends BmobObject {
    private List<String> haibao = new ArrayList<>();


    public List<String> getHaibao() {
        return haibao;
    }

    public void setHaibao(List<String> haibao) {
        this.haibao = haibao;
    }
}
