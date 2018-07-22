package cn.fsyz.shishuo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;

import com.fsyz.shishuo.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

//控制活动
public class ControlActivity extends BaseActivity {


    EditText name,changestring;
    Button sure;
    String cna,id;
    int cs;
    MyUser myUser;
    String Id,r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        name = findViewById(R.id.change_name);
        changestring = findViewById(R.id.change_string);
        sure = findViewById(R.id.control);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cna = name.getText().toString();
                try{
                cs = Integer.parseInt(changestring.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (cs>3){
                    ShowToast("权限值不得超过3");
                    return;
                }
                if (cs==0){
                    ShowToast("你居然又设置为0，是不是不想混了？");
                    return;
                }
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.addWhereEqualTo("username", cna);
                query.findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(List<MyUser> object, BmobException e) {
                        if(e==null){
                            for (MyUser gameScore : object) {
                                //获得playerName的信息
                                id = gameScore.getObjectId();
                                //myUser = gameScore;
                            }
                            BmobQuery<Power> query1 = new BmobQuery<Power>();
                            query1.addWhereEqualTo("User",id);
                            query1.findObjects(new FindListener<Power>() {
                                @Override
                                public void done(List<Power> list, BmobException e) {
                                    if(e==null){
                                        for (Power gameScore : list) {
                                            Id = gameScore.getObjectId();
                                            r = gameScore.getRight();
                                        }
                                        if (r.contentEquals("5")){
                                            ShowToast("对方是管理员，你无法修改！");
                                            return;
                                        }
                                        Power power = new Power();
                                        power.setRight(String.valueOf(cs));
                                        power.update(Id,new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e==null){
                                                    ShowToast("修改成功");
                                                    finish();
                                                }else {
                                                    ShowToast(""+e.getMessage());
                                                }
                                            }
                                        });
                                    }else{
                                        ShowToast("请重新输入用户信息:" + e.getMessage());
                                    }
                                }
                            });
                            //Log.e("User id is",id);
                        }else{
                            ShowToast("请重新输入用户信息:" + e.getMessage());
                        }
                    }
                });

            }
        });
    }
}
