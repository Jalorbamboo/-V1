package cn.fsyz.shishuo.Bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Jalor on 2018/4/26.
 */

public class AppVersion extends BmobObject {
    private Number version_i;
    private BmobFile path;
    private String target_side;
    private String update_log;
    private String version;
    private Boolean isforce;
    private String versioncode;

    public Number getVersion_i() {
        return version_i;
    }

    public void setVersion_i(Number version_i) {
        this.version_i = version_i;
    }

    public BmobFile getPath() {
        return path;
    }

    public void setPath(BmobFile path) {
        this.path = path;
    }

    public String getTarget_side() {
        return target_side;
    }

    public void setTarget_side(String target_side) {
        this.target_side = target_side;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsforce() {
        return isforce;
    }

    public void setIsforce(Boolean isforce) {
        this.isforce = isforce;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }
}
