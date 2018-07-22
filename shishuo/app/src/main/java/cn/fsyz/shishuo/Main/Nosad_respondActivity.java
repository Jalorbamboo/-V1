package cn.fsyz.shishuo.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Nosad;
import com.fsyz.shishuo.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


//解忧杂货店回复类
public class Nosad_respondActivity extends BaseActivity {

    String id;

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
                    progressDialog = new ProgressDialog(Nosad_respondActivity.this);
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_nosad_respond);
        Intent intent = getIntent();
        id = intent.getStringExtra("Id");
        Log.e("receive",""+id);
        final EditText nosad_content = findViewById(R.id.nosad_respond_content);
        //获取当前用户
        final MyUser myUser = MyUser.getCurrentUser(MyUser.class);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_nosad_respond);//toolbar
        toolbar.setTitle("回复这位小盆友");//toolbar
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
                        handler.sendEmptyMessage(mPROCESS);
                        Discuss discuss = new Discuss();
                        Nosad nosad = new Nosad();
                        nosad.setObjectId(id);
                        discuss.setNosad(nosad);
                        discuss.setBmobUser(MyUser.getCurrentUser());
                        discuss.setDiscuss(nosad_content.getText().toString());
                        discuss.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    handler.sendEmptyMessage(DISMISS);
                                    ShowToast("回复小朋友成功啦");
                                    finish();
                                }else{
                                    handler.sendEmptyMessage(DISMISS);
                                    ShowToast("发送失败"+e.getMessage());
                                }
                            }
                        });
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