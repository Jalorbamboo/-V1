package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.TalkAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Talk;

import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TalklistActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    //周讨论的id
    String id;
    //内容
    TextView title,questions;
    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private TalkAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_talklist);

        title = findViewById(R.id.title_talk_list);
        questions = findViewById(R.id.content_talk_list);

        title.setText(getIntent().getStringExtra("title"));
        questions.setText(getIntent().getStringExtra("comment"));
        id = getIntent().getStringExtra("id");
        //Log.e("id",id);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView_talk_list);
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
        mRecyclerViewAdapter = new TalkAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        //注册点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new TalkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TalklistActivity.this, TalkDetailActivity.class);
                try {
                    String c = (String) ((TextView) view.findViewById(R.id.text1)).getText();
                    String id = (String) ((TextView) view.findViewById(R.id.id1)).getText();
                    String top = (String) ((TextView) view.findViewById(R.id.title1)).getText();
                    intent.putExtra("comment", c);
                    intent.putExtra("title", top);
                    intent.putExtra("id", id);
                    Log.e("详情i的",""+id);
                }catch (Exception e){
                    String c = (String) ((TextView) view.findViewById(R.id.text_talkopen_w)).getText();
                    String id = (String) ((TextView) view.findViewById(R.id.id_talkopen_w)).getText();
                    String top = (String) ((TextView) view.findViewById(R.id.title_talkopen_w)).getText();
                    intent.putExtra("comment", c);
                    intent.putExtra("title", top);
                    intent.putExtra("id", id);
                    Log.e("详情i的2",""+id);
                }
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        getData();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_talk_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser bmobUser = BmobUser.getCurrentUser() ;
                if (bmobUser!=null){
                    Intent intent = new Intent(TalklistActivity.this,TalkActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "你使用的是非官方软件，请注意", Snackbar.LENGTH_LONG)
                            .setAction("null", null).show();
                }
            }
        });
    }

    //获得数据
    private void getData() {
        BmobQuery<Talk> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("User");
        query.include("TalkTitle");
        //设置最多每次只查询10个
        query.setLimit(10);
        query.addWhereEqualTo("TalkTitle",id);
        query.findObjects(new FindListener<Talk>() {
            @Override
            public void done(final List<Talk> list, BmobException e) {
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


    // 刷新操作
    @Override
    public void onRefresh() {
        BmobQuery<Talk> query = new BmobQuery<>();
        // 查找数据
        query.setLimit(1);
        query.findObjects(new FindListener<Talk>() {
            @Override
            public void done(final List<Talk> list, BmobException e) {
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

    //加载更多
    @Override
    public void onLoadMore() {
        BmobQuery<Talk> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("User");
        // 查找数据
        query.include("TalkTitle");
        //设置最多每次只查询10个
        query.setLimit(10);
        query.addWhereEqualTo("TalkTitle",id);
        query.setSkip(10*mCount);
        query.findObjects(new FindListener<Talk>() {
            @Override
            public void done(final List<Talk> list, BmobException e) {
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
