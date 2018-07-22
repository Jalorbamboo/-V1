package cn.fsyz.shishuo.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
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
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebSettings;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Comment;
import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.Bean.Talk;
import cn.fsyz.shishuo.LoginActivity;
import com.fsyz.shishuo.R;
import cn.fsyz.shishuo.Utils.CircleTransform;

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

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.dp2px;

//话题发表人详情
public class TalkDetailActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks,BGANinePhotoLayout.Delegate {

    //批量删除评论
    List<BmobObject> dis = new ArrayList<BmobObject>();
    TextView comment,title;
    Button button;
    LikeButton imageViewlike;
    BGABadgeImageView imageView,pic;
    EditText editText;
    String name,commentname,UID,id,right;
    List<String>photourl = new ArrayList<>();
    private BGANinePhotoLayout mCurrentClickNpl;
    private static final int PRC_PHOTO_PREVIEW = 6;
    String p ;
    //判断是否图片加载完成
    String j;
    //点赞人数
    String a;
    //作者
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
                    progressDialog1 = new ProgressDialog(TalkDetailActivity.this);
                    progressDialog1.setTitle("发送中，请耐心等待");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                    break;
                case PROCESS1:
                    progressDialog = new ProgressDialog(TalkDetailActivity.this);
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
        setContentView(R.layout.activity_message_detail);
        final WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mCurrentClickNpl = findViewById(R.id.npl_item_moment_photos);
        title = (TextView) findViewById(R.id.detail_author);
        pic = findViewById(R.id.m_pic_imageView);
        final Intent intent = getIntent();
        String c = intent.getStringExtra("comment");
        String to = intent.getStringExtra("top");
        //final String a = intent.getStringExtra("author");
        id  = intent.getStringExtra("id");
        BmobQuery<Talk> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.include("User");
        commentBmobQuery.getObject(id, new QueryListener<Talk>() {
            @Override
            public void done(Talk comment, BmobException e) {
                if (e==null){
                    Picasso.with(TalkDetailActivity.this).load(comment.getUser().getUserpic_url()).transform(new CircleTransform()).config(Bitmap.Config.RGB_565)
                            .resize(dp2px(100),dp2px(100)).centerCrop().into(pic);
                    title.setText("作者：" + comment.getUser().getUsername());
                }
            }
        });
        getPhoto();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_message_detail);//toolbar
        toolbar.setTitle("");//toolbar
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
        toolbar.inflateMenu(R.menu.detail_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                        handler.sendEmptyMessage(PROCESS1);
                        Talk gameScore = new Talk();
                        gameScore.setObjectId(id);
                        gameScore.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("bmob","成功");
                                    ShowToast("文本删除成功");
                                    delete();
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

        initView();
        comment = findViewById(R.id.detail_comment);
        //imageView2 = findViewById(R.id.content_image_main);
        editText = (EditText)findViewById(R.id.content);
        button = (Button)findViewById(R.id.send_content);
        imageViewlike = findViewById(R.id.like_button);
        mLinearLayout = findViewById(R.id.bgm);
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();
        //name = a;
        commentname = to;
        comment.setText(c);

        like_number = findViewById(R.id.likes_member_number);
        //查询人
        BmobQuery<MyUser>  query_m  = new BmobQuery<>();
        //当前的Comment
        Talk post = new Talk();
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
                Talk comment = new Talk();
                comment.setObjectId(id);
                if (j==null){
                    ShowToast("请等待~努力链接控制台，请等3秒再尝试！");
                    return;
                }
                comment.setPhotoArray(photourl);
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
                                    Talk person = new Talk();
                                    person.setObjectId(id);
                                    final Discuss comment = new Discuss();
                                    comment.setDiscuss(c);
                                    comment.setTalk(person);
                                    comment.setBmobUser(user);
                                    comment.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId,BmobException e) {
                                            if(e==null){
                                                if (p==null){
                                                    Log.e("p","==null");
                                                    ShowToast("评论发表成功");
                                                    handler.sendEmptyMessage(dismiss1);
                                                    editText.setText("");
                                                    getHost(id);
                                                }else{
                                                    Log.e("p","!=null");
                                                    ShowToast("评论发表成功");
                                                    handler.sendEmptyMessage(dismiss1);
                                                    editText.setText("");
                                                    getpHost(p);
                                                }
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
                    Intent intent1 = new Intent(TalkDetailActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
            }
        });
        imageView = findViewById(R.id.discuzz);
        Bitmap bitmap2 = readBitMap(this,R.mipmap.chat);
        imageView.setImageBitmap(bitmap2);
        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        Talk comment = new Talk();
        comment.setObjectId(id);
        query.addWhereEqualTo("Talk",new BmobPointer(comment));
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

    private void deletecomment() {
        BmobQuery<Discuss> discussBmobQuery = new BmobQuery<>();
        discussBmobQuery.addWhereEqualTo("Talk",id);
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


    //加载网页音乐
    private void openbgm() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Comment>() {
            @Override
            public void done(Comment comment, BmobException e) {
                if (e==null){
                    Bgm = comment.getBgm();
                    if (Bgm.contentEquals("")){
                        ShowToast("放心，【诗】主没有放置音乐");
                    }else{
                        //getWindow().getDecorView是获得最外层的View
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            //消去底部导航键
                            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏虚拟按键栏
                            //        | View.SYSTEM_UI_FLAG_IMMERSIVE //防止点击屏幕时,隐藏虚拟按键栏又弹了出来
                            //);
                            View view = findViewById(R.id.message_detail_card);
                            Snackbar snackBar =Snackbar.make(view,"有背景音乐，客官要听听吗？(可能有点费流量哦！)",Snackbar.LENGTH_LONG);
                            snackBar.setAction("确定", new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                @Override
                                public void onClick(View v) {
                                    mAgentWeb = AgentWeb.with(TalkDetailActivity.this)//
                                            .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//
                                            .useDefaultIndicator()
                                            .defaultProgressBarColor()
                                            .createAgentWeb()//
                                            .ready()
                                            .go(Bgm);
                                    AgentWebSettings agentWebSettings = mAgentWeb.getAgentWebSettings();
                                    ShowToast("前方高能！BGM来袭~");
                                    agentWebSettings.getWebSettings().setMediaPlaybackRequiresUserGesture(false);
                                    agentWebSettings.getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                                    //加载底部导航键
                                    //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                }
                            }).show();

                        }
                    }
                }
            }
        });
    }

    private void getPhoto() {
        BmobQuery<Talk> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Talk>() {
            @Override
            public void done(Talk comment, BmobException e) {
                if (e==null){
                    photourl = comment.getPhotoArray();
                    mCurrentClickNpl.setDelegate(TalkDetailActivity.this);
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


    private void delete() {
        final String[] filePaths = new String[photourl.size()];
        if (photourl.size()==0){
            Log.e("hi","this no file");
            handler.sendEmptyMessage(dismiss2);
            finish();
            return;
        }
        for (int i = 0; i <photourl.size() ; i++) {
            //filePaths[i] = photourl.get(i);
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
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        BmobQuery<Talk> query = new BmobQuery<Talk>();
        query.include("User");
        query.getObject(id, new QueryListener<Talk>() {
            @Override
            public void done(Talk comment, BmobException e) {
                if (e==null){
                    String Sname = comment.getUser().getUsername();
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

    private void getpHost(String p) {
        BmobPushManager bmobPushManager = new BmobPushManager();
        BmobQuery<BmobInstallation>query = BmobInstallation.getQuery();
        query.addWhereEqualTo("User",p);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage("你在【诗】题为"+commentname+"的评论有新的回复", new PushListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ShowToast("已通知评论的【诗】主");
                } else {
                    //ShowToast("异常：" + e.getMessage());
                    Log.e("异常",""+e.getMessage());
                }
            }
        });
    }

    private void getHost(String id) {
        BmobQuery<Talk> query = new BmobQuery<Talk>();
        query.include("User");
        query.getObject(id, new QueryListener<Talk>() {
            @Override
            public void done(Talk comment, BmobException e) {
                if (e==null) {
                    commentname = comment.getContent();
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
        BmobQuery<BmobInstallation>query = BmobInstallation.getQuery();
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

    private void initView(){


        findViewById(R.id.discuzz).setOnClickListener(this);
    }

    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.discuzz){
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
        final MessageDetailActivity.MusicAdapter adapter = new MessageDetailActivity.MusicAdapter();
        recyclerView.setAdapter(adapter);
        final MyUser myUser = MyUser.getCurrentUser(MyUser.class);
        adapter.setOnItemClickListener(new MessageDetailActivity.MusicAdapter.OnItemClickListener() {
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
                p = i;
                Log.e("p",""+p);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        BmobQuery<Discuss> query = new BmobQuery<>();
        // 按时间降序查询
        Talk comment = new Talk();
        comment.setObjectId(id);
        query.addWhereEqualTo("Talk",new BmobPointer(comment));
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

        private MessageDetailActivity.MusicAdapter.OnItemClickListener onItemClickListener;//设置点击事件
        public void setData(List<Discuss> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MessageDetailActivity.MusicAdapter.MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reclycle_view_item_2,null));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            MessageDetailActivity.MusicAdapter.MusicViewHolder holder1 = (MessageDetailActivity.MusicAdapter.MusicViewHolder) holder;

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

        void setOnItemClickListener(MessageDetailActivity.MusicAdapter.OnItemClickListener listener) {
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