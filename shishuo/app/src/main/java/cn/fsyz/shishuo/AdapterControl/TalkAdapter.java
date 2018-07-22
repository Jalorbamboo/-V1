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
import cn.fsyz.shishuo.Bean.Talk;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.dp2px;

/**
 * Created by JALOR on 2018/3/1.
 */

//每周的参与人的发言列表适配器
public class TalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private List<Talk> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;
    private static final int TYPE_TEXTVIEW = 0;//文本
    private static final int TYPE_IMAGEVIEW = 1;//带有图片


    //获得数据类型有无图片
    @Override
    public int getItemViewType(int position) {
        String i = "";
        if (null == dataList.get(position).getPhoto()) {
            //Log.e(""+position,"TYPE_TEXTVIEW");
            return TYPE_TEXTVIEW;// 编辑框
        } else if (i != dataList.get(position).getPhoto()) {
            //Log.e(""+position,"TYPE_IMAGEVIEW");
            return TYPE_IMAGEVIEW;// 按钮
        } else {
            //Log.e(""+position,"o");
            return 0;
        }
    }

    public void addAllData(List<Talk> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public TalkAdapter(Context context) {

        mContext = context;
    }

    //有图片的处理holder
    public class ViewViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView content;
        public TextView time;
        public TextView url;
        public TextView id;
        public ImageView cover;

        ViewViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title1);
            content = (TextView)itemView.findViewById(R.id.text1);
            time = itemView.findViewById(R.id.tv_time1);
            url = itemView.findViewById(R.id.url1);
            cover = itemView.findViewById(R.id.cover1);
            id = itemView.findViewById(R.id.id1);
        }
    }

    //文本类型数据的绑定界面
    public class  TextViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView content1;
        TextView time1;
        TextView id1;

        TextViewHolder(View itemView) {
            super(itemView);
            title1 = (TextView) itemView.findViewById(R.id.title_talkopen_w);
            content1 = (TextView)itemView.findViewById(R.id.text_talkopen_w);
            time1 = itemView.findViewById(R.id.tv_time_talkopen_w);
            id1 = itemView.findViewById(R.id.id_talkopen_w);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels ;
        screenHith = dm.heightPixels;

        if (viewType==TYPE_IMAGEVIEW){
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
            return new TalkAdapter.ViewViewHolder(view2);
        }else if (viewType==TYPE_TEXTVIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_talkopen_wenzi, parent, false);
            return new TalkAdapter.TextViewHolder(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Talk feedback = dataList.get(position);



        //holder.author.setText(feedback.getBmobUser().getUsername());


        if (holder instanceof TalkAdapter.ViewViewHolder) {
            //设置标题
            ((TalkAdapter.ViewViewHolder) holder).title.setText(feedback.getTitle());
            //设置内容
            ((TalkAdapter.ViewViewHolder) holder).content.setText(feedback.getContent());
            ((TalkAdapter.ViewViewHolder) holder).url.setText(feedback.getPhoto());
            ((TalkAdapter.ViewViewHolder) holder).id.setText(feedback.getObjectId());
            ((TalkAdapter.ViewViewHolder) holder).time.setText(feedback.getCreatedAt());
            Picasso.with(mContext).load(feedback.getPhoto()).config(Bitmap.Config.RGB_565)
                    .resize(dp2px(800), dp2px(250)).centerCrop().into(((TalkAdapter.ViewViewHolder) holder).cover);
        }else if (holder instanceof TalkAdapter.TextViewHolder){
            ((TalkAdapter.TextViewHolder) holder).id1.setText(feedback.getObjectId());
            ((TalkAdapter.TextViewHolder) holder).content1.setText(feedback.getContent());
            ((TalkAdapter.TextViewHolder) holder).title1.setText(feedback.getTitle());
            ((TalkAdapter.TextViewHolder) holder).time1.setText(feedback.getCreatedAt());
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

    public void setOnItemClickListener(TalkAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }





}
