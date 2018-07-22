package cn.fsyz.shishuo.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by JALOR on 2018/3/2.
 */

//话题题目
public class TalkTitle extends BmobObject {
    private String qtitle;
    private String questions;
    private String Photo;
    private MyUser teacher;
    private List<String> PhotoArray=new ArrayList<>();

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public MyUser getTeacher() {
        return teacher;
    }

    public void setTeacher(MyUser teacher) {
        this.teacher = teacher;
    }

    public List<String> getPhotoArray() {
        return PhotoArray;
    }

    public void setPhotoArray(List<String> photoArray) {
        PhotoArray = photoArray;
    }
}
