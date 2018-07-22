package cn.fsyz.shishuo.AdapterControl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import cn.fsyz.shishuo.Bean.Comment;
import cn.fsyz.shishuo.Bean.MyUser;
import cn.fsyz.shishuo.Bean.Power;
import com.fsyz.shishuo.R;
import cn.fsyz.shishuo.Utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.dp2px;

/**
 * Created by JALOR on 2018/1/25.
 */

//主页面的适配器
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_TEXTVIEW = 0;//文本
    private static final int TYPE_IMAGEVIEW = 1;//带有图片
    private String Id,r,id;//设置字符串
    private Context mContext;//上下文
    private List<Comment> dataList = new ArrayList<>();//设置comment的一串数据
    private MainAdapter.OnItemClickListener onItemClickListener;//设置点击事件
     int screenWidth,screenHith;






    //获得数据
    public void addAllData(List<Comment> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }


    //获得数据类型有无图片
    @Override
    public int getItemViewType(int position) {
        String i = "";
        if (null == dataList.get(position).getPhoto_url()) {
            //Log.e(""+position,"TYPE_TEXTVIEW");
            return TYPE_TEXTVIEW;// 编辑框
        } else if (i != dataList.get(position).getPhoto_url()) {
            //Log.e(""+position,"TYPE_IMAGEVIEW");
            return TYPE_IMAGEVIEW;// 按钮
        } else {
            //Log.e(""+position,"o");
            return 0;
        }
    }

    //清除数据
    public void clearData() {
        this.dataList.clear();
    }



    //适配
    public MainAdapter(Context context) {

        mContext = context;

    }

    //有图片事的数据绑定界面
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView content;
        TextView time;
        TextView id,likes;
        TextView url;
        ImageView cover;
        BGABadgeImageView pic;

        ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title3);
            author = (TextView)itemView.findViewById(R.id.author3);
            content = (TextView)itemView.findViewById(R.id.text3);
            time = itemView.findViewById(R.id.tv_time3);
            url = itemView.findViewById(R.id.url);
            cover = itemView.findViewById(R.id.cover);
            id = itemView.findViewById(R.id.id3);
            likes = itemView.findViewById(R.id.likes_number);
            pic = itemView.findViewById(R.id.pic3);

        }
    }

    //当不同的类型数据交由不同的Holder处理
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels ;
        screenHith = dm.heightPixels;

        if (viewType==TYPE_IMAGEVIEW){
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item3, parent, false);
                return new ContentViewHolder(view2);
        }else if (viewType==TYPE_TEXTVIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item4, parent, false);
            return new TextViewHolder(v);
        }
        return null;
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        //return new ViewHolder(v);
    }


    //文本类型数据的绑定界面
    public class  TextViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView author1;
        TextView content1;
        TextView time1;
        TextView id1,likes1;
        BGABadgeImageView pic1;

        TextViewHolder(View itemView) {
            super(itemView);
            title1 = (TextView) itemView.findViewById(R.id.title4);
            author1 = (TextView)itemView.findViewById(R.id.author4);
            content1 = (TextView)itemView.findViewById(R.id.text4);
            time1 = itemView.findViewById(R.id.tv_time4);
            id1 = itemView.findViewById(R.id.id4);
            likes1 = itemView.findViewById(R.id.likes_number4);
            pic1 = itemView.findViewById(R.id.pic4);

        }
    }


    //绑定数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        //加载动画
        //runEnterAnimation(holder.itemView,position);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(25)
                .oval(false)
                .build();


        if (holder instanceof ContentViewHolder) {


            //索引position必须要减去其头部的个数
            Comment feedback = dataList.get(position);
            ((ContentViewHolder) holder).author.setText(feedback.getBmobUser().getUsername());
            ((ContentViewHolder) holder).title.setText(feedback.getName());
            ((ContentViewHolder) holder).content.setText(feedback.getSth());
            ((ContentViewHolder) holder).time.setText(feedback.getCreatedAt());
            ((ContentViewHolder)holder).id.setText(feedback.getObjectId());
            ((ContentViewHolder) holder).url.setText(feedback.getPhoto_url());
            Picasso.with(mContext)
                    .load(feedback.getPhoto_url()).transform(transformation).config(Bitmap.Config.RGB_565)
                    .resize(dp2px(800),dp2px(250)).centerCrop().into(((ContentViewHolder) holder).cover);
            Picasso.with(mContext).load(feedback.getBmobUser().getUserpic_url())
                    .resize(dp2px(100),dp2px(100)).centerCrop().config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.ic_launcher).transform(new CircleTransform()).into(((ContentViewHolder) holder).pic);


            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.addWhereEqualTo("username", feedback.getBmobUser().getUsername());
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> object, BmobException e) {
                    if(e==null){
                        for (MyUser gameScore : object) {
                            id = gameScore.getObjectId();
                            //myUser = gameScore;
                        }
                        BmobQuery<Power> query1 = new BmobQuery<Power>();
                        query1.addWhereEqualTo("User",id);
                        query1.findObjects(new FindListener<Power>() {
                            @Override
                            public void done(List<Power> list, BmobException e) {
                                if(e==null){
                                    for (Power gameScore : list) {
                                        Id = gameScore.getObjectId();
                                        r = gameScore.getRight();
                                    }
                                    //Log.e(""+Id,""+r);
                                    final Bitmap avatarBadgeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_vip1);
                                    if (r.contentEquals("1")){
                                        ((ContentViewHolder) holder).pic.hiddenBadge();

                                    }else if (r.contentEquals("2")){
                                        ((ContentViewHolder) holder).pic.hiddenBadge();

                                    }else if (r.contentEquals("3")){
                                        ((ContentViewHolder) holder).pic.showDrawableBadge(avatarBadgeBitmap);
                                    }else if (r.contentEquals("4")){
                                        ((ContentViewHolder) holder).pic.showDrawableBadge(avatarBadgeBitmap);
                                    }else if (r.contentEquals("5")){
                                        ((ContentViewHolder) holder).pic.showDrawableBadge(avatarBadgeBitmap);
                                    }
                                }
                            }
                        });
                        //Log.e("User id is",id);
                    }
                }
            });


        }else if (holder instanceof TextViewHolder) {


            //索引position必须要减去其头部的个数
            Comment feedback = dataList.get(position);
            ((TextViewHolder) holder).author1.setText(feedback.getBmobUser().getUsername());
            ((TextViewHolder) holder).title1.setText(feedback.getName());
            ((TextViewHolder) holder).content1.setText(feedback.getSth());
            ((TextViewHolder) holder).time1.setText(feedback.getCreatedAt());
            ((TextViewHolder) holder).id1.setText(feedback.getObjectId());
            Picasso.with(mContext).load(feedback.getBmobUser().getUserpic_url())
                    .resize(dp2px(100),dp2px(100)).config(Bitmap.Config.RGB_565)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher).transform(new CircleTransform()).into(((TextViewHolder) holder).pic1);


            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.addWhereEqualTo("username", feedback.getBmobUser().getUsername());
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> object, BmobException e) {
                    if(e==null){
                        for (MyUser gameScore : object) {
                            id = gameScore.getObjectId();
                            //myUser = gameScore;
                        }
                        BmobQuery<Power> query1 = new BmobQuery<Power>();
                        query1.addWhereEqualTo("User",id);
                        query1.findObjects(new FindListener<Power>() {
                            @Override
                            public void done(List<Power> list, BmobException e) {
                                if(e==null){
                                    for (Power gameScore : list) {
                                        Id = gameScore.getObjectId();
                                        r = gameScore.getRight();
                                    }
                                    //Log.e(""+Id,""+r);
                                    final Bitmap avatarBadgeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_vip1);
                                    if (r.contentEquals("1")){
                                        ((TextViewHolder) holder).pic1.hiddenBadge();

                                    }else if (r.contentEquals("2")){
                                        ((TextViewHolder) holder).pic1.hiddenBadge();

                                    }else if (r.contentEquals("3")){
                                        ((TextViewHolder) holder).pic1.showDrawableBadge(avatarBadgeBitmap);
                                    }else if (r.contentEquals("4")){
                                        ((TextViewHolder) holder).pic1.showDrawableBadge(avatarBadgeBitmap);
                                    }else if (r.contentEquals("5")){
                                        ((TextViewHolder) holder).pic1.showDrawableBadge(avatarBadgeBitmap);
                                    }
                                }
                            }
                        });
                        //Log.e("User id is",id);
                    }
                }
            });

        }

        //设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });




    }

    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;              //animationsLocked是布尔类型变量，一开始为false
        //确保仅屏幕一开始能够容纳显示的item项才开启动画



        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，默认-1，
            //这两行代码确保了recyclerview滚动式回收利用视图时不会出现不连续效果
            lastAnimatedPosition = position;
            view.setTranslationY(500);     //Item项一开始相对于原始位置下方500距离
            view.setAlpha(0.f);           //item项一开始完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原始位置


            view.animate()
                    .translationY(0).alpha(1.f)                                //设置最终效果为完全不透明
                    //并且在原来的位置
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间
                    //达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))     //设置动画位移先快后慢的效果
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                            //确保仅屏幕一开始能够显示的item项才开启动画
                            //也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }


    @Override
    //获得内容长度
    public int getItemCount() {
        return  dataList.size();
    }

    public void setOnItemClickListener(MainAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //返回点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }













}
