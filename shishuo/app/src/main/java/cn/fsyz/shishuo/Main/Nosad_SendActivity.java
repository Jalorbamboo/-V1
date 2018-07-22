package cn.fsyz.shishuo.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Nosad;

import com.fsyz.shishuo.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Nosad_SendActivity extends BaseActivity {


    private final int mPROCESS = 1;
    private static final int DISMISS = 2;
    //进度条
    ProgressDialog progressDialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case mPROCESS:
                    progressDialog = new ProgressDialog(Nosad_SendActivity.this);
                    progressDialog.setTitle("发送中，请耐心等待");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    break;
                case DISMISS:
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nosad__send);
        final EditText nosad_content = findViewById(R.id.nosad_send_content);
        final CheckBox checkBox = findViewById(R.id.nosad_check_box);
        //获取当前用户
        final MyUser myUser = MyUser.getCurrentUser(MyUser.class);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_nosad_send);//toolbar
        toolbar.setTitle("发送我的信");//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        //设置菜单的按钮
        toolbar.inflateMenu(R.menu.send_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nosad_send_menu:
                        //显示加载框
                        handler.sendEmptyMessage(mPROCESS);
                        String mContent = nosad_content.getText().toString();
                        if(mContent.contentEquals("")){
                            ShowToast("写给爷爷的信不可以是空的呀！");
                            return true;
                        }
                        Nosad nosad = new Nosad();
                        if (checkBox.isChecked()){
                            nosad.setPublicorno(true);
                        }else{
                           // BmobRole hr = new BmobRole("xinxie");
                            //加人必须这样更新来加
                            //hr.setObjectId("e8e1ec270b");
                            //hr.getUsers().add(MyUser.getCurrentUser());
                            //hr.update(new UpdateListener() {
                            //    @Override
                            //    public void done(BmobException e) {
                            //        if (e==null){
                            //            ShowToast("加入成功！");
                            //        }else{
                            //            ShowToast("更新"+e.getMessage());
                            //        }
                            //    }
                            //});
                            nosad.setPublicorno(false);
                        }
                        nosad.setContent(mContent);
                        nosad.setUser(myUser);
                        nosad.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    handler.sendEmptyMessage(DISMISS);
                                    ShowToast("你的信已经发送了，请耐心等待老爷爷的回复吧！");
                                    finish();
                                } else {
                                    handler.sendEmptyMessage(DISMISS);
                                    ShowToast("发生错误" + "" + e.getMessage());
                                }
                            }
                        });
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }
}
