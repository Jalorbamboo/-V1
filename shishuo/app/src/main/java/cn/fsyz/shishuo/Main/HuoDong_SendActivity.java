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

import cn.fsyz.shishuo.BaseActivity;
import cn.fsyz.shishuo.Bean.Huodong;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
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
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//活动的举办界面
public class HuoDong_SendActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate,EasyPermissions.PermissionCallbacks {

    //发送的按钮
    Button button;
    //用户编辑的内容
    EditText content;
    //编辑的标题和bgm
    EditText title,bgm;

    String photo_url = "null";//判断有无照片
    //用户的id
    String id  = null;
    //判断用户权限
    String right;
    //九宫格控件
    private BGASortableNinePhotoLayout mPhotosSnpl;
    //图片数据链
    List<String> photourl = new ArrayList<String>();
    //加入活动的bean
    private Huodong huodong =new Huodong();
    //权限的参数
    private static final int PRC_PHOTO_PICKER = 1;
    private static final int RC_CHOOSE_PHOTO = 5;
    private static final int RC_PHOTO_PREVIEW = 3;
    //进程类的参数
    private final int PROCESS2 = 2;
    private static final int dismiss1 = 4;
    ProgressDialog progressDialog1;
    //handler的设置
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(HuoDong_SendActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //写入链接bmob
        init();
        //取消原有标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_huo_dong__send);
        //各种绑定
        button = (Button)findViewById(R.id.huodong_send);
        content = (EditText)findViewById(R.id.huodong_content);
        title = findViewById(R.id.huodong_title);
        bgm = findViewById(R.id.bgm_huodong);
        //imageButton = findViewById(R.id.select_photo);
        //设置九宫格控件
        mPhotosSnpl = findViewById(R.id.huodong_moment_add_photos);
        //设置最大选择数量
        mPhotosSnpl.setMaxItemCount(9);
        mPhotosSnpl.setEditable(true);
        //可以添加
        mPhotosSnpl.setPlusEnable(true);
        mPhotosSnpl.setSortable(true);
        mPhotosSnpl.setDelegate(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_huodong);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("【诗】·举办活动~");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //发送
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置进程条
                handler.sendEmptyMessage(PROCESS2);
                try {
                    //读取标题和内容
                    final String c = content.getText().toString();
                    final String t = title.getText().toString();
                    //如果为空
                    if (c.contentEquals("")){
                        ShowToast("请输入内容");
                        handler.sendEmptyMessage(dismiss1);
                        return;
                    }else if(t.contentEquals("")){
                        ShowToast("请输入标题");
                        handler.sendEmptyMessage(dismiss1);
                        return;
                    }
                    //读取现在的用户
                    final MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);

                    if(bmobUser != null){
                        // 读取用户名
                        final String username = (String) MyUser.getObjectByKey("objectId");
                        //查询权限表格
                        BmobQuery<Power> query = new BmobQuery<Power>();
                        //查询权限表格的用户名列
                        query.addWhereEqualTo("User",username);
                        //查找
                        query.findObjects(new FindListener<Power>() {
                            @Override
                            public void done(List<Power> list, BmobException e) {
                                if (e==null){
                                    //利用循环查出权限数字
                                    for (Power gameScore : list) {
                                        //获得playerName的信息
                                        right = gameScore.getRight();
                                    }
                                    //如果权限为无
                                    if (right.contentEquals("2")){
                                        ShowToast("你太调皮，正在被惩罚中，不能发言哦！");
                                        handler.sendEmptyMessage(dismiss1);
                                    }else{
                                        //没有权限限制则
                                        Huodong p2 = new Huodong();
                                            p2.setContent(c);
                                            p2.setTitle(t);
                                            p2.setUser(bmobUser);
                                        if (photo_url.contentEquals("null")){
                                            //没有图片
                                            ShowToast("请最起码发布一张海报！谢谢！");
                                            handler.sendEmptyMessage(dismiss1);
                                            //终止于此，有图片则跳过
                                            return;
                                        }
                                        p2.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String objectId,BmobException e) {
                                                if(e==null){
                                                    id = objectId;
                                                    startUploadFile();
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
                        Intent intent = new Intent(HuoDong_SendActivity.this,LoginActivity.class);
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
                Log.e("BmobFile","succsess1");
                if(list1.size()==filePaths.length){
                    Log.e("BmobFile","succsess2");
                    String str1 = (String)list1.get(0);//获取第一行数据
                    huodong.setObjectId(id);
                    huodong.setPhotoUrl(list1);
                    huodong.setPhoto_url(str1);
                    huodong.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                handler.sendEmptyMessage(dismiss1);
                                ShowToast("活动举办成功~");
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


    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
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

    private void init() {
        Bmob.initialize(this, "6cb5cd44d15ed665ff487a51fa84537e");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            photourl = BGAPhotoPickerActivity.getSelectedPhotos(data);
            photo_url = String.valueOf(BGAPhotoPickerActivity.getSelectedPhotos(data));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
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
