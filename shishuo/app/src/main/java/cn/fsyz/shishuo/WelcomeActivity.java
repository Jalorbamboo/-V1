package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.fsyz.shishuo.Bean.Splash;
import cn.fsyz.shishuo.Main.MainActivity;

import com.fsyz.shishuo.R;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.carbs.android.autozoominimageview.library.AutoZoomInImageView;
import cn.fsyz.shishuo.Utils.DeleteFileUtil;

//启动页
public class WelcomeActivity extends AppCompatActivity {

    private static final int RUN_NORMAL = 1;
    private static final int JUDGE = 2;
    Timer timer;
    AutoZoomInImageView auto_zoomin_image_view;
    ImageView logo;
    //远程图片地址
    String Url;
    //网络启动页版本
    String no;
    //文件
    BmobFile file;
    //修改器
    SharedPreferences.Editor editor,editor_run_first;
    //文件名
    String Fname;
    private String PATH = "/shishuo/";
    String target = Environment.getExternalStorageDirectory().getAbsolutePath()+PATH;
    @SuppressLint("HandlerLeak")
    private Handler handlerlist = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case RUN_NORMAL:
                    Log.e("ad","RUN_NORMAL");
                    timer=new Timer();
                    TimerTask timerTask=new TimerTask()
                    {
                        @Override
                        public void run() {
                            BmobUser bmobUser = BmobUser.getCurrentUser();
                            if (bmobUser!=null){
                                WelcomeActivity.this.finish();
                                Intent intent1=new Intent(WelcomeActivity.this,MainActivity.class);
                                startActivity(intent1);
                            }else{
                                WelcomeActivity.this.finish();
                                Intent intent1=new Intent(WelcomeActivity.this,LoginActivity.class);
                                startActivity(intent1);

                            }
                        }
                    };
                    timer.schedule(timerTask,4000);
                    break;
                case JUDGE:
                    SharedPreferences shared=getSharedPreferences("is",MODE_PRIVATE);
                    String isfer=shared.getString("Splashname","");
                    editor=shared.edit();
                    if(isfer.equals(Fname)){
                        //名字符合  
                        return;
                    }else{
                        //名字不符合
                        DeleteFileUtil.deleteDirectory(target);
                        oncrateFile();
                        download();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        init();
        //清理缓存
        //CleanMessageUtil.clearAllCache(getApplicationContext());
        oncrateFile();
        setContentView(R.layout.activity_welcome);
        auto_zoomin_image_view = findViewById(R.id.auto_zoomin_image_view);
        //加载图片
        antu();
        logo = findViewById(R.id.logo);
        SharedPreferences shared=getSharedPreferences("judge",MODE_PRIVATE);
        String isfer=shared.getString("run","");
        editor_run_first=shared.edit();
        if(isfer.equals("yes")){
            //名字符合 
            //判断数据 
            data();
        }else{
            //名字不符合
            editor_run_first.putString("run","yes");
            editor_run_first.commit();

        }


        Bitmap bitmap1 = readBitMap(this,R.mipmap.logo);
        logo.setImageBitmap(bitmap1);
        //Bitmap bitmap = readBitMap(this,);
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
            }
        });
        handlerlist.sendEmptyMessage(RUN_NORMAL);

    }



    private void antu() {
        SharedPreferences shared=getSharedPreferences("is",MODE_PRIVATE);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        Fname = shared.getString("Splashname","");
        File path = new File(target+Fname); //获得SDCard目录
        Bitmap bitmap2 = BitmapFactory.decodeFile(target+Fname,opt);
        if (path!=null){
            auto_zoomin_image_view.setImageBitmap(bitmap2);
        }else{
            return;
        }
        auto_zoomin_image_view.post(new Runnable() {
            @Override
            public void run() {
                //简单方式启动放大动画
                //放大增量是0.3，放大时间是1000毫秒，放大开始时间是1000毫秒以后
//                auto_zoomin_image_view.init()
//                  .startZoomInByScaleDeltaAndDuration(0.3f, 1000, 1000);

                //使用较为具体的方式启动放大动画
                auto_zoomin_image_view.init()
                        .setScaleDelta(0.2f)//放大的系数是原来的（1 + 0.2）倍
                        .setDurationMillis(1500)//动画的执行时间为1500毫秒
                        .setOnZoomListener(new AutoZoomInImageView.OnZoomListener(){
                            @Override
                            public void onStart(View view) {
                                //放大动画开始时的回调
                            }
                            @Override
                            public void onUpdate(View view, float progress) {
                                //放大动画进行过程中的回调 progress取值范围是[0,1]
                            }
                            @Override
                            public void onEnd(View view) {
                                //放大动画结束时的回调
                            }
                        })
                        .start(1);//延迟1000毫秒启动
            }
        });
    }



    private void oncrateFile() {
        File Dir  = new File(target);
        if (!Dir.exists()){
            Dir.mkdir();
            Log.e("create file","yes");
        }else{
            return;
        }
    }

    private void data() {
        BmobQuery<Splash> query = new BmobQuery<>();
        query.findObjects(new FindListener<Splash>() {
            @Override
            public void done(List<Splash> object, BmobException e) {
                if(e==null){
                    for (Splash preview1 : object) {
                        //获得playerName的信息
                        Url = preview1.getHaibao().getFileUrl();
                        no = preview1.getNo();
                        Fname = preview1.getHaibao().getFilename();
                        file = preview1.getHaibao();
                    }
                    //判断是否下载
                    handlerlist.sendEmptyMessage(JUDGE);
                    Log.e("wel","chenggong");
                }else{
                    Log.e("wel","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }







    private void download() {
        Log.e("download","start");
        File saveFile = new File(target, file.getFilename());
        file.download(saveFile,new DownloadFileListener() {
          @Override
        public void done(String s, BmobException e) {
          if (e==null){
            Log.e("down","下载成功"+s);
            editor.putString("Splashname", Fname);
            editor.commit();
            }else{
          Log.e("down",""+e.getMessage());
                }
          }

         @Override
         public void onProgress(Integer integer, long l) {
             Log.e("download","下载进度："+integer+","+l);
         }
        });

    }

    private void init() {
        //Bmob.initialize(this, "6cb5cd44d15ed665ff487a51fa84537e");
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId("8780088b394aa22ea2abbbec1bcebc8d")
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(5)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        .build();
        Bmob.initialize(config);

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
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ad","restart");
        handlerlist.sendEmptyMessage(RUN_NORMAL);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    }
