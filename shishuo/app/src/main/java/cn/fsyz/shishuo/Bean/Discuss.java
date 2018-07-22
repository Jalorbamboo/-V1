package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by JALOR on 2018/2/2.
 */

//评论的Bean
public class Discuss extends BmobObject {
    private Comment comment;
    private BmobUser bmobUser;
    private String discuss;
    private Huodong huodong;
    private WhiteComment whitecomment;
    private cn.fsyz.shishuo.Bean.Nosad Nosad;
    private cn.fsyz.shishuo.Bean.Talk Talk;
    private cn.fsyz.shishuo.Bean.Bsender Bsender;

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
    }

    public BmobUser getBmobUser() {
        return bmobUser;
    }

    public void setBmobUser(BmobUser bmobUser) {
        this.bmobUser = bmobUser;
    }


    public Huodong getHuodong() {
        return huodong;
    }

    public void setHuodong(Huodong huodong) {
        this.huodong = huodong;
    }



    public WhiteComment getWhitecomment() {
        return whitecomment;
    }

    public void setWhitecomment(WhiteComment whitecomment) {
        this.whitecomment = whitecomment;
    }


    public cn.fsyz.shishuo.Bean.Nosad getNosad() {
        return Nosad;
    }

    public void setNosad(cn.fsyz.shishuo.Bean.Nosad nosad) {
        Nosad = nosad;
    }

    public cn.fsyz.shishuo.Bean.Talk getTalk() {
        return Talk;
    }

    public void setTalk(cn.fsyz.shishuo.Bean.Talk talk) {
        Talk = talk;
    }

    public cn.fsyz.shishuo.Bean.Bsender getBsender() {
        return Bsender;
    }

    public void setBsender(cn.fsyz.shishuo.Bean.Bsender bsender) {
        Bsender = bsender;
    }
}
