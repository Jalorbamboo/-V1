package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cn.fsyz.shishuo.Bean.MyUser;

import com.fsyz.shishuo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by JALOR on 2018/2/10.
 */

//更改信息时的再次确认活动
public class EnsureActivity extends BaseActivity{

    EditText password;
    TextView login;
    private final int PROCESS2 = 2;
    private static final int dismiss = 3;
    private static final int dismiss1 = 4;
    ProgressDialog progressDialog,progressDialog1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(EnsureActivity.this);
                    progressDialog1.setTitle("正在登陆中，请等待");
                    progressDialog1.show();
                    break;
                case dismiss:
                    progressDialog.dismiss();
                    break;
                case dismiss1:
                    progressDialog1.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ensure);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_sure);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("请确认你的账号");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar

        final MyUser current = MyUser.getCurrentUser(MyUser.class);
        final BmobUser myUser = new BmobUser();
        password = (EditText)findViewById(R.id.password_ensure);
        login = (TextView) findViewById(R.id.tv_sign_ensure);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(PROCESS2);
                String cu = current.getEmail();
                Log.e("current",""+cu);
                //if (cu.contentEquals(u)){
                  // ShowToast("账号输入正确");
              //  }else {
               //     ShowToast("请用当前用户登陆");
              //      handler.sendEmptyMessage(dismiss1);
              //      return;
             //   }
                String p = password.getText().toString();
                BmobUser.loginByAccount(cu, p, new LogInListener<BmobUser>() {

                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(e==null){
                            if (user.getEmailVerified().toString().contentEquals("true")){
                                Log.i("smile","用户登陆成功");
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("登陆成功");
                                Intent intent = new Intent(EnsureActivity.this,UserDetailActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("请去你的邮箱点击认证链接");
                            }
                        }else{
                            handler.sendEmptyMessage(dismiss1);
                            ShowToast(""+e.getMessage());
                        }
                    }
                });
            }
        });


    }

    private void init(){
        Bmob.initialize(EnsureActivity.this, "8780088b394aa22ea2abbbec1bcebc8d");
    }


}
