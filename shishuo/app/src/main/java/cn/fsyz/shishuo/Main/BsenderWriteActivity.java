package cn.fsyz.shishuo.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Bsender;
import cn.fsyz.shishuo.Bean.BsenderTitle;
import cn.fsyz.shishuo.Bean.MyUser;
import com.fsyz.shishuo.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jalor on 2018/5/20.
 */

public class BsenderWriteActivity extends BaseActivity {
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
                    progressDialog = new ProgressDialog(BsenderWriteActivity.this);
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
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bsender_send);
        final EditText nosad_content = findViewById(R.id.nosad_bsender_content);
        //获取当前用户
        final MyUser myUser = MyUser.getCurrentUser(MyUser.class);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bsender_send);//toolbar
        id = getIntent().getStringExtra("id_Bs");
        toolbar.setTitle("我的投稿");//toolbar
        setSupportActionBar(toolbar);//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                            ShowToast("写给广播站的稿不可以是空的哦！");
                            return true;
                        }
                        Bsender nosad = new Bsender();
                        BsenderTitle bsenderTitle = new BsenderTitle();
                        bsenderTitle.setObjectId(id);
                        nosad.setBsenderTitle(bsenderTitle);
                        nosad.setContent(mContent);
                        nosad.setUser(myUser);
                        nosad.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    handler.sendEmptyMessage(DISMISS);
                                    ShowToast("你的广播站的已经发送了，请耐心等待广播站的回复吧！");
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
