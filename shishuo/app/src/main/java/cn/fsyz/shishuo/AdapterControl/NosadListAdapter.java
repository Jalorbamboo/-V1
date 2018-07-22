package cn.fsyz.shishuo.AdapterControl;

import android.annotation.SuppressLint;
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
import cn.fsyz.shishuo.Bean.Nosad;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jalor on 2018/4/14.
 */

public class NosadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //上下文
    Context mContext;
    private List<Nosad> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;
    private static final int TYPE_TEXTVIEW = 0;//文本
    private static final int TYPE_IMAGEVIEW = 1;//带有图片

    //获得数据类型有无图片
    @Override
    public int getItemViewType(int position) {

            return TYPE_IMAGEVIEW;//图片

    }

    //加载数据
    public void addAllData(List<Nosad> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //清除数据
    public void clearData() {
        this.dataList.clear();
    }

    //使用接口
    public NosadListAdapter(Context context) {

        mContext = context;
    }




    //有图片的处理holder
    public class ViewViewHolder extends RecyclerView.ViewHolder {


        public ImageView cover;
        public TextView time,id;


        ViewViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.mail_image);
            time = itemView.findViewById(R.id.mail_time);
            id = itemView.findViewById(R.id.mail_id);
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
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.mail_item, parent, false);
            return new NosadListAdapter.ViewViewHolder(view2);
        }else if (viewType==TYPE_TEXTVIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_talkopen_wenzi, parent, false);
            return new NosadListAdapter.TextViewHolder(v);
        }
        return null;
    }

    //处理事件
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Nosad feedback = dataList.get(position);



        //holder.author.setText(feedback.getBmobUser().getUsername());


        if (holder instanceof NosadListAdapter.ViewViewHolder) {
            //设置ID
            ((ViewViewHolder) holder).id.setText(feedback.getObjectId());
            //设置时间
            int num =  dataList.size() - position;
            ((NosadListAdapter.ViewViewHolder) holder).time.setText("第"+num+"封");
            //((TalkAdapter.ViewViewHolder) holder).url.setText(feedback.getPhoto());
            //((TalkAdapter.ViewViewHolder) holder).id.setText(feedback.getObjectId());
            //((TalkAdapter.ViewViewHolder) holder).time.setText(feedback.getCreatedAt());
            Picasso.with(mContext).load(R.drawable.mail).config(Bitmap.Config.RGB_565)
                    .into(((NosadListAdapter.ViewViewHolder) holder).cover);
        }else if (holder instanceof NosadListAdapter.TextViewHolder){
            ((NosadListAdapter.TextViewHolder) holder).id1.setText(feedback.getObjectId());
            ((NosadListAdapter.TextViewHolder) holder).content1.setText(feedback.getContent());
            ((NosadListAdapter.TextViewHolder) holder).title1.setText("");
            ((NosadListAdapter.TextViewHolder) holder).time1.setText(feedback.getCreatedAt());
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

    public void setOnItemClickListener(NosadListAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
