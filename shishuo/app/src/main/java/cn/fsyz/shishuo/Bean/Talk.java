package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by JALOR on 2018/3/1.
 */

//话题讨论
public class Talk extends BmobObject{
    private String title;
    private String content;
    private MyUser User;
    private String Photo;
    private List<String> PhotoArray=new ArrayList<>();
    private TalkTitle TalkTitle;
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

    public cn.fsyz.shishuo.Bean.TalkTitle getTalkTitle() {
        return TalkTitle;
    }

    public void setTalkTitle(cn.fsyz.shishuo.Bean.TalkTitle talkTitle) {
        TalkTitle = talkTitle;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
