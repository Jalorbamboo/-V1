package cn.fsyz.shishuo.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Bsender;
import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.LoginActivity;
import com.fsyz.shishuo.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BsenderJudgeActivity extends BaseActivity {

    //批量删除评论
    List<BmobObject> dis = new ArrayList<BmobObject>();
    TextView comment,title;
    Button button;
    ImageView imageView,pic;
    EditText editText;
    String name,commentname,UID,id,right;

    private BGANinePhotoLayout mCurrentClickNpl;
    private static final int PRC_PHOTO_PREVIEW = 6;
    TextView like_number;
    LinearLayout mLinearLayout;
    AgentWeb mAgentWeb;
    String Bgm;
    private static final int PROCESS2 = 2;
    private static final int PROCESS1 = 1;
    private static final int dismiss1 = 4;
    private static final int dismiss2 = 5;
    ProgressDialog progressDialog1,progressDialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(BsenderJudgeActivity.this);
                    progressDialog1.setTitle("发送中，请耐心等待");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                    break;
                case PROCESS1:
                    progressDialog = new ProgressDialog(BsenderJudgeActivity.this);
                    progressDialog.setTitle("删除中，请耐心等待");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    break;
                case dismiss1:
                    progressDialog1.dismiss();
                    break;
                case dismiss2:
                    progressDialog.dismiss();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bsender_judge);

        final WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        final Intent intent = getIntent();
        id  = intent.getStringExtra("id_bs");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bsender_judge);//toolbar
        toolbar.setTitle("收到投稿");//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        final String username = (String) MyUser.getObjectByKey("objectId");
        BmobQuery<Power> query1 = new BmobQuery<Power>();
        query1.addWhereEqualTo("User",username);
        query1.findObjects(new FindListener<Power>() {
            @Override
            public void done(List<Power> list, BmobException e) {
                if (e==null){
                    for (Power gameScore : list) {
                        //获得playerName的信息
                        right = gameScore.getRight();
                    }
                }else{
                    ShowToast("获取您的权限失败");
                }
            }
        });
        title = (TextView) findViewById(R.id.author_bsender_judge);
        comment = findViewById(R.id.comment_bsender_judge);
        editText = (EditText)findViewById(R.id.content_bsender_judge);
        button = (Button)findViewById(R.id.send_bsender_judge);
        BmobQuery<Bsender> query = new BmobQuery<Bsender>();
        query.include("User");
        query.getObject(id, new QueryListener<Bsender>() {
            @Override
            public void done(Bsender bsender, BmobException e) {
                if (e==null){
                    title.setText(""+bsender.getUser().getSchool()+"/"+bsender.getUser().getGrade()+"/"+bsender.getUser().getClasses()+"/"+bsender.getUser().getRealname());
                    comment.setText(""+bsender.getContent());
                }else{

                }
            }
        });

        toolbar.inflateMenu(R.menu.detail_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                        handler.sendEmptyMessage(PROCESS1);
                        Bsender gameScore = new Bsender();
                        gameScore.setObjectId(id);
                        gameScore.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("bmob","成功");
                                    ShowToast("文本删除成功");
                                    //删除评论
                                    deletecomment();
                                }else{
                                    handler.sendEmptyMessage(dismiss2);
                                    ShowToast("遭遇挫折"+e.getMessage());
                                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(PROCESS2);
                final String c = editText.getText().toString();
                if (c.contentEquals("")){
                    ShowToast("请输入回复");
                    handler.sendEmptyMessage(dismiss1);
                    return;
                }
                final BmobUser user = BmobUser.getCurrentUser();
                if (user!=null){
                    final String username = (String) MyUser.getObjectByKey("objectId");
                    BmobQuery<Power> query = new BmobQuery<Power>();
                    query.addWhereEqualTo("User",username);
                    query.findObjects(new FindListener<Power>() {
                        @Override
                        public void done(List<Power> list, BmobException e) {
                            if (e==null){
                                for (Power gameScore : list) {
                                    //获得playerName的信息
                                    right = gameScore.getRight();
                                }
                                if (right.contentEquals("2")){
                                    ShowToast("你太调皮，正在被惩罚中，不能发言哦！");
                                    handler.sendEmptyMessage(dismiss1);
                                }else{
                                    Bsender person = new Bsender();
                                    person.setObjectId(id);
                                    final Discuss comment = new Discuss();
                                    comment.setDiscuss(c);
                                    comment.setBsender(person);
                                    comment.setBmobUser(user);
                                    comment.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId,BmobException e) {
                                            if(e==null){
                                                    Log.e("p","==null");
                                                    ShowToast("评论发表成功");
                                                    handler.sendEmptyMessage(dismiss1);
                                                    editText.setText("");
                                                    getHost(id);
                                            }else{
                                                handler.sendEmptyMessage(dismiss1);
                                                ShowToast(""+e.getMessage());
                                                Log.e("bmob","失败："+e.getMessage());
                                            }
                                        }

                                    });
                                }
                            }else{
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("获取信息失败");
                            }
                        }
                    });
                }else{
                    Intent intent1 = new Intent(BsenderJudgeActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
            }
        });
        imageView = findViewById(R.id.discuzz_bsender_judge);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        Bitmap bitmap2 = readBitMap(this, R.mipmap.chat);
        imageView.setImageBitmap(bitmap2);


    }

    private void deletecomment() {
        BmobQuery<Discuss> discussBmobQuery = new BmobQuery<>();
        discussBmobQuery.addWhereEqualTo("Bsender",id);
        discussBmobQuery.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(List<Discuss> list, BmobException e) {
                if (e==null){
                    for (Discuss gameScore : list) {
                        //获得数据的objectId信息
                        Discuss p1 = new Discuss();
                        p1.setObjectId(gameScore.getObjectId());
                        dis.add(p1);
                    }
                    new BmobBatch().deleteBatch(dis).doBatch(new QueryListListener<BatchResult>() {

                        @Override
                        public void done(List<BatchResult> o, BmobException e) {
                            if(e==null){
                                for(int i=0;i<o.size();i++){
                                    BatchResult result = o.get(i);
                                    BmobException ex =result.getError();
                                    if(ex==null){
                                        Log.e("delete","第"+i+"个数据批量删除成功");
                                    }else{
                                        Log.e("delete","第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                                    }
                                }
                                handler.sendEmptyMessage(dismiss2);
                            }else{
                                Log.e("delete","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }
            }
        });
    }








    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.detail_main, menu);


        return true;
    }



    private void getHost(String id) {
        BmobQuery<Bsender> query = new BmobQuery<Bsender>();
        query.include("User");
        query.getObject(id, new QueryListener<Bsender>() {
            @Override
            public void done(Bsender comment, BmobException e) {
                if (e==null) {
                    commentname = comment.getTitle();
                    name = comment.getUser().getObjectId();
                    UID = comment.getUser().getUDID();
                    String UDID = BmobInstallationManager.getInstallationId();
                    if (UID.contentEquals(""+UDID)){
                        return;
                    }
                    sendFeekBack();
                }else{
                    ShowToast(""+e.getMessage());
                }
            }
        });
    }



    private void sendFeekBack() {
        BmobPushManager bmobPushManager = new BmobPushManager();
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        Log.e("tag3",""+name);
        query.addWhereEqualTo("User",name);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage("【诗】题为"+commentname+"有新的回复", new PushListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ShowToast("已通知【诗】主");
                } else {
                    //ShowToast("异常：" + e.getMessage());
                    Log.e("异常",""+e.getMessage());
                }
            }
        });
    }



    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }




    private void showBottomSheetDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,null);

        handleList(view);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void handleList(View contentView){

        Intent intent = getIntent();
        final String id  = intent.getStringExtra("id_bs");

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        final MusicAdapter adapter = new MusicAdapter();
        recyclerView.setAdapter(adapter);
        final MyUser myUser = MyUser.getCurrentUser(MyUser.class);
        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String t = (String) ((TextView)view.findViewById(R.id.author_2)).getText();
                String i = (String) ((TextView)view.findViewById(R.id.id_2)).getText();
                if (myUser.getObjectId().contentEquals(i)){
                    ShowToast("自己不能评论自己");
                    return;
                }
                ShowToast("请返回输入框评论");
                editText.setText("@"+t+"");

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        Bsender comment = new Bsender();
        Log.e("???",""+id);
        comment.setObjectId(id);
        query.addWhereEqualTo("Bsender",new BmobPointer(comment));
        query.order("-createdAt");
        query.include("bmobUser");
        // 查找数据
        query.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(final List<Discuss> list, BmobException e) {
                if (e == null) {


                    if (list.size() > 0) {


                        adapter.setData(list);
                        adapter.notifyDataSetChanged();

                    }else{


                    }

                }
            }});



    }






    public static class MusicAdapter extends RecyclerView.Adapter{
        private List<Discuss> mData;

        private MusicAdapter.OnItemClickListener onItemClickListener;//设置点击事件
        public void setData(List<Discuss> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MusicAdapter.MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reclycle_view_item_2,null));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            MusicAdapter.MusicViewHolder holder1 = (MusicAdapter.MusicViewHolder) holder;

            Discuss feedback = mData.get(position);

            holder1.author.setText(feedback.getBmobUser().getUsername());
            holder1.content.setText(feedback.getDiscuss());
            holder1.time.setText(feedback.getCreatedAt());
            holder1.id.setText(feedback.getBmobUser().getObjectId());
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    }
                }
            });

            holder1.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, pos);
                    }
                    //表示此事件已经消费，不会触发单击事件
                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0:mData.size();
        }



        public static class MusicViewHolder extends RecyclerView.ViewHolder{
            public TextView title;
            public TextView author;
            public TextView content;
            public TextView time,id;

            MusicViewHolder(View itemView) {
                super(itemView);
                author = (TextView)itemView.findViewById(R.id.author_2);
                content = (TextView)itemView.findViewById(R.id.text_2);
                time = itemView.findViewById(R.id.tv_time_2);
                id = itemView.findViewById(R.id.id_2);
            }
        }

        void setOnItemClickListener(MusicAdapter.OnItemClickListener listener) {
            this.onItemClickListener =  listener;
        }

        //返回点击事件
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

    }

    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片  
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);

    }




    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mAgentWeb!=null){
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        // CleanMessageUtil.clearAllCache(getApplicationContext());
        super.onDestroy();
    }
}