package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by JALOR on 2018/2/8.
 */
//banner的控制Bean
public class Banner extends BmobObject {
    private BmobFile banner;
    private String web;
    private String title;


    public BmobFile getBanner() {
        return banner;
    }

    public void setBanner(BmobFile banner) {
        this.banner = banner;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
