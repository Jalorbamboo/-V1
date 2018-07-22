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

import cn.fsyz.shishuo.Bean.WhiteComment;
import com.fsyz.shishuo.R;
import cn.fsyz.shishuo.Utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.dp2px;

/**
 * Created by JALOR on 2018/1/30.
 */
//真心话的列表适配器
public class WhiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXTVIEW = 0;//文本
    private static final int TYPE_IMAGEVIEW = 1;//带有图片
    Context mContext;
    private List<WhiteComment> dataList = new ArrayList<>();
    private WhiteAdapter.OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;


    //获得数据类型有无图片
    @Override
    public int getItemViewType(int position) {
        String i = "";
        if (null == dataList.get(position).getImage_url()) {
            //Log.e(""+position,"TYPE_TEXTVIEW");
            return TYPE_TEXTVIEW;// 编辑框
        } else if (i != dataList.get(position).getImage_url()) {
            //Log.e(""+position,"TYPE_IMAGEVIEW");
            return TYPE_IMAGEVIEW;// 按钮
        } else {
            //Log.e(""+position,"o");
            return 0;
        }
    }

    public void addAllData(List<WhiteComment> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public WhiteAdapter(Context context) {

        mContext = context;
    }

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
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item3, parent, false);
            return new WhiteAdapter.ViewViewHolder(view2);
        }else if (viewType==TYPE_TEXTVIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item4, parent, false);
            return new WhiteAdapter.TextViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        WhiteComment feedback = dataList.get(position);



            //holder.author.setText(feedback.getBmobUser().getUsername());
        if (holder instanceof ViewViewHolder) {
            ((ViewViewHolder) holder).title.setText(feedback.getName());
            ((ViewViewHolder) holder).content.setText(feedback.getSth());
            ((ViewViewHolder) holder).time.setText(feedback.getCreatedAt());
            ((ViewViewHolder) holder).url.setText(feedback.getImage_url());
            ((ViewViewHolder) holder).id.setText(feedback.getObjectId());
            Picasso.with(mContext).load(feedback.getImage_url()).config(Bitmap.Config.RGB_565)
                    .resize(dp2px(800),dp2px(250)).centerCrop().into(((ViewViewHolder) holder).cover);
            Picasso.with(mContext).load(R.mipmap.ic_launcher_round).config(Bitmap.Config.RGB_565)
                    .fit().into(((ViewViewHolder) holder).pic);


        }else if (holder instanceof TextViewHolder){
            ((WhiteAdapter.TextViewHolder) holder).title1.setText(feedback.getName());
            ((WhiteAdapter.TextViewHolder) holder).content1.setText(feedback.getSth());
            ((WhiteAdapter.TextViewHolder) holder).time1.setText(feedback.getCreatedAt());
            ((WhiteAdapter.TextViewHolder) holder).id1.setText(feedback.getObjectId());
            Picasso.with(mContext).load(R.mipmap.ic_launcher_round)
                    .resize(dp2px(100),dp2px(100)).config(Bitmap.Config.RGB_565)
                    .centerCrop()
                    .transform(new CircleTransform()).into(((WhiteAdapter.TextViewHolder) holder).pic1);

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

    public void setOnItemClickListener(WhiteAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }





}
