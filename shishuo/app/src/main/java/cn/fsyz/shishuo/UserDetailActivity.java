package cn.fsyz.shishuo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


import android.os.Bundle;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsyz.shishuo.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;


import com.jph.takephoto.model.CropOptions;
import com.squareup.picasso.Picasso;

import cn.fsyz.shishuo.Bean.MyUser;

import cn.fsyz.shishuo.Utils.CircleTransform;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import pub.devrel.easypermissions.EasyPermissions;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;
//用户更改信息页面
public class UserDetailActivity extends TakePhotoActivity implements EasyPermissions.PermissionCallbacks {
    ImageView pic;
    Toolbar toolbar;
    private final int PROCESS2 = 2;
    private static final int dismiss1 = 4;
    ProgressDialog progressDialog1;
    TextView changepassward;
    EditText oldpassward,newpassward;
    String pic_url;
    //得到权限的int
    private static final int GETPERMISSION = 6;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(UserDetailActivity.this);
                    progressDialog1.setTitle("正在努力上传头像中，请等待");
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        setContentView(R.layout.activity_user_detail);
        //获得权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        final Uri imageUri = Uri.fromFile(file);
        pic = findViewById(R.id.changepic);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("pic","onclick");
                TakePhoto takePhoto = getTakePhoto();
                //takePhoto.onPickFromDocuments();
                //takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
                takePhoto.onPickFromGallery();
            }
        });
        MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
        String id = bmobUser.getObjectId();
        BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null){
                    if (myUser.getUserpic_url().contentEquals("")){
                        Log.e("没有头像","yes");
                    }else{
                        pic_url =  myUser.getUserpic_url();
                        Log.e("log",""+pic_url);

                    }
                }
            }
        });
        changepassward = findViewById(R.id.change_passward);
        changepassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("请输入用户名和密码");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                view = LayoutInflater.from(UserDetailActivity.this).inflate(R.layout.dialog, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final View finalView = view;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        oldpassward = (EditText) finalView.findViewById(R.id.oldpassward);
                        newpassward = (EditText) finalView.findViewById(R.id.newpassword);
                        cpassward();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
        String username = (String) MyUser.getObjectByKey("objectId");
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(username, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null){
                    String url = myUser.getUserpic_url();
                    Picasso.with(getParent()).load(url).transform(new CircleTransform()).memoryPolicy(NO_CACHE, NO_STORE).config(Bitmap.Config.RGB_565).placeholder(R.mipmap.ic_launcher).into(pic);
                }else{
                    Picasso.with(getParent()).load(R.mipmap.ic_launcher).memoryPolicy(NO_CACHE, NO_STORE).config(Bitmap.Config.RGB_565).transform(new CircleTransform()).into(pic);
                }
            }
        });


    }

    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        CropOptions.Builder builder = new CropOptions.Builder();
            builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    //更改密码
    private void cpassward() {
        handler.sendEmptyMessage(PROCESS2);
        String a = oldpassward.getText().toString();
        String b = newpassward.getText().toString();
        MyUser newUser = new MyUser();
        newUser.setPassword(b);
        MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    handler.sendEmptyMessage(dismiss1);
                    Toast.makeText(UserDetailActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                }else{
                    handler.sendEmptyMessage(dismiss1);
                    Toast.makeText(UserDetailActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("cuowu",""+e.getMessage());
                }
            }
        });
    }


    @Override

    public void takeCancel() {

        super.takeCancel();

    }





    @Override
    public void takeSuccess(String imagePath) {
        super.takeSuccess(imagePath);
        handler.sendEmptyMessage(PROCESS2);
        Log.e("takesuccess",imagePath);
        Picasso.with(this).load("file://"+imagePath).transform(new CircleTransform()).into(pic);
        if(pic_url==null){
            Log.e("pic_url","为空");
        }else{
            Log.e("pic_url","不为空");
            Delefile();
        }

        final BmobFile bmobFile = new BmobFile(new File(imagePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    saveFile(bmobFile);
                }else {
                    Log.e("errorfile",e.getMessage());
                }
            }
        });
    }

    private void saveFile(BmobFile bmobFile) {
        Log.e("保存","头像");
        String url = bmobFile.getUrl();
        MyUser newUser = new MyUser();
        newUser.setUserpic_url(url);
        newUser.setUserpic(bmobFile);
        MyUser bmobUser = MyUser.getCurrentUser(MyUser.class);
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    handler.sendEmptyMessage(dismiss1);
                    Toast.makeText(UserDetailActivity.this,"更换成功",Toast.LENGTH_LONG).show();
                }else{
                    handler.sendEmptyMessage(dismiss1);
                    Toast.makeText(UserDetailActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("cuowu",""+e.getMessage());
                }
            }
        });

    }

    //删除原头像
    private void Delefile() {
        Log.e("删除","中");
        BmobFile file = new BmobFile();
        file.setUrl(pic_url);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ShowToast("原头像删除成功");
                    Log.e("删除","成功");
                }else{
                    ShowToast("原头像删除失败："+e.getErrorCode()+","+e.getMessage());
                }
            }
        });

    }

    @Override
    public void takeFail(String msg) {
        super.takeFail(msg);
    }


    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }

    Toast mToast;

    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }








    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
