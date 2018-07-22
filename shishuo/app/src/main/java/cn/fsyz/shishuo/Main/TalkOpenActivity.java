package cn.fsyz.shishuo.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.fsyz.shishuo.ActivityCollector;
import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.Bean.TalkTitle;
import cn.fsyz.shishuo.LoginActivity;
import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//管理员发送话题
public class TalkOpenActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,BGASortableNinePhotoLayout.Delegate {

    Button button;
    EditText content;
    EditText bgm;
    EditText title;
    String photo_url = "null";//判断有无照片
    String id  = null;//获得【诗】的ID
    String right;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    public static final int FAIL = 4;
    List<String> photourl = new ArrayList<String>();
    private TalkTitle comment =new TalkTitle();
    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 5;
    private static final int RC_PHOTO_PREVIEW = 3;

    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";
    private final int PROCESS2 = 2;
    private static final int dismiss1 = 4;
    ProgressDialog progressDialog1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(TalkOpenActivity.this);
                    progressDialog1.setTitle("发送中，请耐心等待");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                    break;
                case dismiss1:
                    progressDialog1.dismiss();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handlerlist = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case FAIL:


                    ActivityCollector.finishAll();
                    Intent intent  = new Intent(TalkOpenActivity.this,LoginActivity.class);
                    startActivity(intent);
                    TalkOpenActivity.this.finish();


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fetchUserInfo();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_talk_open);
        button = (Button)findViewById(R.id.send_talk_open);
        content = (EditText)findViewById(R.id.content_talk_open);
        title = findViewById(R.id.title_talk_open);
        //imageButton = findViewById(R.id.select_photo);
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos_talk_open);
        mPhotosSnpl.setMaxItemCount(9);
        mPhotosSnpl.setEditable(true);
        mPhotosSnpl.setPlusEnable(true);
        mPhotosSnpl.setSortable(true);
        mPhotosSnpl.setDelegate(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_talk_open);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("发布本周话题");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.detail_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(PROCESS2);
                try {
                    final String c = content.getText().toString();
                    final String t = title.getText().toString();
                    if (t.contentEquals("")){
                        ShowToast("请输入题目");
                        handler.sendEmptyMessage(dismiss1);
                        return;
                    }
                    if (c.contentEquals("")){
                        ShowToast("请输入题目详情");
                        handler.sendEmptyMessage(dismiss1);
                        return;
                    }
                    final MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);

                    if(bmobUser != null){
                        // 允许用户使用应用
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
                                        TalkTitle p2 = new TalkTitle();
                                        p2.setQtitle(t);
                                        p2.setQuestions(c);
                                        p2.setTeacher(bmobUser);
                                        p2.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String objectId,BmobException e) {
                                                if(e==null){
                                                    if (photo_url.contentEquals("null")){
                                                        //Log.e("tag","没有照片");
                                                        ShowToast("发送成功");
                                                        handler.sendEmptyMessage(dismiss1);
                                                        finish();
                                                    }else{
                                                        id = objectId;
                                                        startUploadFile();
                                                    }
                                                }else{
                                                    ShowToast("发送失败：" + e.getMessage());
                                                    handler.sendEmptyMessage(dismiss1);
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
                        //缓存用户对象为空时， 可打开用户注册界面…
                        ShowToast("请注册");
                        Intent intent = new Intent(TalkOpenActivity.this,LoginActivity.class);
                        startActivity(intent);
                        handler.sendEmptyMessage(dismiss1);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(dismiss1);
                    e.printStackTrace();
                }

            }
        });
    }



    private void startUploadFile() {
        final String[] filePaths = new String[photourl.size()];
        for (int i = 0; i <photourl.size() ; i++) {
            filePaths[i] = photourl.get(i);
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                //Log.e("BmobFile","succsess1");
                if(list1.size()==filePaths.length){
                    //Log.e("BmobFile","succsess2");
                    String str1 = (String)list1.get(0);//获取第一行数据
                    comment.setObjectId(id);
                    comment.setPhotoArray(list1);
                    comment.setPhoto(str1);
                    comment.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("发送成功");
                                finish();
                            }
                        }
                    });
                }

            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                handler.sendEmptyMessage(dismiss1);
                ShowToast("错误码"+i +",错误描述："+s);
            }
        });
    }














    private void fetchUserInfo() {
        MyUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.e("fetchUserInfo","success");
                } else {
                    ShowToast("无法同步本地账户");
                    MyUser.logOut();
                    handlerlist.sendEmptyMessage(FAIL);
                    Log.e("fetchUserInfo","fail");
                }
            }
        });
    }

    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }



    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
        photourl.remove(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            photourl = BGAPhotoPickerActivity.getSelectedPhotos(data);
            photo_url = String.valueOf(BGAPhotoPickerActivity.getSelectedPhotos(data));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
            photourl = BGAPhotoPickerActivity.getSelectedPhotos(data);
            photo_url = String.valueOf(BGAPhotoPickerActivity.getSelectedPhotos(data));
        }
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            //File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }
}