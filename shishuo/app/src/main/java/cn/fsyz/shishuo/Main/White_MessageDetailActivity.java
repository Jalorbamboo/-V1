package cn.fsyz.shishuo.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.Bean.WhiteComment;
import cn.fsyz.shishuo.LoginActivity;

import com.fsyz.shishuo.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
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
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pub.devrel.easypermissions.EasyPermissions;

public class White_MessageDetailActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks,BGANinePhotoLayout.Delegate {

    //批量删除评论
    List<BmobObject> dis = new ArrayList<BmobObject>();
    TextView comment,title;
    Button button;
    //点赞数量
    TextView like_number;
    //显示评论数量
    BGABadgeImageView imageView;
    LikeButton imageViewlike;
    EditText editText;
    String name,commentname,UID,id,right;
    List<String>photourl = new ArrayList<>();
    private BGANinePhotoLayout mCurrentClickNpl;
    private static final int PRC_PHOTO_PREVIEW = 6;
    //判断是否图片加载完成
    String j;
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
                    progressDialog1 = new ProgressDialog(White_MessageDetailActivity.this);
                    progressDialog1.setTitle("发送中，请耐心等待");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                    break;
                case PROCESS1:
                    progressDialog = new ProgressDialog(White_MessageDetailActivity.this);
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
        setContentView(R.layout.activity_message_detail_white);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        mCurrentClickNpl = findViewById(R.id.npl_item_moment_photos_w);
        final Intent intent = getIntent();
        String c = intent.getStringExtra("comment");
        String to = intent.getStringExtra("top");
        final String a = intent.getStringExtra("author");
        id  = intent.getStringExtra("id");
        Log.e("id",""+id);
        getPhoto();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_message_detail_w);//toolbar
        toolbar.setTitle(""+to);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        title = (TextView) findViewById(R.id.detail_author_w);
        comment = findViewById(R.id.detail_comment_w);
        editText = (EditText)findViewById(R.id.content_w);
        button = (Button)findViewById(R.id.send_content_w);
        imageViewlike = findViewById(R.id.like_button_White_detail);


        title.setText("作者：匿名");
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();
        name = a;
        commentname = to;
        comment.setText(c);
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
        like_number = findViewById(R.id.likes_member_number_white_detail);
        //查询人
        BmobQuery<MyUser>  query_m  = new BmobQuery<>();
        //当前的Comment
        WhiteComment post = new WhiteComment();
        post.setObjectId(id);
        // TODO 不友好
        query_m.addWhereRelatedTo("likes",new BmobPointer(post));
        query_m.findObjects(new FindListener<MyUser>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null){
                    like_number.setText("共"+list.size()+"次点赞");
                    for (int i = 0; i <list.size() ; i++) {
                        String a = list.get(i).getObjectId();
                        if (a.contentEquals(MyUser.getCurrentUser().getObjectId())){
                            imageViewlike.setLiked(true);
                            //终止循环
                            break;
                        }
                    }
                }
            }
        });

        imageViewlike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(final LikeButton likeButton) {
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                WhiteComment comment = new WhiteComment();
                comment.setObjectId(id);
                if (j==null){
                    ShowToast("请等待~努力链接控制台，请等3秒再尝试！");
                    return;
                }
                comment.setPhotoUrl(photourl);
                //将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
                BmobRelation relation = new BmobRelation();
                //将当前用户添加到多对多关联中
                relation.add(user);
                //多对多关联指向`post`的`likes`字段
                comment.setLikes(relation);
                comment.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            likeButton.setLiked(true);
                            Log.e("bmob","多对多关联添加成功");
                        }else{
                            Log.e("bmob","失败："+e.getMessage());
                        }
                    }

                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        toolbar.inflateMenu(R.menu.detail_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                        handler.sendEmptyMessage(PROCESS1);

                                        WhiteComment gameScore = new WhiteComment();
                                        gameScore.setObjectId(id);
                                        gameScore.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    Log.e("bmob","成功");
                                                    delete();
                                                    deletecomment();
                                                    ShowToast("文本删除成功");
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(PROCESS2);
                final String c = editText.getText().toString();
                if (c.contentEquals("")){
                    ShowToast("请输入评论");
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
                                    WhiteComment whiteComment = new WhiteComment();
                                    whiteComment.setObjectId(id);
                                    final Discuss comment = new Discuss();
                                    comment.setDiscuss(c);
                                    comment.setWhitecomment(whiteComment);
                                    comment.setBmobUser(user);
                                    comment.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId,BmobException e) {
                                            if(e==null){
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
                    Intent intent1 = new Intent(White_MessageDetailActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
            }
        });

        imageView = findViewById(R.id.discuzz_w);
        Bitmap bitmap2 = readBitMap(this,R.mipmap.chat);
        imageView.setImageBitmap(bitmap2);

        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        WhiteComment comment = new WhiteComment();
        comment.setObjectId(id);
        query.addWhereEqualTo("whitecomment",new BmobPointer(comment));
        query.order("-createdAt");
        query.include("bmobUser");
        // 查找数据
        query.findObjects(new FindListener<Discuss>() {
            @Override
            public void done(final List<Discuss> list, BmobException e) {
                if (e == null) {


                    if (list.size() > 0) {

                        imageView.showTextBadge(""+list.size());


                    }

                }
            }});

    }



    //删除评论
    private void deletecomment() {
        BmobQuery<Discuss> discussBmobQuery = new BmobQuery<>();
        //查找评论列的id
        discussBmobQuery.addWhereEqualTo("whitecomment",id);
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
                            }else{
                                Log.e("delete","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }
            }
        });
    }

    private void getPhoto() {
        BmobQuery<WhiteComment> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<WhiteComment>() {
            @Override
            public void done(WhiteComment comment, BmobException e) {
                if (e==null){
                    photourl = comment.getPhotoUrl();
                    mCurrentClickNpl.setDelegate(White_MessageDetailActivity.this);
                    mCurrentClickNpl.setData((ArrayList<String>) photourl);
                    //加载完毕
                    j = "1";
                }else{
                    ShowToast("加载失败"+e.getMessage());
                    Log.e("bug",""+e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        BmobQuery<WhiteComment> query = new BmobQuery<WhiteComment>();
        query.include("bmobUser");
        query.getObject(id, new QueryListener<WhiteComment>() {
            @Override
            public void done(WhiteComment comment, BmobException e) {
                if (e==null){
                    String Sname = comment.getBmobUser().getUsername();
                    MyUser myUser = MyUser.getCurrentUser(MyUser.class);
                    String Uname = myUser.getUsername();
                    if (Uname.contentEquals(Sname)){
                        getMenuInflater().inflate(R.menu.detail_main, menu);
                    }else if (right.contentEquals("5")){
                        getMenuInflater().inflate(R.menu.detail_main, menu);
                    }
                }
            }
        });
        return true;
    }


    private void delete() {
        final String[] filePaths = new String[photourl.size()];
        if (photourl.size()==0){
            Log.e("hi","this no file");
            handler.sendEmptyMessage(dismiss2);
            finish();
            return;
        }
        for (int i = 0; i <photourl.size() ; i++) {
            filePaths[i] = photourl.get(i);
            String filei = photourl.get(i);
            BmobFile file = new BmobFile();
            file.setUrl(filei);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
            file.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        ShowToast("文件删除成功");
                    }else{
                        ShowToast("文件删除失败："+e.getErrorCode()+","+e.getMessage());
                    }
                }
            });

        }
        handler.sendEmptyMessage(dismiss2);
        finish();



        ;


    }


    private void getHost(String id) {
        BmobQuery<WhiteComment> query = new BmobQuery<WhiteComment>();
        query.include("bmobUser");
        query.getObject(id, new QueryListener<WhiteComment>() {
            @Override
            public void done(WhiteComment comment, BmobException e) {
                if (e==null) {
                    commentname = comment.getName();
                    name = comment.getBmobUser().getObjectId();
                    UID = comment.getBmobUser().getUDID();
                    String UDID = BmobInstallationManager.getInstallationId();
                    //Log.e("tag",""+UDID);
                    //Log.e("tag2",""+UID);
                    if (UID.contentEquals(""+UDID)){
                        //ShowToast("");
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
        BmobQuery<BmobInstallation>query = BmobInstallation.getQuery();
        Log.e("tag3",""+name);
        query.addWhereEqualTo("User",name);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage("你的真心话："+commentname+"有新的回复", new PushListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ShowToast("已通知主人");
                } else {
                    //ShowToast("异常：" + e.getMessage());
                    Log.e("异常",""+e.getMessage());
                }
            }
        });
    }

    private void initView(){


        findViewById(R.id.discuzz_w).setOnClickListener(this);
    }

    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.discuzz_w){
            showBottomSheetDialog();
        }
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
        final String id  = intent.getStringExtra("id");

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        final MusicAdapter adapter = new MusicAdapter();
        recyclerView.setAdapter(adapter);

        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        WhiteComment comment = new WhiteComment();
        comment.setObjectId(id);
        query.addWhereEqualTo("whitecomment",new BmobPointer(comment));
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

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    private void photoPreviewWrapper() {

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(this)
                    .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl.getCurrentClickItem());
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl.getData())
                        .currentPosition(mCurrentClickNpl.getCurrentClickItemPosition()); // 当前预览图片的索引
            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


    public static class MusicAdapter extends RecyclerView.Adapter{
        private List<Discuss> mData;

        public void setData(List<Discuss> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MessageDetailActivity.MusicAdapter.MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reclycle_view_item_2,null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageDetailActivity.MusicAdapter.MusicViewHolder holder1 = (MessageDetailActivity.MusicAdapter.MusicViewHolder) holder;

            Discuss feedback = mData.get(position);

            //holder1.author.setText("匿名");
            holder1.content.setText(feedback.getDiscuss());
            holder1.time.setText(feedback.getCreatedAt());

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0:mData.size();
        }

        public static class MusicViewHolder extends RecyclerView.ViewHolder{
            public TextView title;
            //public TextView author;
            public TextView content;
            public TextView time;

            public MusicViewHolder(View itemView) {
                super(itemView);
                //author = (TextView)itemView.findViewById(R.id.author_2);
                content = (TextView)itemView.findViewById(R.id.text_2);
                time = itemView.findViewById(R.id.tv_time_2);
            }
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


}