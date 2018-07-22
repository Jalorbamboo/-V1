package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.AdapterControl.NosadListAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Nosad;
import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//解忧管理员的类
public class Nosad_managerActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
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
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(Nosad_managerActivity.this);
        //绑定适配器
        mRecyclerViewAdapter = new NosadListAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new NosadListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Nosad_managerActivity.this, Nosad_DetailActivity.class);
                String id = (String) ((TextView) view.findViewById(R.id.mail_id)).getText();
                intent.putExtra("id", id);
                Log.e("nosad id", id);
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

                    } else {
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        ShowToast("没有信息了");
                    }

                } else {
                    ShowToast("请检查你的网络");
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 查找数据
        query.setLimit(1);
        query.include("User");
        query.findObjects(new FindListener<Nosad>() {
            @Override
            public void done(final List<Nosad> list, BmobException e) {
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
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("User");
        // 查找数据
        query.setLimit(10);
        query.setSkip(10*mCount);
        query.findObjects(new FindListener<Nosad>() {
            @Override
            public void done(final List<Nosad> list, BmobException e) {
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
