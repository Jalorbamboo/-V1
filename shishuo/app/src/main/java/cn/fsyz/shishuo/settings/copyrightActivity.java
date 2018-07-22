package cn.fsyz.shishuo.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import cn.fsyz.shishuo.BaseActivity;
import com.fsyz.shishuo.R;

import java.io.InputStream;

//版权声明类
public class copyrightActivity extends BaseActivity {

    Toolbar toolbar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_copyright);

        toolbar = (Toolbar)findViewById(R.id.Copyright_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("版权声明");//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar
        ImageView imageView = (ImageView)findViewById(R.id.copyright_imageView);
        Bitmap bitmap = readBitMap(this,R.mipmap.logo);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        //摧毁内存
        imageView = (ImageView)findViewById(R.id.imageView);
        if(imageView != null && imageView.getDrawable() != null){

            Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageView.setImageDrawable(null);
            if(oldBitmap != null){
                oldBitmap.recycle();
                Log.e("onDestroy","recycle");


            }
        }
        System.gc();
        super.onDestroy();
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
}
