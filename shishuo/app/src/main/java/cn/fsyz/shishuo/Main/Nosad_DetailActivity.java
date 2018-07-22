package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.DiscuzzAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Nosad;
import cn.fsyz.shishuo.Bean.Power;

import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

//解忧杂货的详情活动
public class Nosad_DetailActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    String id;

    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private DiscuzzAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消原有标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nosad__detail);
        final Intent intent = getIntent();
        id  = intent.getStringExtra("id");
        final TextView textView = findViewById(R.id.text_nosad_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_nosad_detail);//toolbar
        BmobQuery<Nosad> query0 = new BmobQuery<>();
        query0.include("User");
        query0.getObject(""+id, new QueryListener<Nosad>() {
            @Override
            public void done(Nosad huodong, BmobException e) {
                if (e==null){
                    textView.setText(huodong.getContent());
                }else {
                    ShowToast("加载失败！");
                }
            }
        });
        toolbar.setTitle("信");//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.inflateMenu(R.menu.nosad_discuzz);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_nosad_discuzz:
                        Intent intent = new Intent(Nosad_DetailActivity.this,Nosad_respondActivity.class);
                        intent.putExtra("Id",id);
                        Log.e("pass id",""+id);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView_nosad_detail);
        //获取mRecyclerView对象
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //以下两项优化内存
        //mRecyclerView.setHasFixedSize(false);
        //mRecyclerView.setNestedScrollingEnabled(false);
        //显示下拉刷新
        mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置上拉刷新文字
        mPullLoadMoreRecyclerView.setFooterViewText("加载ing");

        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        //设置条目动画
        //mPullLoadMoreRecyclerView.setItemAnimator( new DefaultItemAnimator());
        //绑定滑动监听
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        //绑定适配器
        mRecyclerViewAdapter = new DiscuzzAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new DiscuzzAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent1 = new Intent(Nosad_DetailActivity.this,no_mail_detailActivity.class);
                String content = (String) ((TextView)view.findViewById(R.id.text4)).getText();
                intent1.putExtra("content",content);
                startActivity(intent1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        getData();
    }

    private void getData() {
        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("User");
        query.include("Nosad");
        //设置最多每次只查询10个
        query.setLimit(10);
        query.addWhereEqualTo("Nosad",id);
        query.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(final List<Discuss> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        //如果有数据，则加载数据
                        mRecyclerViewAdapter.addAllData(list);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }else{
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        ShowToast("没有信息了");
                    }

                }else{
                    ShowToast("请检查你的网络");
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }});

    }
    int right;
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        BmobQuery<Power> query1 = new BmobQuery<Power>();
        query1.addWhereEqualTo("User", MyUser.getCurrentUser().getObjectId());
        query1.findObjects(new FindListener<Power>() {
            @Override
            public void done(List<Power> list, BmobException e) {
                if (e==null){
                    for (Power gameScore : list) {
                        //获得playerName的信息
                        right = Integer.parseInt(gameScore.getRight());
                    }
                    if (right==6){
                        getMenuInflater().inflate(R.menu.nosad_discuzz, menu);
                    }
                }else{
                    ShowToast("获取您的权限失败");
                }
            }
        });
        return true;
    }

    @Override
    public void onRefresh() {
        BmobQuery<Discuss> query = new BmobQuery<>();
        // 查找数据
        query.setLimit(1);
        query.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(final List<Discuss> list, BmobException e) {
                if (e == null) {
                    //如果能联网则
                    setRefresh();
                    getData();

                }else{
                    ShowToast("请检查你的网络");
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }});
    }

    @Override
    public void onLoadMore() {
        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("User,Nosad");
        // 查找数据
        query.setLimit(10);
        query.setSkip(10*mCount);
        query.addWhereEqualTo("Nosad",id);
        query.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(final List<Discuss> list, BmobException e) {
                if (e == null) {

                    if (list.size() > 0) {
                        mRecyclerViewAdapter.addAllData(list);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    }else{
                        ShowToast("已无更多数据");
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }else{
                    ShowToast("请检查你的网络");
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }});
        mCount = mCount + 1;
    }

    //刷新时要清除数据并且设置页数为1
    private void setRefresh() {
        mRecyclerViewAdapter.clearData();
        mCount = 1;
    }
}
