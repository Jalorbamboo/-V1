package cn.fsyz.shishuo.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import com.squareup.picasso.Picasso;

import com.squareup.picasso.Transformation;
import cn.fsyz.shishuo.ActivityCollector;
import cn.fsyz.shishuo.Bean.AppVersion;
import cn.fsyz.shishuo.Bean.Banner;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Preview;
import cn.fsyz.shishuo.DetailActivity;
import cn.fsyz.shishuo.CallBackActivity;
import cn.fsyz.shishuo.LoginActivity;
import cn.fsyz.shishuo.NoSwipBaseActivity;
import com.fsyz.shishuo.R;
import cn.fsyz.shishuo.Utils.AdDialog;
import cn.fsyz.shishuo.settings.SettingsActivity;
import cn.fsyz.shishuo.UserActivity;
import cn.fsyz.shishuo.Utils.CircleTransform;
import cn.fsyz.shishuo.fragment.MyFragmentPagerAdapter;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;


import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import pub.devrel.easypermissions.EasyPermissions;

import static com.just.agentweb.FileUpLoadChooserImpl.REQUEST_CODE;
import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;


//主界面的Activity
public class MainActivity extends NoSwipBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,EasyPermissions.PermissionCallbacks, OnDownloadListener {

    //BANNER的注册器
    MZBannerView<Banner> mMZBanner;
    //得到权限的int
    private static final int GETPERMISSION = 6;
    //获得用户等级
    String right;
    //侧栏的头像
    ImageView imageView;
    //侧栏的文字
    TextView school;
    //主页ADdialog
    String ad_url;
    //加载框架
    ProgressDialog pd6;


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    //版本信息
    PackageInfo packageInfo ;

