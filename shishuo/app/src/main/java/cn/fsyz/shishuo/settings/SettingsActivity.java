package cn.fsyz.shishuo.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.fsyz.shishuo.ActivityCollector;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.ControlActivity;
import cn.fsyz.shishuo.HighsettingsActivity;
import cn.fsyz.shishuo.Main.Nosad_managerActivity;
import cn.fsyz.shishuo.Main.TalkOpenActivity;
import com.fsyz.shishuo.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


//设置列表
public class SettingsActivity extends BaseActivity {

    Toolbar toolbar;
    //改写数据
    SharedPreferences.Editor editor;
    //权限检查
    String right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        //绑定是否同意的状态
        SharedPreferences shared = getSharedPreferences("agree",MODE_PRIVATE);
        editor=shared.edit();
        toolbar = (Toolbar)findViewById(R.id.toolbar_setting);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("设置");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar
        toolbar.inflateMenu(R.menu.manager_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.manager:
                                //数字化权限
                                int righti = Integer.parseInt(right);
                                if (righti==5){
                                    Intent inten1t = new Intent(SettingsActivity.this, ControlActivity.class);
                                    startActivity(inten1t);
                                }else{
                                    ShowToast("你无此权限进入总控制中心");
                                }
                        break;
                    case R.id.nosad_manager:
                                int righti2 = Integer.parseInt(right);
                                if (righti2==6){
                                    Intent inten1t = new Intent(SettingsActivity.this, Nosad_managerActivity.class);
                                    startActivity(inten1t);
                                }else{
                                    ShowToast("你无此权限进入解忧回复");
                                }
                        break;
                    case R.id.talk_manager:
                        int righti3 = Integer.parseInt(right);
                        if (righti3==5){
                            Intent inten1t = new Intent(SettingsActivity.this, TalkOpenActivity.class);
                            startActivity(inten1t);
                        }else{
                            ShowToast("你无此权限进入话题发表");
                        }
                }
                return false;
            }
        });
        ListView list1 = (ListView) findViewById(R.id.list1);
        // 定义一个数组
        final String[] arr1 = { "版本信息","检查更新","服务协议"};
        // 将数组包装为ArrayAdapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this, R.layout.array_item, arr1);
        // 为ListView设置Adapter
        list1.setAdapter(adapter1);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = arr1[position];
                if(name.contentEquals("版本信息")){
                    Intent intent = new Intent(SettingsActivity.this,Systeminfo_activity.class);
                    startActivity(intent);
                }else if(name.contentEquals("检查更新")){
                    Intent intent = new Intent(SettingsActivity.this,UpdateActivity.class);
                    startActivity(intent);
                }else if (name.contentEquals("服务协议")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setIcon(R.mipmap.logo);
                    builder.setTitle("【诗】说服务协议");
                    //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                    view = LayoutInflater.from(SettingsActivity.this).inflate(R.layout.copyright_private, null);
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
                            editor.putString("isfer", "n");
                            editor.commit();
                            BmobUser.logOut();
                            ActivityCollector.finishAll();
                            finish();
                        }
                    });
                    builder.show();
                }
            }
        });
        ListView list2 = (ListView) findViewById(R.id.list2);
        // 定义一个数组
        final String[] arr2 = { "版权声明"};
        //final String[] arr2 = { "高级设置","版权声明"};
        // 将数组包装为ArrayAdapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, R.layout.array_item, arr2);
        // 为ListView设置Adapter
        list2.setAdapter(adapter2);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = arr2[position];
                if (name.contentEquals("版权声明")){
                    Intent intent = new Intent(SettingsActivity.this,copyrightActivity.class);
                    startActivity(intent);
                }else if (name.contentEquals("高级设置")){
                    Intent intent = new Intent(SettingsActivity.this,HighsettingsActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public  boolean onCreateOptionsMenu(final Menu menu) {
        //点击进入管理状态

        final String username = (String) MyUser.getObjectByKey("objectId");
        BmobQuery<Power> query = new BmobQuery<Power>();
        query.addWhereEqualTo("User",username);
        query.findObjects(new FindListener<Power>() {
            @Override
            public void done(List<Power> list, BmobException e) {
                for (Power gameScore : list) {
                    //获得playerName的信息
                    right = gameScore.getRight();
                }int righti = Integer.parseInt(right);
                if (righti>=5){
                    getMenuInflater().inflate(R.menu.manager_main, menu);
                }
            }
        });
        return true;
    }
}
