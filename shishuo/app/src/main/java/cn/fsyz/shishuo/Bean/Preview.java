package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by JALOR on 2018/2/13.
 */


//预告的Bean
public class Preview extends BmobObject {
    private String preview;
    private BmobFile Wel;
    private Boolean Show;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }


    public BmobFile getWel() {
        return Wel;
    }

    public void setWel(BmobFile wel) {
        Wel = wel;
    }

    public Boolean getShow() {
        return Show;
    }

    public void setShow(Boolean show) {
        Show = show;
    }
}
