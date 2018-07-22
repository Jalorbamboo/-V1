package cn.fsyz.shishuo.AdapterControl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import cn.fsyz.shishuo.Bean.Discuss;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

/**
 * Created by Jalor on 2018/4/14.
 */

public class DiscuzzAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上下文
    Context mContext;
    private List<Discuss> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;
    private static final int TYPE_TEXTVIEW = 0;//文本
    private static final int TYPE_IMAGEVIEW = 1;//带有图片

    @Override
    public int getItemViewType(int position) {

            return TYPE_TEXTVIEW;// 编辑框


    }

    //加载数据
    public void addAllData(List<Discuss> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //清除数据
    public void clearData() {
        this.dataList.clear();
    }

    //使用接口
    public DiscuzzAdapter(Context context) {

        mContext = context;
    }




    //有图片的处理holder
    public class ViewViewHolder extends RecyclerView.ViewHolder {


        public TextView title;
        public TextView author;
        public TextView content;
        public TextView time;
        public TextView url;
        public TextView id;
        public ImageView cover,pic;

        public ViewViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title3);
            author = (TextView)itemView.findViewById(R.id.author3);
            content = (TextView)itemView.findViewById(R.id.text3);
            time = itemView.findViewById(R.id.tv_time3);
            url = itemView.findViewById(R.id.url);
            cover = itemView.findViewById(R.id.cover);
            pic = itemView.findViewById(R.id.pic3);
            id = itemView.findViewById(R.id.id3);
        }
    }

    //文本类型数据的绑定界面
    public class  TextViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        //TextView author1;
        TextView content1;
        TextView time1;
        TextView id1,likes1;
        BGABadgeImageView pic1;

        TextViewHolder(View itemView) {
            super(itemView);
            title1 = (TextView) itemView.findViewById(R.id.title4);
            //author1 = (TextView)itemView.findViewById(R.id.author4);
            content1 = (TextView)itemView.findViewById(R.id.text4);
            time1 = itemView.findViewById(R.id.tv_time4);
            id1 = itemView.findViewById(R.id.id4);
            likes1 = itemView.findViewById(R.id.likes_number4);
            pic1 = itemView.findViewById(R.id.pic4);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels ;
        screenHith = dm.heightPixels;

        if (viewType==TYPE_IMAGEVIEW){
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.mail_item, parent, false);
            return new DiscuzzAdapter.ViewViewHolder(view2);
        }else if (viewType==TYPE_TEXTVIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item4, parent, false);
            return new DiscuzzAdapter.TextViewHolder(v);
        }
        return null;
    }

    //处理事件
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Discuss feedback = dataList.get(position);



        //holder.author.setText(feedback.getBmobUser().getUsername());


        if (holder instanceof DiscuzzAdapter.ViewViewHolder) {
            //设置ID
            ((DiscuzzAdapter.ViewViewHolder) holder).id.setText(feedback.getObjectId());
            //设置时间
            ((DiscuzzAdapter.ViewViewHolder) holder).time.setText(feedback.getCreatedAt());
            //((TalkAdapter.ViewViewHolder) holder).url.setText(feedback.getPhoto());
            //((TalkAdapter.ViewViewHolder) holder).id.setText(feedback.getObjectId());
            ((DiscuzzAdapter.ViewViewHolder) holder).content.setText(feedback.getDiscuss());
            Picasso.with(mContext).load(R.drawable.mail).config(Bitmap.Config.RGB_565)
                    .into(((DiscuzzAdapter.ViewViewHolder) holder).cover);
            ((DiscuzzAdapter.ViewViewHolder) holder).title.setText("你有老爷爷的回信");
        }else if (holder instanceof DiscuzzAdapter.TextViewHolder){
            ((DiscuzzAdapter.TextViewHolder) holder).id1.setText(feedback.getObjectId());
            ((DiscuzzAdapter.TextViewHolder) holder).content1.setText(feedback.getDiscuss());
            ((DiscuzzAdapter.TextViewHolder) holder).title1.setText("你有老爷爷的回信");
            ((DiscuzzAdapter.TextViewHolder) holder).time1.setText(feedback.getCreatedAt());
            Picasso.with(mContext).load(R.drawable.grandpa).config(Bitmap.Config.RGB_565).
                    into(((TextViewHolder) holder).pic1);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(DiscuzzAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

}
