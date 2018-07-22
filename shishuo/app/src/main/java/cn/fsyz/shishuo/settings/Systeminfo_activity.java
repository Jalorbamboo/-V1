package cn.fsyz.shishuo.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import cn.fsyz.shishuo.BaseActivity;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JALOR on 2017/3/11.
 */
//更新信息类
public class Systeminfo_activity extends BaseActivity {
    private List<text> fruitList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sys_list_layout);
        TextView sys_btn = (TextView) findViewById(R.id.sys_num);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_sys);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("版本信息");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar

        initText();
        textAdapter adapter = new textAdapter(Systeminfo_activity.this,
                R.layout.sys_list_text_layout, fruitList);
        ListView listView = (ListView) findViewById(R.id.sys_listView);
        listView.setAdapter(adapter);

        PackageManager pm = getPackageManager();
        try {
            //获取包的详细信息
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号和版本名称
            System.out.println("版本号："+info.versionCode);
            System.out.println("版本名称："+info.versionName);
            final String version = info.versionName;
            sys_btn.setText("" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void initText() {
        text new1 = new text(
                "2018.5.28:\n【修复】无法登陆" +
                        "\n2018.5.27:" +
                        "\n【优化】侧栏UI" +
                        "\n【优化】活动模块UI" +
                        "\n【添加】广播站投稿功能" +
                        "\n【开放】话题讨论" +
                        "\n2018.5.12:" +
                        "\n【优化】更新提示" +
                        "\n【优化】网络连接" +
                        "\n【优化】注册提示" +
                        "\n【添加】安全认证" +
                        "\n2018.5.1:" +
                        "\n【修复】话题讨论的bug" +
                        "\n【优化】图片显示" +
                        "\n2018.4.30:" +
                        "\n【优化】个人信息列表" +
                        "\n【优化】主界面动画" +
                        "\n2018.4.29:" +
                        "\n【优化】侧栏UI" +
                        "\n【修复】选择图片时的bug" +
                        "\n【添加】信息界面作者头像" +
                        "\n【优化】个人信息页" +
                        "\n【优化】主界面UI" +
                        "\n2018.4.27:" +
                        "\n【优化】UI" +
                        "\n【修复】选择图片时的bug" +
                        "\n2018.4.26" +
                        "\n【更新】自动更新库" +
                        "\n【修改】UI" +
                        "\n2018.4.24:" +
                        "\n【修复】无法点赞" +
                        "\n【优化】修改资料入口" +
                        "\n【修复】选择图片时的bug" +
                        "\n【优化】版权声明" +
                        "\n2018.4.22:" +
                        "\n【优化】缩小App体积" +
                        "\n【优化】主界面UI" +
                        "\n2018.4.21:" +
                        "\n【修复】无法上传头像" +
                        "\n【添加】评论数量显示功能" +
                        "\n【优化】解忧杂货店写信功能" +
                        "\n【添加】点赞功能" +
                        "\n【添加】主页面广告组建" +
                        "\n2018.4.19:" +
                        "\n【修改】动画" +
                        "\n2018.4.17:" +
                        "\n【优化】头像修改" +
                        "\n2018.4.14:" +
                        "\n【修复】无法注册" +
                        "\n【优化】细节" +
                        "\n【添加】解忧杂货店模块" +
                        "\n【修复】一些小细节" +
                        "\n【添加】活动二维码生成" +
                        "\n【优化】启动页布局" +
                        "\n【优化】诗界面的音乐可关闭" +
                        "\n【添加】扫码参加活动" +
                        "\n2018.4.12:" +
                        "\n【优化】应小米要求无功能选线去除" +
                        "\n2018.4.11:" +
                        "\n【修复】小米安全检测报毒问题" +
                        "\n2018.4.10:" +
                        "\n【优化】布局" +
                        "\n2018.4.6:" +
                        "\n【优化】真心话界面布局" +
                        "\n【优化】畅所欲言布局" +
                        "\n【优化】用户头像读取" +
                        "\n【适配】Android 8.0通知栏" +
                        "\n【优化】启动页OOM" +
                        "\n【优化】布局文字大小" +
                        "\n2018.3.29：" +
                        "\n【添加】启动页自动更新" +
                        "\n2018.3.17：" +
                        "\n【优化】诗的主人才显示删除按钮" +
                        "\n【优化】话题讨论" +
                        "\n【优化】社团活动查看参与人员" +
                        "\n2018.3.13:" +
                        "\n【优化】动画" +
                        "\n2018.3.10:" +
                        "\n【优化】启动界面" +
                        "\n【优化】版权说明" +
                        "\n【优化】话题讨论" +
                        "\n2018.3.8:" +
                        "\n【优化】启动页" +
                        "\n【优化】评论列表" +
                        "\n【优化】话题讨论" +
                        "\n2018.3.6：" +
                        "\n【优化】权限获取" +
                        "\n【优化】UI" +
                        "\n【添加】新logo" +
                        "\n2018.3.3:" +
                        "\n【优化】UI" +
                        "\n【优化】声明" +
                        "\n2018.3.1:" +
                        "\n【修复】一个bug" +
                        "\n【优化】布局结构" +
                        "\n【添加】匿名删除功能" +
                        "\n2018.2.27：" +
                        "\n【更新】主界面功能" +
                        "\n2018.2.22：" +
                        "\n【修复】内存泄漏" +
                        "\n2018.2.21:" +
                        "\n【修复】真心话需要有图片才能发送的bug" +
                        "\n【修复】内存泄露" +
                        "\n【添加】开放活动参加功能" +
                        "\n【修复】一些你没发现的Bug" +
                        "\n【添加】找回密码功能" +
                        "\n【添加】忘记密码找回功能" +
                        "\n【添加】评论其他【诗】主的功能" +
                        "\n【优化】内存泄露" +
                        "\n2018.2.20:" +
                        "\n【添加】【诗】的背景音乐" +
                        "\n2018.2.19:" +
                        "\n【添加】删除功能" +
                        "\n【修复】文字框不显示大V认证" +
                        "\n2018.2.18:" +
                        "\n【添加】自动清除缓存，为你的宝贵的手机空间想的多一点" +
                        "\n【添加】VIP贵宾认证" +
                        "\n2018.2.17:" +
                        "\n【添加】管理员权限" +
                        "\n【优化】访问权限" +
                        "\n2018.2.16:" +
                        "\n【添加】访问权限" +
                        "\n2018.2.15:" +
                        "\n【优化】主页面图片显示" +
                        "\n【优化】启动页布局" +
                        "\n2018.2.14:" +
                        "\n【修复】无法连接网络时的bug" +
                        "\n【优化】分享文字" +
                        "\n【修复】无法提交反馈的bug" +
                        "\n【修复】侧栏无法显示头像" +
                        "\n【修复】启动页不用实名制的漏洞" +
                        "\n【添加】分享功能" +
                        "\n2018.2.13:" +
                        "\n【修复】点赞后图片消失" +
                        "\n【优化】禁止横屏" +
                        "\n【优化】真心话的进入接口" +
                        "\n【优化】点击作者名字可查看详情" +
                        "\n【优化】所有界面的UI逻辑" +
                        "\n【添加】Banner的点击事件" +
                        "\n【添加】更新预告" +
                        "\n2018.2.12:" +
                        "\n【添加】版权声明" +
                        "\n【优化】启动页和广告页的运行逻辑" +
                        "\n【添加】分享多张图片" +
                        "\n【添加】权限询问" +
                        "\n【添加】滑动退出"+
                        "\n【修复】主页搜索栏无法点击的Bug（2018.2.11）" +
                        "\n【优化】界面UI（2018.2.11）" +
                        "\n【添加】下载图片保存位置（2018.2.11）" +
                        "\n【修复】发送界面选择图片后无显示的bug（2018.2.11）" +
                        "\n【添加】图片下载功能（2018.2.11）" +
                        "\n【优化】部分细节（2018.2.11）" +
                        "\n【添加】【诗】主可删除【诗】（20182.11）" +
                        "\n【添加】点击回复后，发【诗】人可收到消息（2018.2.11）" +
                        "\n【修复】登陆界面无法登陆问题（2018.2.10）" +
                        "\n【修复】发送界面选择图片事的Bug（2018.2.10）" +
                        "\n【优化】App图标以及某些界面细节（2018.2.10）" +
                        "\n【添加】用户详情系统（2018.2.10）" +
                        "\n【优化】发送图片时的逻辑（2018.2.10）" +
                        "\n【添加】手动检查更新（2018.2.9）" +
                        "\n【添加】推送消息的点击事件（2018.2.9）" +
                        "\n【添加】标题栏的点击事件（2018.2.9）" +
                        "\n【添加】VIP会员权限（2018.2.9）" +
                        "\n【添加】管理员权限（2018.2.9）" +
                        "\n【修复】未注册成功就可以登陆的BUG（2018.2.9）" +
                        "\n【添加】匿名功能(2018.2.8)" +
                        "\n【优化】主界面Banner（2018.2.8）" +
                        "\n【优化】启动页白屏（2018.2.8）" +
                        "\n【优化】评论以及发送消息功能（2018.2.8）" +
                        "\n【优化】界面布局（2018.2.8）" +
                        "\n【添加】消息推送（2018.2.8）" +
                        "\n【添加】检查更新（2018.2.8）" +
                        "\n【修复】图片过丑（2018.2.7）" +
                        "\n【修复】邮箱无认证且能登陆（2018.2.7）" +
                        "\n【修复】评论栏的bug（20118.2.7）" +
                        "\n【添加】只允许用邮箱登陆（2018.2.7）" +
                        "\n【修复】App图标无法显示（2018.2.7）" +
                        "\n【添加】点击图片的查看功能（2018.2.7）" +
                        "\n【添加】主界面添加发送图片功能（2018.2.7）" +
                        "\n【添加】浮动窗（2018.2.6）" +
                        "\n【添加】实验室的发送图片功能（2018.2.6）" +
                        "\n【优化】评论列表（2018.2.5）" +
                        "\n【优化】主页面（2018.2.5）" +
                        "\n【优化】注册的动画（2018.2.4）" +
                        "\n【优化】主页面列表（2018.2.3）" +
                        "\n【添加】重启App时先进入启动页（2018.2.3）" +
                        "\n【添加】评论功能（2018.2.2）" +
                        "\n【修复】启动页面动画过于卡（2018.2.3）" );
        fruitList.add(new1);
    }



}