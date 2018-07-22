package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;

import com.fsyz.shishuo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

//注册类
public class SighActivity extends BaseActivity {

    Button sigh;
    EditText user;
    EditText password,email,classes,grade,school,rname,remail;

    private final int PROCESS = 1;
    private final int PROCESS2 = 2;
    private static final int dismiss = 3;
    private static final int dismiss1 = 4;
    ProgressDialog progressDialog,progressDialog1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS:
                    progressDialog = new ProgressDialog(SighActivity.this);
                    progressDialog.setTitle("正在注册中，请等待");
                    progressDialog.show();
                    break;
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(SighActivity.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_sigh);
        user = findViewById(R.id.User);
        password = findViewById(R.id.Password);
        sigh = findViewById(R.id.sigh);
        email = findViewById(R.id.Emai);
        classes = findViewById(R.id.Realclass);
        rname = findViewById(R.id.Realname);
        school = findViewById(R.id.RealSchool);
        grade = findViewById(R.id.RealGrade);
        remail = findViewById(R.id.Email);
        sigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(PROCESS);
                String u = user.getText().toString();
                final String p = password.getText().toString();
                String e = email.getText().toString();
                String name = rname.getText().toString();
                String s = school.getText().toString();
                String g = grade.getText().toString();
                String c = classes.getText().toString();
                String t = remail.getText().toString();
                if (e.contentEquals(t)){
                    Log.e("en","");
                }else{
                    ShowToast("请确认你的邮箱,两次邮箱不一样！");
                    handler.sendEmptyMessage(dismiss);
                    return;
                }
                if (name.contentEquals("")){
                    ShowToast("请输入你的真实姓名");
                    handler.sendEmptyMessage(dismiss);
                    return;
                }else if (c.contentEquals("")){
                    ShowToast("请输入你的真实班级");
                    handler.sendEmptyMessage(dismiss);
                    return;
                }else if (s.contentEquals("")){
                    ShowToast("请输入你的真实学校");
                    handler.sendEmptyMessage(dismiss);
                    return;
                }else if (g.contentEquals("")){
                    ShowToast("请输入你的真实年级");
                    handler.sendEmptyMessage(dismiss);
                    return;
                }
                MyUser myUser = new MyUser();
                myUser.setUsername(u);
                myUser.setEmail(e);
                myUser.setPassword(p);
                myUser.setClasses(c);
                myUser.setRealname(name);
                myUser.setGrade(g);
                //myUser.setPower("1");
                myUser.setSchool(s);
                myUser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser bmobUser, BmobException e) {
                        if (e==null){
                            ShowToast("注册成功，请登陆，请点击邮箱中的链接认证！");
                            addRole(bmobUser);
                            handler.sendEmptyMessage(dismiss);
                            finish();
                        }else{
                            ShowToast(""+e.getMessage());
                            handler.sendEmptyMessage(dismiss);
                            Log.e("detail",""+e);
                        }
                    }
                });
            }
        });
    }

    private void addRole(MyUser bmobUser) {
        //注：BmobRole的角色名称-必须为英文,中文会创建不成功
        Power power = new Power();
        power.setUser(bmobUser);
        power.setRight("1");
        power.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    ShowToast("欢迎您体验【诗】说(普通用户)");
                }else{
                    ShowToast(""+e.getMessage());
                }
            }
        });
        //BmobRole androidRole = new BmobRole("common");
        //将当前用户添加到他选择的用户组中
        //BmobUser user = BmobUser.getCurrentUser();
       // androidRole.setObjectId("4zCTqqqw");
       // androidRole.getUsers().add(user);
          //  androidRole.update(new UpdateListener() {
             //   @Override
             //   public void done(BmobException e) {
             //       if(e==null){
             //           ShowToast("欢迎您体验【诗】说(普通用户)");
             //       }
            //    }
         //   });

            BmobUser.logOut();

    }

    private void init(){
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }
}
