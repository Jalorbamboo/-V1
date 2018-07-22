package cn.fsyz.shishuo.fragment;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.fsyz.shishuo.AdapterControl.NosadListAdapter;
import cn.fsyz.shishuo.Bean.Nosad;
import cn.fsyz.shishuo.Main.Nosad_DetailActivity;

import com.fsyz.shishuo.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by JALOR on 2018/2/27.
 */
//解忧杂货店
public class Fragment3 extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    //注册ReclycleView
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    //页数
    private int mCount = 1;
    //适配器
    private NosadListAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;
    //BANNER的注册器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_two, container, false);
        //注册mPullLoadMoreRecyclerView
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)view.findViewById(R.id.pullLoadMoreRecyclerView_nosad);
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
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        //绑定适配器
        mRecyclerViewAdapter = new NosadListAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new NosadListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Nosad_DetailActivity.class);
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
        return view;
    }

    //获得数据
    private void getData() {
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("User");
        query.addWhereEqualTo("publicorno",true);
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


    // 刷新操作
    @Override
    public void onRefresh() {
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 查找数据
        query.setLimit(1);
        query.addWhereEqualTo("publicorno",true);
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

    //加载更多
    @Override
    public void onLoadMore() {
        BmobQuery<Nosad> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("User");
        query.addWhereEqualTo("publicorno",true);
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

    Toast mToast;

    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }

    }
}
