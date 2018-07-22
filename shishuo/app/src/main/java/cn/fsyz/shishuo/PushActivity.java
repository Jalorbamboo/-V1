package cn.fsyz.shishuo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.fsyz.shishuo.R;
import com.just.agentweb.AgentWeb;

//推送详情类
public class PushActivity extends BaseActivity {

     AgentWeb mAgentWeb;
     LinearLayout mLinearLayout,mLinearLayout1;
     String title,content;
     Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        String web1 = intent.getStringExtra("web_p");
        title = intent.getStringExtra("title_p");
        content = intent.getStringExtra("alert_p");
        Log.e("title",""+web1);
        setContentView(R.layout.activity_push);
        mLinearLayout = findViewById(R.id.container);
        //mLinearLayout1 = findViewById(R.id.text_p);

            //mLinearLayout.setVerticalGravity(View.GONE);
            mAgentWeb = AgentWeb.with(this)//

                    .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//

                    .useDefaultIndicator()
                    .defaultProgressBarColor()
                    .createAgentWeb()//
                    .ready()
                    .go(web1);

        mToolbar = findViewById(R.id.toolbar_web);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(""+title);
        this.setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null)

            // Enable the Up button

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                finish();

            }

        });

    }
}
