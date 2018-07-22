package cn.fsyz.shishuo.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.WhiteAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.WhiteComment;
import com.fsyz.shishuo.R;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WhiteActivity extends BaseActivity
        implements  PullLoadMoreRecyclerView.PullLoadMoreListener{

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private WhiteAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_white);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_white);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("【诗】说de匿名真心话");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView_white);
        //获取mRecyclerView对象
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //设置下拉刷新是否可见
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置是否可以下拉刷新
        //mPullLoadMoreRecyclerView.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        //mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
        //显示下拉刷新
        mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置上拉刷新文字
        mPullLoadMoreRecyclerView.setFooterViewText("加载ing");
        //设置上拉刷新文字颜色
        //mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.white);
        //设置加载更多背景色
        //mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.colorBackground);
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mRecyclerViewAdapter = new WhiteAdapter(this);
        mRecyclerViewAdapter.setOnItemClickListener(new WhiteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(WhiteActivity.this,White_MessageDetailActivity.class);
                try{
                String c = (String) ((TextView)view.findViewById(R.id.text3)).getText();
                String t = (String) ((TextView)view.findViewById(R.id.author3)).getText();
                String url  =(String) ((TextView)view.findViewById(R.id.url)).getText();
                String top = (String) ((TextView)view.findViewById(R.id.title3)).getText();
                String id = (String) ((TextView)view.findViewById(R.id.id3)).getText();
                    Log.e("Mian id is ",""+url);
                    intent.putExtra("comment",c);
                    intent.putExtra("author",t);
                    intent.putExtra("url",url);
                    intent.putExtra("top",top);
                    intent.putExtra("id",id);
                }catch(Exception e){
                    String c = (String) ((TextView)view.findViewById(R.id.text4)).getText();
                    String t = (String) ((TextView)view.findViewById(R.id.author4)).getText();
                    //String url  =(String) ((TextView)view.findViewById(R.id.url)).getText();
                    String top = (String) ((TextView)view.findViewById(R.id.title4)).getText();
                    String id = (String) ((TextView)view.findViewById(R.id.id4)).getText();
                    //Log.e("Mian id is ",""+url);
                    intent.putExtra("comment",c);
                    intent.putExtra("author",t);
                    //intent.putExtra("url",url);
                    intent.putExtra("top",top);
                    intent.putExtra("id",id);
                    e.getStackTrace();
                }
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        getData();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_white);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser bmobUser = BmobUser.getCurrentUser() ;
                if (bmobUser!=null){
                    Intent intent = new Intent(WhiteActivity.this,WhiteSendActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "你使用的是非官方软件，请注意", Snackbar.LENGTH_LONG)
                            .setAction("null", null).show();
                }
            }
        });
    }



    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }


    private void getData() {
        Log.e("getdate","run0");

        Log.e("getdate","run");

        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask()
        {
            @Override
            public void run() {

                mPullLoadMoreRecyclerView.setRefreshing(false);

                Log.e("getdate","run3");
            }
        };
        timer.schedule(timerTask,3000);

        BmobQuery<WhiteComment> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("MyUser");
        // 查找数据
        query.setLimit(10);
        query.findObjects(new FindListener<WhiteComment>() {
            @Override
            public void done(final List<WhiteComment> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        Log.e("getdate","run1");
                        mRecyclerViewAdapter.addAllData(list);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        timer.cancel();
                    }else{
                        Log.e("getdate","run2");
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        ShowToast("没有信息了");
                        timer.cancel();
                    }

                }
            }});

    }





    @Override
    public void onRefresh() {
        setRefresh();
        getData();
    }

    @Override
    public void onLoadMore() {
        Log.e("wxl", "onLoadMore");

        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask()
        {
            @Override
            public void run() {

                mPullLoadMoreRecyclerView.setRefreshing(false);

            }
        };
        timer.schedule(timerTask,3000);

        BmobQuery<WhiteComment> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        // 查找数据
        query.setLimit(10);
        query.setSkip(10*mCount);
        query.findObjects(new FindListener<WhiteComment>() {
            @Override
            public void done(final List<WhiteComment> list, BmobException e) {
                if (e == null) {



                    if (list.size() > 0) {
                        mRecyclerViewAdapter.addAllData(list);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        timer.cancel();
                    }else{
                        ShowToast("已无更多数据");
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        timer.cancel();
                    }



                }
            }});
        mCount = mCount + 1;

    }

    private void setRefresh() {
        mRecyclerViewAdapter.clearData();
        mCount = 1;
    }



}
