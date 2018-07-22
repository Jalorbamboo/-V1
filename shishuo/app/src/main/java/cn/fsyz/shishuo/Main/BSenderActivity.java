package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import cn.fsyz.shishuo.AdapterControl.BsenderOpenAdapter;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.BsenderTitle;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import com.fsyz.shishuo.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BSenderActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private BsenderOpenAdapter mRecyclerViewAdapter_h;
    private int mCount = 1;
    RefreshLayout refreshLayout;
    int right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //复用活动界面
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bsender);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bsender);//toolbar
        //setSupportActionBar(toolbar);//toolbar
        //getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("佛一宿舍广播站投稿");//toolbar
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
                }else{
                    ShowToast("获取您的权限失败");
                }
            }
        });
        mRecyclerView = findViewById(R.id.recycler_view_bsender);
        //绑定适配器
        mRecyclerViewAdapter_h = new BsenderOpenAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerViewAdapter_h);
        mRecyclerViewAdapter_h.setOnItemClickListener(new BsenderOpenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                if (right==7){
                intent = new Intent(BSenderActivity.this,BsenderlistActivity.class);
                }
                else{
                    intent = new Intent(BSenderActivity.this,BsenderWriteActivity.class);
                }
                try{
                    String c = (String) ((TextView)view.findViewById(R.id.text1)).getText();
                    String id  =(String) ((TextView)view.findViewById(R.id.id1)).getText();
                    String top = (String) ((TextView)view.findViewById(R.id.title1)).getText();
                    intent.putExtra("comment",c);
                    intent.putExtra("title",top);
                    intent.putExtra("id_Bs",id);
                    //Log.e("talktitle id",id);
                }catch (Exception e){
                    String c = (String) ((TextView)view.findViewById(R.id.text_talkopen_w)).getText();
                    String id  =(String) ((TextView)view.findViewById(R.id.id_talkopen_w)).getText();
                    String top = (String) ((TextView)view.findViewById(R.id.title_talkopen_w)).getText();
                    intent.putExtra("comment",c);
                    intent.putExtra("title",top);
                    intent.putExtra("id_Bs",id);
                    //Log.e("talktitle id",id);
                    e.getStackTrace();
                }
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout_bsender);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                BmobQuery<BsenderTitle> query = new BmobQuery<>();
                // 按时间降序查询
                query.order("-createdAt");
                query.include("User");
                // 查找数据
                query.setLimit(10);
                query.setSkip(10*mCount);
                query.findObjects(new FindListener<BsenderTitle>() {
                    @Override
                    public void done(final List<BsenderTitle> list, BmobException e) {
                        if (e == null) {

                            if (list.size() > 0) {
                                mRecyclerViewAdapter_h.addAllData(list);
                                refreshLayout.finishLoadMore(/*,false*/);//传入false表示刷新失败

                            }else{
                                ShowToast("已无更多数据");
                                refreshLayout.finishLoadMore(false);
                            }
                        }else{
                            ShowToast("请检查你的网络");
                            refreshLayout.finishLoadMore(false);
                        }
                    }});
                mCount = mCount + 1;
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mRecyclerViewAdapter_h.clearData();
                mCount = 1;
                BmobQuery<BsenderTitle> query = new BmobQuery<>();
                // 按时间降序查询
                query.order("-createdAt");
                //包括bmobUser这个关联列
                query.include("User");
                //设置最多每次只查询10个
                query.setLimit(10);
                query.findObjects(new FindListener<BsenderTitle>() {
                    @Override
                    public void done(final List<BsenderTitle> list, BmobException e) {
                        if (e == null) {
                            if (list.size() > 0) {
                                //如果有数据，则加载数据
                                mRecyclerViewAdapter_h.addAllData(list);
                                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                            }else{
                                refreshLayout.finishRefresh(false/*,false*/);//传入false表示刷新失败
                                ShowToast("没有信息了");
                            }
                        }else{
                            ShowToast("请检查你的网络");
                            refreshLayout.finishRefresh(false/*,false*/);//传入false表示刷新失败
                        }
                    }});
            }
        });
    }
}
