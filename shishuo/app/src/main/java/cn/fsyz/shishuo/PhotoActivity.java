package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.fsyz.shishuo.R;
import com.squareup.picasso.Picasso;
import cn.fsyz.shishuo.Bean.Comment;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

//照片读取类
public class PhotoActivity extends BaseActivity {

    private String PATH = "/com.sunshinecompany.shishuo/";
    String target = Environment.getExternalStorageDirectory().getAbsolutePath()+PATH;
    private static final int PROCESS2 = 2;
    private static final int dismiss2 = 5;
    ProgressDialog progressDialog1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESS2:
                    progressDialog1 = new ProgressDialog(PhotoActivity.this);
                    progressDialog1.setTitle("下载中，请耐心等待");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                    break;
                case dismiss2:
                    progressDialog1.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        createFile();
        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        final String id = intent.getStringExtra("id");
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_photo);//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setTitle("查看图片");//toolbar
        toolbar.inflateMenu(R.menu.photo_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.download:
                        handler.sendEmptyMessage(PROCESS2);
                        BmobQuery<Comment> query = new BmobQuery<>();
                        query.getObject(id, new QueryListener<Comment>() {
                            @Override
                            public void done(Comment comment, BmobException e) {
                                if (e==null){
                                    //BmobFile file = comment.getPhoto();
                                    //File saveFile = new File(target, file.getFilename());
                                    //file.download( saveFile,new DownloadFileListener() {
                                      //  @Override
                                        //public void done(String s, BmobException e) {
                                          //  if (e==null){
                                            //    ShowToast("下载成功"+s);
                                              //  handler.sendEmptyMessage(dismiss2);
                                        //    }else{
                                              //  ShowToast(""+e.getMessage());
                                              //  handler.sendEmptyMessage(dismiss2);
                                          //  }
                                      //  }

                                      //  @Override
                                      //  public void onProgress(Integer integer, long l) {

                                     //   }
                                  //  });
                                }else{
                                    handler.sendEmptyMessage(dismiss2);
                                    ShowToast("获得图片源文件失败"+e.getMessage());
                                }
                            }
                        });
                        break;

                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//toolbar
        ImageView imageView = findViewById(R.id.url_photo);
        Picasso.with(PhotoActivity.this).load(url)
                .error(R.drawable.error)
                .into(imageView);



    }

    private void createFile(){
        File Dir  = new File(target);
        if (!Dir.exists()){
            Dir.mkdir();
            Log.e("create file","yes");
        }else{
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_main, menu);
        return true;
    }

    private void init(){
        Bmob.initialize(this, "8780088b394aa22ea2abbbec1bcebc8d");
    }
}
