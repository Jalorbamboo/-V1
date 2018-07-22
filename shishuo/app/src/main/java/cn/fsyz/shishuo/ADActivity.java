package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fsyz.shishuo.R;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.carbs.android.autozoominimageview.library.AutoZoomInImageView;

//广告活动
public class ADActivity extends AppCompatActivity {

    private static final int RUN_NORMAL = 1;
    private static final int FAIL = 2;
    Timer timer,timer1;
    AutoZoomInImageView auto_zoomin_image_view;
    ImageView logo;
    private long exittime = 0;

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



                                ADActivity.this.finish();

                        }
                    };
                    timer.schedule(timerTask,2000);
                    break;
                case FAIL:

                    timer1=new Timer();
                    TimerTask timerTask1=new TimerTask()
                    {
                        @Override
                        public void run() {



                            ActivityCollector.finishAll();
                            Intent intent  = new Intent(ADActivity.this,LoginActivity.class);
                            startActivity(intent);
                            ADActivity.this.finish();

                        }
                    };
                    timer.schedule(timerTask1,1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        init();

        auto_zoomin_image_view = findViewById(R.id.auto_zoomin_image_view);
        logo = findViewById(R.id.logo);
        Bitmap bitmap1 = readBitMap(this,R.mipmap.logo);
        logo.setImageBitmap(bitmap1);
        //Bitmap bitmap = readBitMap(this,R.drawable.login_bg5);
        //auto_zoomin_image_view.setImageBitmap(bitmap);
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
                        .start(1000);//延迟1000毫秒启动
            }
        });
        handlerlist.sendEmptyMessage(RUN_NORMAL);
    }

    private void init() {
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 按返回键禁止退出应用程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exittime) > 2000) {
                // 应用名
                String applicationName = getResources().getString(
                        R.string.app_name);
                String msg = "再按一次返回键退出界面" + applicationName;
                //String msg1 = "再按一次返回键回到桌面";
                Toast.makeText(ADActivity.this, msg, Toast.LENGTH_SHORT).show();
                // 计算两次返回键按下的时间差
                exittime = System.currentTimeMillis();
            } else {
                // 关闭应用程序
                finish();
                // 返回桌面操作
                // Intent home = new Intent(Intent.ACTION_MAIN);
                // home.addCategory(Intent.CATEGORY_HOME);
                // startActivity(home);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
