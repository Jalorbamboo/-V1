package cn.fsyz.shishuo;

import android.app.Application;
import android.util.Log;


import com.squareup.leakcanary.LeakCanary;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.logging.Logger;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by JALOR on 2018/2/7.
 */

//App基类
public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d ");
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Log.e("application","yes");
                } else {
                    Log.e("application","no/"+e.getMessage());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        //安装内存泄露检测
        LeakCanary.install(this);
        //侧滑安装
        BGASwipeBackHelper.init(this, null);
        //安装二维码框架
        ZXingLibrary.initDisplayOpinion(this);
    }
}