    //注册广播
    FreshBroadcastReceiver networkChangeReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //注册更新
        setContentView(R.layout.activity_main);
        //初始化视图
        initViews();
        //initData();
        //获得权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
           ShowToast("【诗】主，欢迎回来！");
        } else {
            EasyPermissions.requestPermissions(this, "诗说需要以下权限:\n\n1.获得手机的识别码（用于登陆）\n2.获得手机的储存读取权限（" +
                    "用于软件的更新）+\n3.获取手机拍照权限（识别二维码）", GETPERMISSION, perms);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //注册广播监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.sunshinecompany.shishuo.broadcasttest.FRESH_BROADCAST");
         networkChangeReceiver = new FreshBroadcastReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);


        //爆炸式menu
        BoomMenuButton boomMenuButton = (BoomMenuButton) findViewById(R.id.fab);
        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            switch (index){
                                case 0:
                                    Intent intent1 = new Intent(MainActivity.this,SendActivity.class);
                                    startActivity(intent1);
                                    break;
                                case 1:
                                    Intent intent2 = new Intent(MainActivity.this,Nosad_SendActivity.class);
                                    startActivity(intent2);
                                    break;
                                case 2:
                                    Intent intent = new Intent(MainActivity.this, WhiteSendActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    Intent intent3 = new Intent(MainActivity.this,TalkOpenActivity.class);
                                    startActivity(intent3);
                            }
                        }
                    })
                    .normalImageRes(getImageResource())
                    .normalText(getext());
            boomMenuButton.addBuilder(builder);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //设置侧栏图标为原来的颜色
        navigationView.setItemIconTintList(null);
        //设置侧栏的指令
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        school= headerView.findViewById(R.id.SchoolView);
        imageView=(ImageView)headerView.findViewById(R.id.imageView);

        //设置侧栏用户名字
        final TextView myName = (TextView) headerView.findViewById(R.id.UserName);
        final BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser!=null) {
        String localUserN = BmobUser.getCurrentUser().getUsername();
        myName.setText(localUserN);
        }


        LoadADdialog();



        //获得banner的数据
        mMZBanner = findViewById(R.id.banner);

        //活动弹窗
        agreeornot();
        snedBroad();
        //检查更新
        UpdateOrNot();

    }
    private final int SHOW_QUITDIALOG = 10;
    @SuppressLint("HandlerLeak")
    private Handler handler_Safe = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_QUITDIALOG:
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(MainActivity.this);
                    normalDialog.setIcon(R.mipmap.logo);
                    normalDialog.setTitle("注意");
                    normalDialog.setMessage("您的账号已经在别的设备登陆，请重新登陆！");
                    normalDialog.setCancelable(false);
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BmobUser.logOut();   //清除缓存用户对象
                                    // 现在的currentUser是null了
                                    ActivityCollector.finishAll();
                                    Intent intent1=new Intent(MainActivity.this,LoginActivity.class);
                                    startActivity(intent1);
                                    System.exit(0);
                                }
                            });
                    normalDialog.show();
                    break;

            }
        }
    };

    private void Safeverify() {
        BmobInstallation bmobInstallation = new BmobInstallation();
        final String Lid  = bmobInstallation.getInstallationId();
        BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();
        final String Id  = (String) MyUser.getObjectByKey("objectId");
        query1.getObject(Id, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null){
                    if (Lid.contentEquals(myUser.getUDID())){
                        //ShowToast("一致");
                        Log.e("Mainactivity","yes");
                    }else{
                        handler_Safe.sendEmptyMessage(SHOW_QUITDIALOG);
                    }
                }
            }
        });
    }

    private void LoadADdialog() {
        BmobQuery<Preview> query = new BmobQuery<>();
        query.getObject("Wj6HoooA", new QueryListener<Preview>() {
            @Override
            public void done(Preview preview1, BmobException e) {
                if (e==null){
                    ad_url = preview1.getWel().getFileUrl();
                    if (preview1.getShow()){
                        AdDialog adDialog = new AdDialog(MainActivity.this,ad_url,"测试",""+preview1.getPreview());
                        adDialog.onCreateView();
                        adDialog.setUiBeforShow();
                        //点击空白区域能不能退出
                        adDialog.setCanceledOnTouchOutside(true);
                        //按返回键能不能退出
                        adDialog.setCancelable(true);
                        adDialog.show();
                    }
                }
            }
        });
    }

    //爆炸
    private static int index = 0;
    static String getext() {
        if (index >= text.length) index = 0;
        return text[index++];

    }
    private static String [] text = new String[]{"分享","解忧小店","真心话","畅所欲言"

    };
    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.write,
            R.drawable.email,
            R.drawable.heart,
            R.mipmap.tabbar_compose_idea
