package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by JALOR on 2018/1/27.
 */

//主页面【诗】的Bean
public class Comment extends BmobObject {
    private String name;
    private MyUser bmobUser;
    private String sth;
    private String photo_url;
    private String bgm;
    private BmobRelation likes;


    private List<String> PhotoUrl=new ArrayList<>();


    public String getSth(){
        return sth;
    }

    public void setSth(String sth) {
        this.sth = sth;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }



    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }



    public MyUser getBmobUser() {
        return bmobUser;
    }

    public void setBmobUser(MyUser bmobUser) {
        this.bmobUser = bmobUser;
    }


    public List<String> getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(List<String> photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getBgm() {
        return bgm;
    }

    public void setBgm(String bgm) {
        this.bgm = bgm;
    }
}
