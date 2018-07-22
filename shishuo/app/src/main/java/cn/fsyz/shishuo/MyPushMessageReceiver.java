package cn.fsyz.shishuo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.fsyz.shishuo.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Created by JALOR on 2018/2/7.
 */

//服务器广播接收
public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            // 收到广播时,发送一个通知
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String title = "【诗】说有新消息，请到App内查看";
            String web = null;
            String alert = null;
            try {
                // 处理JSON
                JSONObject jsonObject = new JSONObject(jsonStr);
                alert = jsonObject.getString("alert");
                title = jsonObject.getString("title");
                web = jsonObject.getString("web");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent1 = new Intent(context, PushActivity.class);
            intent1.putExtra("web_p", web);
            intent1.putExtra("title_p", title);
            intent1.putExtra("alert_p",alert);
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) (Math.random() * 1000) + 1, intent1, Intent.FLAG_ACTIVITY_CLEAR_TOP);



            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel("channel_01",
                        "消息推送", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(mChannel);
            }


            //如果是评论通知
            if (web==null){
                Notification.Builder builder = new Notification.Builder(context);
                builder.setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setContentText(alert)
                        .setAutoCancel(true);//用户点击就自动消失
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setChannelId("channel_01");
                }

                manager.notify(1, builder.build());
                //如果是推送
            }else{
            Notification.Builder builder = new Notification.Builder(context);
            builder
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setContentText(alert)
                        .setAutoCancel(true)//用户点击就自动消失
                        .setContentIntent(pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setChannelId("channel_01");
                }
            manager.notify(1, builder.build());
            }
        }

    }
}
