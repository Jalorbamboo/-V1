package cn.fsyz.shishuo.Main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import cn.fsyz.shishuo.AdapterControl.NumberAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Huodong;
import cn.fsyz.shishuo.Bean.MyUser;

import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//参与人员
public class JoinNumberActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private NumberAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;
    //id值
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_number);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView_join_number);
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
        mRecyclerViewAdapter = new NumberAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        id = getIntent().getStringExtra("id");
        getnumber();
    }

    private void getnumber() {
        BmobQuery<MyUser>  query  = new BmobQuery<>();
        Huodong post = new Huodong();
        post.setObjectId(id);
        // TODO 不友好
        query.addWhereRelatedTo("participant",new BmobPointer(post));

        query.findObjects(new FindListener<MyUser>() {

            @Override

            public void done(List<MyUser> list, BmobException e) {

                if (e == null) {

                    Log.e("join","ok " + list.size());
                    mRecyclerViewAdapter.addAllData(list);
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                } else {

                    Log.e("join", e.toString());

                }

            }

        });
    }

    @Override
    public void onRefresh() {
        
    }

    @Override
    public void onLoadMore() {

    }
}