//            R.drawable.bee,
//            R.drawable.butterfly,
//            R.drawable.cat,
//            R.drawable.deer,
//            R.drawable.dolphin,
//            R.drawable.eagle,
//            R.drawable.horse,
//            R.drawable.jellyfish,
//            R.drawable.owl,
//            R.drawable.peacock,
//            R.drawable.pig,
//            R.drawable.rat,
//            R.drawable.snake,
//            R.drawable.squirrel
    };

    private void snedBroad() {
        Intent intent = new Intent("com.sunshinecompany.shishuo.broadcasttest.FRESH_BROADCAST");
        this.sendBroadcast(intent);
    }



    private void agreeornot() {
        SharedPreferences shared = getSharedPreferences("agree",MODE_PRIVATE);
        String isfer = shared.getString("isfer","");
        if(isfer.equals("y")){
            //第二次进入跳转  

            getBannerImage();
            //initData1();
            //加入活动栈管理
            ActivityCollector.addActivity(this);
        }else{
            //第一次进入跳转 
            ShowToast("因您没有同意服务协议，将在三秒后关闭App");
            Timer timer=new Timer();
            TimerTask timerTask=new TimerTask()
            {
                @Override
                public void run() {
                    BmobUser.logOut();
                        finish();
                }
            };
            timer.schedule(timerTask,3000);
        }
    }

    private void getpic() {
        //获得侧栏的用户头像
        final String username = (String) MyUser.getObjectByKey("objectId");
            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.getObject(username, new QueryListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e==null) {
                        String url = myUser.getUserpic_url();
                        Picasso.with(getParent()).load(url).transform(new CircleTransform()).config(Bitmap.Config.RGB_565).placeholder(R.mipmap.ic_launcher).into(imageView);
                        school.setText("" + myUser.getSchool());
                    }
                    //else{
                      //  final String username = (String) MyUser.getObjectByKey("objectId");
                      //  BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                      //  query.getObject(username, new QueryListener<MyUser>() {
                      //      @Override
                       //     public void done(MyUser myUser, BmobException e) {
                       //         if (e==null) {
                      //              String url = myUser.getUserpic_url();
                      //              Picasso.with(getParent()).load(url).transform(new CircleTransform()).config(Bitmap.Config.RGB_565).placeholder(R.mipmap.ic_launcher).into(imageView);
                      //              school.setText("" + myUser.getSchool());
                      //          }}});
                    //}
                }
            });

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 把执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        finish();
        ShowToast("缺乏必要的权限，【诗】说无法运行，请到设置内给予权限");
    }

    //当用户拒绝提供权限
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == GETPERMISSION) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }



    //为Baner绑定Holder
    public static class BannerViewHolder implements MZViewHolder<Banner>{

        private ImageView mImageView;
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(0)
                .oval(false)
                .build();

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }
        //为图片提供摆放位置
        @Override
        public void onBind(Context context, int i, Banner movie) {
            Picasso.with(context).load(movie.getBanner().getFileUrl()).memoryPolicy(NO_CACHE, NO_STORE).transform(transformation).config(Bitmap.Config.RGB_565).into(mImageView);

        }
    }

    //查询Banner数据
    private void getBannerImage(){
        BmobQuery<Banner> bannerBmobQuery = new BmobQuery<Banner>();
        bannerBmobQuery.findObjects(new FindListener<Banner>() {
            @Override
            public void done(List<Banner> list, BmobException e) {
                if (e==null){
                if (list.size() > 0) {
                    setBanner(list);
                }
            }
            }
        });
    }

    //设置Banner数据
    private void setBanner(final List<Banner> list){
        //设置点击事件
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
        //设置Banner页数
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
               // Log.e("ViewHolder","run");
                return new BannerViewHolder();
            }
        });
        mMZBanner.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//暂时取消
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.qr_code) {
            Intent intent = new Intent(getApplication(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            //startActivity(intent);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_share) {
           Intent textIntent = new Intent(Intent.ACTION_SEND);
           textIntent.setType("text/plain");
           textIntent.putExtra(Intent.EXTRA_TEXT, "佛山一中创客实验室自主开发的校园社交应用诗说，快来体验吧！" +
                   "下载请点击>>https://www.coolapk.com/apk/com.sunshinecompany.shishuo<<");
           startActivity(Intent.createChooser(textIntent, "分享"));
        } else if (id == R.id.nav_send) {
           Intent intent = new Intent(MainActivity.this, CallBackActivity.class);
           startActivity(intent);
       }else if (id==R.id.settings){
           Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
           startActivity(intent);
       }else if(id==R.id.nosad_receive){
           Intent intent = new Intent(MainActivity.this,Nosad_MyMailActivity.class);
           startActivity(intent);
       }else if (id==R.id.nav_pic){
           if (MyUser.getCurrentUser()!=null) {
               try{
                   Intent intent = new Intent(MainActivity.this,UserActivity.class);
                   ShowToast("修改资料点击右上角的按钮哦！");
                   startActivity(intent);
               }catch (Exception e){
                   ShowToast("无法获取你的数据");
                   e.printStackTrace();
               }
           }else {
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);
           }
       }else if (id==R.id.nav_Bsender){
           Intent intent = new Intent(MainActivity.this,BSenderActivity.class);
           startActivity(intent);
       }else if (id==R.id.nav_activity){
           Intent intent = new Intent(MainActivity.this,HuodongActivity.class);
           startActivity(intent);
       }else if(id==R.id.nav_whitemessage){
           Intent intent = new Intent(MainActivity.this,WhiteActivity.class);
           startActivity(intent);
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //注册机
    private void init() {
        Bmob.initialize(this, "6cb5cd44d15ed665ff487a51fa84537e");
    }







    //禁止用户退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 按返回键禁止退出应用程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    //Banner的要求
    @Override
    public void onPause() {
        super.onPause();
        mMZBanner.pause();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();
    }




    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        //取消动态变化广播接收器的注册
        unregisterReceiver(networkChangeReceiver);
        super.onDestroy();

    }

    //添加fragment
    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        //four = mTabLayout.getTabAt(3);

        //设置Tab的图标，假如不需要则把下面的代码删去
        //one.setIcon(R.mipmap.ic_launcher);
        //two.setIcon(R.mipmap.ic_launcher);
        //three.setIcon(R.mipmap.ic_launcher);
        //four.setIcon(R.mipmap.ic_launcher);


    }

    //获得活动弹窗



    public class FreshBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新banner
            getBannerImage();
            //刷新头像
            getpic();
            //只允许一账号一机器登陆
            Safeverify();
        }
    }

    private void UpdateOrNot() {
        BmobQuery<AppVersion> appVersionBmobQuery = new BmobQuery<>();
        appVersionBmobQuery.order("-createdAt");
        appVersionBmobQuery.findObjects(new FindListener<AppVersion>() {
            @Override
            public void done(List<AppVersion> list, BmobException e) {
                if (e==null){

                        String a = list.get(0).getVersioncode();
                        int i = Integer.parseInt(a);
                        String Log = list.get(0).getUpdate_log();
                        String target_size = list.get(0).getTarget_side();
                        String version = list.get(0).getVersion();
                        String Url = list.get(0).getPath().getFileUrl();
                        Boolean isForse = list.get(0).getIsforce();
                        CheckNewVersion(Url,isForse,i,Log,target_size,version);

                }
            }
        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**

         * 处理二维码扫描结果

         */

        if (requestCode == REQUEST_CODE) {

            //处理扫描结果（在界面上显示）

            if (null != data) {

                Bundle bundle = data.getExtras();

                if (bundle == null) {

                    return;

                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {

                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Log.e("解析结果:" ,"" +result);
                    Intent intent = new Intent(MainActivity.this,Huodong_detailActivity.class);
                    intent.putExtra("id",result);
                    startActivity(intent);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    Log.e("解析失败" ,"");

                }

            }

        }
    }

    private void CheckNewVersion(String url, Boolean isForse, int a, String log, String target_size, String version) {
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //支持断点下载
                .setBreakpointDownload(true)
                //设置是否显示通知栏进度
                .setShowNotification(false)
                //设置强制更新
                .setForcedUpgrade(isForse)
                //设置下载过程的监听
                .setOnDownloadListener(this);
        DownloadManager manager = DownloadManager.getInstance(this);
        manager.setApkName("诗说.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(false)
                .setConfiguration(configuration)
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setApkVersionCode(a)
                .setApkVersionName(version)
                .setApkSize(target_size)
                .setApkDescription(log)
                .download();
    }
    @Override
    public void start()
    {
        handlerF.sendEmptyMessage(PROCESS2);
    }

    @Override
    public void downloading(final int max, final int progress) {
    }

    @Override
    public void done(File apk) {
        Log.e("已下载完毕","");
    }

    @Override
    public void error(Exception e) {

    }



    private final int PROCESS2 = 2;
    private static final int dismiss1 = 4;
    private static final int dismiss2 = 5;
    private ProgressDialog progressDialog;
    @SuppressLint("HandlerLeak")
    private Handler handlerF = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    pd6 = new ProgressDialog(MainActivity.this);
                    pd6.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置水平进度条
                    pd6.setCancelable(false);// 设置是否可以通过点击Back键取消
                    pd6.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    pd6.setIcon(R.mipmap.logo);// 设置提示的title的图标，默认是没有的
                    pd6.setTitle("提示");
                    pd6.setMessage("新版本下载中,请耐心等待，大概15秒的下载时间");
                    pd6.show();
                    break;
                case dismiss1:
                    pd6.dismiss();
                    break;
                case dismiss2:
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("安装中");
                    progressDialog.show();
                    break;
            }
        }
    };


}
