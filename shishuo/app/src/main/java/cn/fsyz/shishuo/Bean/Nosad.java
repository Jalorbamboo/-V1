package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jalor on 2018/4/13.
 */

public class Nosad extends BmobObject {
    private String Content;
    private MyUser User;
    private String Photo;
    private Boolean publicorno;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public MyUser getUser() {
        return User;
    }

    public void setUser(MyUser user) {
        User = user;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Boolean getPublicorno() {
        return publicorno;
    }

    public void setPublicorno(Boolean publicorno) {
        this.publicorno = publicorno;
    }
}
