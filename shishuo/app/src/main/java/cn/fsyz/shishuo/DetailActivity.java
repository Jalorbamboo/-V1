package cn.fsyz.shishuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.fsyz.shishuo.R;
import com.just.agentweb.AgentWeb;

import cn.fsyz.shishuo.Bean.Banner;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by JALOR on 2018/1/30.
 */

//Banner的详情活动
public class DetailActivity extends BaseActivity{
    String web,title;
    AgentWeb mAgentWeb;
    LinearLayout mLinearLayout;
    Toolbar toolbar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail);

        mLinearLayout = findViewById(R.id.container_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        try{
        Log.e("i is", String.valueOf(position));
        }catch (Exception e){
            e.printStackTrace();
        }
        Query_m(position);

        toolbar = (Toolbar)findViewById(R.id.toolbar_detail);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        //toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar



    }

    private void Query_m(final int position) {
        Log.e("QUery","run");
        BmobQuery<Banner> query = new BmobQuery<>();
        query.findObjects(new FindListener<Banner>() {
            @Override
            public void done(List<Banner> list, BmobException e) {
                if (e==null){
                    web = list.get(position).getWeb();
                    title = list.get(position).getTitle();
                    toolbar.setTitle(""+title);
                    Shoewweb(web);
                }
            }
        });
    }

    private void Query(int position) {
        Log.e("QUery","run");
        BmobQuery<Banner> query = new BmobQuery<>();
        query.addWhereEqualTo("position",String.valueOf(position));
        query.findObjects(new FindListener<Banner>() {
            @Override
            public void done(List<Banner> object, BmobException e) {
                Log.e("Query","run1");
                if (e==null){
                    for (Banner gameScore : object) {
                        //获得playerName的信息
                        web = gameScore.getWeb();
                        title = gameScore.getTitle();
                    }
                    Log.e("web",web);
                    toolbar.setTitle(""+title);
                    Shoewweb(web);
                }else{
                    ShowToast("获取信息失败");
                }
            }
        });
    }

    private void Shoewweb(String web) {
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .createAgentWeb()//
                .ready()
                .go(web);

    }
}
