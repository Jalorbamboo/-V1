package cn.fsyz.shishuo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import cn.fsyz.shishuo.Bean.AppVersion;
import cn.fsyz.shishuo.Bean.MyInstallation;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Main.MainActivity;

import com.fsyz.shishuo.R;

import cn.fsyz.shishuo.sign.TransitionView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

/**
 * Created by JALOR on 2018/1/24.
 */

//登陆活动
public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnDownloadListener {

    //得到权限的int
    private static final int GETPERMISSION = 1;

    //改写数据
    SharedPreferences.Editor editor;

    private TransitionView mAnimView;
    EditText user,password;

    TextView sigh;
    TextView login;
    TextView email;


    private static final int dismiss = 3;

    ProgressDialog progressDialog,progressDialog1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(LoginActivity.this);
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
        setContentView(R.layout.activity_login);
        agree();
        //检查更新
        UpdateOrNot();
        final Intent intent = getIntent();
        final String  id =  intent.getStringExtra("id");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_login);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("登陆");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar

        mAnimView = (TransitionView) findViewById(R.id.ani_view);

        mAnimView.setOnAnimationEndListener(new TransitionView.OnAnimationEndListener()
        {
            @Override
            public void onEnd()
            {
                //跳转到主页面
                gotoHomeActivity();
            }
        });

        //获得权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            ShowToast("欢迎使用诗说！");
        } else {
            EasyPermissions.requestPermissions(this, "诗说需要以下权限:\n\n1.获得手机的识别码（用于登陆）\n2.获得手机的储存读取权限（" +
                    "用于软件的更新）+\n3.获取手机拍照权限（识别二维码）", GETPERMISSION, perms);
        }

        BmobUser myUser = new BmobUser();
        user = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        sigh = (TextView) findViewById(R.id.email_sign_in_button);
        login = (TextView) findViewById(R.id.tv_sign_up);
        final CoordinatorLayout coordinatorLayout = findViewById(R.id.login_view);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                handler.sendEmptyMessage(PROCESS2);
                final String u = user.getText().toString();
                String p = password.getText().toString();
                BmobUser.loginByAccount(u, p, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(e==null){
                                Log.i("smile", "用户登陆成功");
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("登陆成功");
                                modifyInstallationUser(user);
                                mAnimView.startAnimation();
                        }else{
                            handler.sendEmptyMessage(dismiss1);
                            ShowToast(""+e.getMessage());
                        }
                    }
                });
            }
        });
        sigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SighActivity.class);
                startActivity(intent);
            }
        });
        email = findViewById(R.id.change_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("请输入账号");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.forget_passward, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);


                final EditText forget = (EditText)view.findViewById(R.id.forget_pd);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        final String b = forget.getText().toString();
                        //    将输入的用户名和密码打印出来
                        BmobUser.resetPasswordByEmail(b, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ShowToast("请求改密邮件成功，请到" + b + "邮箱中进行激活。");
                                }else{
                                    ShowToast("失败:" + e.getMessage());
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });

    }

    private void agree() {
        SharedPreferences shared = getSharedPreferences("agree",MODE_PRIVATE);
        String isfer = shared.getString("isfer","0");
        editor=shared.edit();
        if(isfer.equals("y")){
            //第二次进入跳转  
            Log.e("log","1");
        }else{
            //第一次进入跳转 
            jugde();
            Log.e("log","can");
        }
    }



    private void jugde() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("【诗】说服务协议");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.copyright_private, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);

        builder.setPositiveButton("接受", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                editor.putString("isfer", "y");
                editor.commit();
            }
        });
        builder.setNegativeButton("不接受", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.show();
    }


    private void modifyInstallationUser(final BmobUser user) {
        BmobQuery<MyInstallation> bmobQuery = new BmobQuery<>();
        final String id = BmobInstallationManager.getInstallationId();
        bmobQuery.addWhereEqualTo("installationId", id);
        bmobQuery.findObjectsObservable(MyInstallation.class)
                .subscribe(new Action1<List<MyInstallation>>() {
                    @Override
                    public void call(List<MyInstallation> installations) {

                        if (installations.size() > 0) {
                            MyInstallation installation = installations.get(0);
                            installation.setUser(user);
                            installation.updateObservable()
                                    .subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            //ShowToast("更新设备用户信息成功！");
                                            ChangeUDID();
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            ShowToast("更新设备用户信息失败：" + throwable.getMessage());
                                        }
                                    });

                        } else {
                            ShowToast("后台不存在此设备Id的数据，请确认此设备Id是否正确！\n" + id);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ShowToast("查询设备数据失败：" + throwable.getMessage());
                    }
                });
    }

    //更换设备ID以及更新用户权限
    private void ChangeUDID() {
        BmobInstallation bmobInstallation = new BmobInstallation();
        String id  = bmobInstallation.getInstallationId();
        MyUser newUser = new MyUser();
        newUser.setUDID(id);
        MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //Log.e("成功","更换数据");
                }else{
                    handler.sendEmptyMessage(dismiss1);
                    Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    //Log.e("cuowu",""+e.getMessage());
                }
            }
        });
    }

    private void init(){
        //Bmob.initialize(LoginActivity.this, "6cb5cd44d15ed665ff487a51fa84537e");
        BmobConfig config =new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId("8780088b394aa22ea2abbbec1bcebc8d ")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(5)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                //.setUploadBlockSize(1024*1024)
                ////文件的过期时间(单位为秒)：默认1800s
                //.setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }

    private void gotoHomeActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    Toast mToast;

    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 把执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
        ShowToast("缺乏必要的权限，【诗】说无法运行，请到设置内给予权限");
    }

    private void UpdateOrNot() {
        BmobQuery<AppVersion> appVersionBmobQuery = new BmobQuery<>();
        appVersionBmobQuery.order("-createdAt");
        appVersionBmobQuery.findObjects(new FindListener<AppVersion>() {
            @Override
            public void done(List<AppVersion> list, BmobException e) {
                if (e==null){

                    String a = list.get(0).getVersioncode();
                    int i = Integer.parseInt(a);
                    String Log = list.get(0).getUpdate_log();
                    String target_size = list.get(0).getTarget_side();
                    String version = list.get(0).getVersion();
                    String Url = list.get(0).getPath().getFileUrl();
                    Boolean isForse = list.get(0).getIsforce();
                    CheckNewVersion(Url,isForse,i,Log,target_size,version);

                }
            }
        });
    }

    private void CheckNewVersion(String url, Boolean isForse, int a, String log, String target_size, String version) {
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //支持断点下载
                .setBreakpointDownload(true)
                //设置是否显示通知栏进度
                .setShowNotification(false)
                //设置强制更新
                .setForcedUpgrade(isForse)
                //设置下载过程的监听
                .setOnDownloadListener(this);
        DownloadManager manager = DownloadManager.getInstance(this);
        manager.setApkName("诗说.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(false)
                .setConfiguration(configuration)
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setApkVersionCode(a)
                .setApkVersionName(version)
                .setApkSize(target_size)
                .setApkDescription(log)
                .download();
    }

    @Override
    public void start()
    {
        handlerF.sendEmptyMessage(PROCESS2);
    }

    @Override
    public void downloading(final int max, final int progress) {
    }

    @Override
    public void done(File apk) {
        Log.e("已下载完毕","");
    }

    @Override
    public void error(Exception e) {

    }



    private final int PROCESS2 = 2;
    private static final int dismiss1 = 4;
    private static final int dismiss2 = 5;
    private ProgressDialog pd6;
    @SuppressLint("HandlerLeak")
    private Handler handlerF = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESS2:
                    pd6 = new ProgressDialog(LoginActivity.this);
                    pd6.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置水平进度条
                    pd6.setCancelable(false);// 设置是否可以通过点击Back键取消
                    pd6.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    pd6.setIcon(R.mipmap.logo);// 设置提示的title的图标，默认是没有的
                    pd6.setTitle("提示");
                    pd6.setMessage("新版本下载中,请耐心等待，大概15秒的下载时间");
                    pd6.show();
                    break;
                case dismiss1:
                    pd6.dismiss();
                    break;
                case dismiss2:
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("安装中");
                    progressDialog.show();
                    break;
            }
        }
    };


}
