package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.NosadListAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Nosad;
import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//解忧杂货店我的信
public class Nosad_MyMailActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private NosadListAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nosad__my_mail);
        //注册mPullLoadMoreRecyclerView
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_nosad_mymail);//toolbar
        toolbar.setTitle("我写过的信");//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullLoadMoreRecyclerView = findViewById(R.id.pullLoadMoreRecyclerView_nosad_mymail);
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
        mPullLoadMoreRecyclerView.setGridLayout(2);
        //设置条目动画
        //mPullLoadMoreRecyclerView.setItemAnimator( new DefaultItemAnimator());
        //绑定滑动监听
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(Nosad_MyMailActivity.this);
        //绑定适配器
        mRecyclerViewAdapter = new NosadListAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new NosadListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Nosad_MyMailActivity.this, Nosad_DetailActivity.class);
                String id  =(String) ((TextView)view.findViewById(R.id.mail_id)).getText();
                intent.putExtra("id",id);
                Log.e("nosad id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        getData();
    }

    //获得数据
    private void getData() {
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("User");
        query.addWhereEqualTo("User", MyUser.getCurrentUser().getObjectId());
        //设置最多每次只查询10个
        query.setLimit(10);
        query.findObjects(new FindListener<Nosad>() {
            @Override
            public void done(final List<Nosad> list, BmobException e) {
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

    }

    @Override
    public void onLoadMore() {

    }
}
