package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by JALOR on 2018/1/30.
 */

//真心话的Bean

public class WhiteComment extends BmobObject {
    private String name;
    private MyUser bmobUser;
    private String sth;
    //封面图片
    private String image_url;
    private List<String> PhotoUrl=new ArrayList<>();
    private BmobRelation likes;


    public String getSth(){
        return sth;
    }

    public void setSth(String sth) {
        this.sth = sth;
    }

    public MyUser getBmobUser() {
        return bmobUser;
    }

    public void setBmobUser(MyUser bmobUser) {
        this.bmobUser = bmobUser;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }




    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<String> getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(List<String> photoUrl) {
        PhotoUrl = photoUrl;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}