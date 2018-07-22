package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by JALOR on 2018/2/19.
 */
//活动的bean
public class Huodong extends BmobObject {
    private String title;
    private String content;
    private MyUser User;
    private List<String> PhotoUrl=new ArrayList<>();
    private String photo_url;
    private BmobRelation participant;


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

    public List<String> getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(List<String> photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public BmobRelation getParticipant() {
        return participant;
    }

    public void setParticipant(BmobRelation participant) {
        this.participant = participant;
    }
}
