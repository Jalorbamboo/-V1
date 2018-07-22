package cn.fsyz.shishuo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import cn.fsyz.shishuo.AdapterControl.MainAdapter;
import cn.fsyz.shishuo.Bean.Comment;
import cn.fsyz.shishuo.Main.MessageDetailActivity;
import com.fsyz.shishuo.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by JALOR on 2018/2/27.
 */
//诗分享
public class Fragment1 extends Fragment {


    //页数
    private int mCount = 1;
    //适配器
    private MainAdapter mRecyclerViewAdapter;
    //
    private RecyclerView mRecyclerView;
    //BANNER的注册器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_main, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_shfx);
        //绑定适配器
        mRecyclerViewAdapter = new MainAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //注册点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),MessageDetailActivity.class);
                try{
                    String c = (String) ((TextView)view.findViewById(R.id.text3)).getText();
                    String t = (String) ((TextView)view.findViewById(R.id.author3)).getText();
                    String id  =(String) ((TextView)view.findViewById(R.id.id3)).getText();
                    String top = (String) ((TextView)view.findViewById(R.id.title3)).getText();
                    String url  =(String) ((TextView)view.findViewById(R.id.url)).getText();
                    intent.putExtra("comment",c);
                    intent.putExtra("author",t);
                    intent.putExtra("id",id);
                    intent.putExtra("top",top);
                    intent.putExtra("url",url);
                }catch (Exception e){
                    String c1 = (String) ((TextView)view.findViewById(R.id.text4)).getText();
                    String t1 = (String) ((TextView)view.findViewById(R.id.author4)).getText();
                    String id1  =(String) ((TextView)view.findViewById(R.id.id4)).getText();
                    String top1 = (String) ((TextView)view.findViewById(R.id.title4)).getText();
                    intent.putExtra("comment",c1);
                    intent.putExtra("author",t1);
                    intent.putExtra("id",id1);
                    intent.putExtra("top",top1);
                    e.printStackTrace();
                }
                //进场动画
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshlayout) {
                setRefresh();
                BmobQuery<Comment> query = new BmobQuery<>();
                // 按时间降序查询
                query.order("-createdAt");
                //包括bmobUser这个关联列
                query.include("bmobUser");
                //设置最多每次只查询10个
                query.setLimit(10);
                query.findObjects(new FindListener<Comment>() {
                    @Override
                    public void done(final List<Comment> list, BmobException e) {
                        if (e == null) {
                            if (list.size() > 0) {
                                //如果有数据，则加载数据
                                mRecyclerViewAdapter.addAllData(list);
                                //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败

                            }else{
                                //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                ShowToast("没有信息了");
                                refreshlayout.finishRefresh(false/*,false*/);//传入false表示刷新失败
                            }

                        }else{
                            ShowToast("请检查你的网络");
                            //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                            refreshlayout.finishRefresh(false/*,false*/);//传入false表示刷新失败
                        }
                    }});

                snedBroad();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshlayout) {
                BmobQuery<Comment> query = new BmobQuery<>();
                // 按时间降序查询
                query.order("-createdAt");
                query.include("bmobUser");
                // 查找数据
                query.setLimit(10);
                query.setSkip(10*mCount);
                query.findObjects(new FindListener<Comment>() {
                    @Override
                    public void done(final List<Comment> list, BmobException e) {
                        if (e == null) {

                            if (list.size() > 0) {
                                mRecyclerViewAdapter.addAllData(list);
                                //mRecyclerView.setAdapter(mRecyclerViewAdapter);
                                //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                refreshlayout.finishLoadMore();//传入false表示加载失败
                            }else{
                                ShowToast("已无更多数据");
                                //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                refreshlayout.finishLoadMore(false);//传入false表示加载失败
                            }
                        }else{
                            ShowToast("请检查你的网络");
                            //mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                            refreshlayout.finishLoadMore(false);//传入false表示加载失败
                        }
                    }});
                mCount = mCount + 1;
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();
        return view;
    }

    //获得数据
    private void getData() {
        BmobQuery<Comment> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        //包括bmobUser这个关联列
        query.include("bmobUser");
        //设置最多每次只查询10个
        query.setLimit(10);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(final List<Comment> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        //如果有数据，则加载数据
                        mRecyclerViewAdapter.addAllData(list);
                    }else{

                        ShowToast("没有信息了");
                    }

                }else{
                    ShowToast("请检查你的网络");
                }
            }});

    }



    private void snedBroad() {
        Intent intent = new Intent("com.sunshinecompany.shishuo.broadcasttest.FRESH_BROADCAST");
        getActivity().sendBroadcast(intent);
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
