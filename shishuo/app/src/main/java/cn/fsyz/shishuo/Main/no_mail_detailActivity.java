package cn.fsyz.shishuo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.fsyz.shishuo.BaseActivity;
import com.fsyz.shishuo.R;

public class no_mail_detailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_no_mail_detail);
        TextView textView = findViewById(R.id.no_mail_content);
        Intent intent = getIntent();
        String c = intent.getStringExtra("content");
        textView.setText(c);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_no_mail_detail);//toolbar
        toolbar.setTitle("老爷爷的回信");//toolbar
        setSupportActionBar(toolbar);//toolbar
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
