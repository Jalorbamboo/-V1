package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Jalor on 2018/5/19.
 */

public class Bsender extends BmobObject {
    private String title;
    private String content;
    private MyUser User;
    private String Photo;
    private List<String> PhotoArray=new ArrayList<>();
    private BsenderTitle bsenderTitle;
    private BmobRelation likes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getPhotoArray() {
        return PhotoArray;
    }

    public void setPhotoArray(List<String> photoArray) {
        PhotoArray = photoArray;
    }

    public BsenderTitle getBsenderTitle() {
        return bsenderTitle;
    }

    public void setBsenderTitle(BsenderTitle bsenderTitle) {
        this.bsenderTitle = bsenderTitle;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
