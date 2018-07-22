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
import cn.fsyz.shishuo.Bean.Huodong;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by JALOR on 2018/2/19.
 */

//活动列表的适配器
public class HuodongAdapter extends RecyclerView.Adapter<HuodongAdapter.ViewHolder> {

    Context mContext;
    private List<Huodong> dataList = new ArrayList<>();
    private HuodongAdapter.OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;


    //加载信息
    public void addAllData(List<Huodong> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public HuodongAdapter(Context context) {

        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public TextView title;
        public TextView author;
        public TextView content;
        public TextView time;
        public TextView url;
        public TextView id;
        public ImageView cover;



        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title5);
            author = (TextView)itemView.findViewById(R.id.author5);
            content = (TextView)itemView.findViewById(R.id.text5);
            time = itemView.findViewById(R.id.tv_time5);
            url = itemView.findViewById(R.id.url5);
            cover = itemView.findViewById(R.id.cover5);

            id = itemView.findViewById(R.id.id5);
        }
    }

    @Override
    public HuodongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels ;
        screenHith = dm.heightPixels;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item5, parent, false);
        return new HuodongAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HuodongAdapter.ViewHolder holder, final int position) {
        Huodong feedback = dataList.get(position);



        holder.author.setText(feedback.getUser().getUsername());
        holder.title.setText(feedback.getTitle());
        holder.content.setText(feedback.getContent());
        holder.time.setText(feedback.getCreatedAt());
        holder.url.setText(feedback.getPhoto_url());
        holder.id.setText(feedback.getObjectId());
        Picasso.with(mContext).load(feedback.getPhoto_url()).memoryPolicy(NO_CACHE, NO_STORE)
                .resize(800,800).config(Bitmap.Config.RGB_565)
                .centerCrop().into(holder.cover);



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

    public void setOnItemClickListener(HuodongAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }





}
