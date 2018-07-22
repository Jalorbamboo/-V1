package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.HuodongAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Huodong;
import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HuodongActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {


    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private HuodongAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_huodong);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_huodong);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("活动一下·【诗】意生活");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //注册mPullLoadMoreRecyclerView
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView_huodong);
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
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);
        //设置条目动画
        mPullLoadMoreRecyclerView.setItemAnimator( new DefaultItemAnimator());
        //绑定滑动监听
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        //绑定适配器
        mRecyclerViewAdapter = new HuodongAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        getDate();
        mRecyclerViewAdapter.setOnItemClickListener(new HuodongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HuodongActivity.this,Huodong_detailActivity.class);

                    String c = (String) ((TextView)view.findViewById(R.id.text5)).getText();
                    String t = (String) ((TextView)view.findViewById(R.id.author5)).getText();
                    String id  =(String) ((TextView)view.findViewById(R.id.id5)).getText();
                    String top = (String) ((TextView)view.findViewById(R.id.title5)).getText();
                    String url  =(String) ((TextView)view.findViewById(R.id.url5)).getText();
                    intent.putExtra("comment",c);
                    intent.putExtra("author",t);
                    intent.putExtra("id",id);
                    intent.putExtra("top",top);
                    intent.putExtra("url",url);


                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void getDate() {
        BmobQuery<Huodong> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("User");
        //设置最多每次只查询10个
        query.setLimit(10);
        query.findObjects(new FindListener<Huodong>() {
            @Override
            public void done(final List<Huodong> list, BmobException e) {
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


    @Override
    public void onRefresh() {
        BmobQuery<Huodong> query = new BmobQuery<>();
        // 查找数据
        query.setLimit(1);
        query.findObjects(new FindListener<Huodong>() {
            @Override
            public void done(final List<Huodong> list, BmobException e) {
                if (e == null) {
                    //如果能联网则
                    setRefresh();
                    getDate();
                }else{
                    ShowToast("请检查你的网络");
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }});
    }

    private void setRefresh() {
        mRecyclerViewAdapter.clearData();
        mCount = 1;
    }

    @Override
    public void onLoadMore() {
        BmobQuery<Huodong> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("User");
        // 查找数据
        query.setLimit(10);
        query.setSkip(10*mCount);
        query.findObjects(new FindListener<Huodong>() {
            @Override
            public void done(final List<Huodong> list, BmobException e) {
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
}
