package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jalor on 2018/5/19.
 */

public class BsenderTitle extends BmobObject {
    private String qtitle;
    private String questions;
    private String Photo;
    private MyUser center;
    private List<String> PhotoArray=new ArrayList<>();

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public MyUser getCenter() {
        return center;
    }

    public void setCenter(MyUser center) {
        this.center = center;
    }

    public List<String> getPhotoArray() {
        return PhotoArray;
    }
}
