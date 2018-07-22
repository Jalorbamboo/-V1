package cn.fsyz.shishuo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.v3.Bmob;

/**
 * Created by JALOR on 2017/10/27.
 */

//没有滑动的基本类
public abstract class NoSwipBaseActivity extends AppCompatActivity  {





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

    private static final String TAG = "BaseActivity";
    //用来控制应用前后台切换的逻辑
    private boolean isCurrentRunningForeground = true;
    @Override
    protected void onStart() {
        super.onStart();
        if (!isCurrentRunningForeground) {
            isCurrentRunningForeground=true;
            //处理跳转到广告页逻辑
            //Intent intennt=new Intent(this,ADActivity.class);
            //startActivity(intennt);
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>切回前台 activity process");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            Log.e(TAG,">>>>>>>>>>>>>>>>>>>切到后台 activity process");
        }
    }


    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程,查看该应用是否在运行
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    Log.e(TAG,"EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        Log.e(TAG, "EntryActivity isRunningBackGround");
        return false;
    }



    private void init(){
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }




}
