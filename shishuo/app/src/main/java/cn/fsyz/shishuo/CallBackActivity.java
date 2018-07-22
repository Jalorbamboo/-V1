package cn.fsyz.shishuo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import cn.fsyz.shishuo.Bean.Feedback;
import cn.fsyz.shishuo.Bean.MyUser;

import com.fsyz.shishuo.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by JALOR on 2018/2/13.
 */

//BUG反馈的Activity
public class CallBackActivity extends BaseActivity {

    EditText QQ,Problem,Number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        QQ = findViewById(R.id.QQ);
        Problem = findViewById(R.id.problem);
        Number = findViewById(R.id.call);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_feedback);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("反馈bug");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 此处可执行登录处理
        Button sure = findViewById(R.id.feedback_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast("正在反馈，请不要多次点击按钮");
                String qq = QQ.getText().toString();
                String problem = Problem.getText().toString();
                String call = Number.getText().toString();
                if (qq.contentEquals("")){
                    ShowToast("请输入QQ号");
                    return;
                }else if (problem.contentEquals("")){
                    ShowToast("请输入你的问题");
                    return;
                }else if (call.contentEquals("")){
                    ShowToast("请输入电话号码");
                    return;
                }
                Feedback feedback = new Feedback();
                feedback.setQQ(qq);
                feedback.setCall(call);
                feedback.setProblem(problem);
                MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
                feedback.setUser(bmobUser);
                QQ.setText("");
                Problem.setText("");
                Number.setText("");
                feedback.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ShowToast("反馈成功！");
                            finish();
                        } else {
                            ShowToast("" + e.getMessage());
                        }
                    }
                });
            }
        });



    }
}
