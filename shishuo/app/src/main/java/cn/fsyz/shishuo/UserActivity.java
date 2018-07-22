package cn.fsyz.shishuo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.fsyz.shishuo.R;
import com.squareup.picasso.Picasso;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import cn.fsyz.shishuo.Utils.CircleTransform;
import cn.fsyz.shishuo.Utils.fastBlurTransform;
import cn.fsyz.shishuo.fragment.AboutmePagerAdapter;

import java.io.InputStream;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by JALOR on 2018/1/25.
 */
//个人信息页面
public class UserActivity extends BaseActivity {
    ImageView backgroung;
    BGABadgeImageView Userpic;
    EditText rn,rs,rg;
    String Id,r;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
        //加入管理栈
        ActivityCollector.addActivity(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =

                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        //设置CollapsingToolbarLayout的标题文字
        collapsingToolbar.setTitle(" "+ MyUser.getCurrentUser().getUsername());
       // TextView mName = (TextView)findViewById(R.id.User_user);
        initViews();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Userpic = findViewById(R.id.userpic);
        backgroung = findViewById(R.id.backdrop);
        final Bitmap avatarBadgeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_vip);
        BmobQuery<Power> query1 = new BmobQuery<Power>();
        Id  = (String) MyUser.getObjectByKey("objectId");
        query1.addWhereEqualTo("User",Id);
        query1.findObjects(new FindListener<Power>() {
            @Override
            public void done(List<Power> list, BmobException e) {
                if(e==null){
                    for (Power gameScore : list) {
                        r = gameScore.getRight();
                    }
                    if (r.contentEquals("5")){
                        Userpic.showDrawableBadge(avatarBadgeBitmap);
                    }else if (r.contentEquals("3")){
                        Userpic.showDrawableBadge(avatarBadgeBitmap);
                    }else if (r.contentEquals("4")){
                        Userpic.showDrawableBadge(avatarBadgeBitmap);
                    }else{
                        Userpic.hiddenBadge();
                    }
                }
            }
        });
       // rn = findViewById(R.id.rname);
       // rg = findViewById(R.id.rgrade);
       // rs = findViewById(R.id.rschool);

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(Id, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null){
                    String url = myUser.getUserpic_url();
                   // rn.setText(myUser.getRealname());
                   // rg.setText(myUser.getGrade());
                   // rs.setText(myUser.getSchool());
                    Picasso.with(getParent()).load(url).transform(new CircleTransform()).memoryPolicy(NO_CACHE, NO_STORE).config(Bitmap.Config.RGB_565).placeholder(R.mipmap.ic_launcher_round).into(Userpic);
                    Picasso.with(getParent()).load(url).transform(new fastBlurTransform(UserActivity.this)).memoryPolicy(NO_CACHE, NO_STORE).config(Bitmap.Config.RGB_565).placeholder(R.mipmap.ic_launcher_round).into(backgroung);

                }else{
                    Picasso.with(getParent()).load(R.mipmap.ic_launcher_round).into(Userpic);
                }
            }
        });

        toolbar.inflateMenu(R.menu.detail_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.exit:
                        BmobUser.logOut();   //清除缓存用户对象
                             // 现在的currentUser是null了
                        ActivityCollector.finishAll();
                        Intent intent1=new Intent(UserActivity.this,LoginActivity.class);
                        startActivity(intent1);
                        System.exit(0);
                        break;
                    case R.id.changed:
                        Intent intent2=new Intent(UserActivity.this,EnsureActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });





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
    protected void onDestroy() {
        System.gc();
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }



    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AboutmePagerAdapter myFragmentPagerAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    //添加fragment
    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewpager_user);
        myFragmentPagerAdapter = new AboutmePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        //two = mTabLayout.getTabAt(1);
        //three = mTabLayout.getTabAt(2);
        //four = mTabLayout.getTabAt(3);

        //设置Tab的图标，假如不需要则把下面的代码删去
        //one.setIcon(R.mipmap.ic_launcher);
        //two.setIcon(R.mipmap.ic_launcher);
        //three.setIcon(R.mipmap.ic_launcher);
        //four.setIcon(R.mipmap.ic_launcher);


    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {



        getMenuInflater().inflate(R.menu.pic_exit, menu);

        return super.onCreateOptionsMenu(menu);

    }
}