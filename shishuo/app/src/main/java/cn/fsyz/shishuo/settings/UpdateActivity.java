package cn.fsyz.shishuo.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Preview;
import com.fsyz.shishuo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

//升级类
public class UpdateActivity extends BaseActivity {
    TextView preview;
    String sth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_Update);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("检查更新");//toolbar
        preview = findViewById(R.id.call_new);
        getNew();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar

        Button button = findViewById(R.id.update_check);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUpdateAgent.forceUpdate(getParent());
                BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        // TODO Auto-generated method stub
                        if (updateStatus == UpdateStatus.Yes) {//版本有更新
                        }else if(updateStatus == UpdateStatus.No){
                            Toast.makeText(UpdateActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
                        }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                            Toast.makeText(UpdateActivity.this, "链接服务错误", Toast.LENGTH_SHORT).show();
                        }else if(updateStatus==UpdateStatus.IGNORED){
                            Toast.makeText(UpdateActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                        }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
                            Toast.makeText(UpdateActivity.this, "链接服务错误", Toast.LENGTH_SHORT).show();
                        }else if(updateStatus==UpdateStatus.TimeOut){
                            Toast.makeText(UpdateActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //发起自动更新
            }
        });
    }

    private void getNew() {
        Log.e("QUery","run");
        BmobQuery<Preview> query = new BmobQuery<>();
        query.getObject("QJGL111M", new QueryListener<Preview>() {
            @Override
            public void done(Preview preview1, BmobException e) {
                if (e==null){
                        sth = preview1.getPreview();

                    preview.setText(sth);
                }else{
                    ShowToast("获取信息失败");
                }
            }
        });

    }

    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }
}
