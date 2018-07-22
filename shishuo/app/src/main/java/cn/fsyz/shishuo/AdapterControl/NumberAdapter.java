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

import cn.fsyz.shishuo.Bean.MyUser;
import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by Jalor on 2018/3/17.
 */

//统计人数的适配器
public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    Context mContext;
    private List<MyUser> dataList = new ArrayList<>();
    private HuodongAdapter.OnItemClickListener onItemClickListener;
    int screenWidth,screenHith;


    public void addAllData(List<MyUser> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public NumberAdapter(Context context) {

        mContext = context;
    }

    //绑定item界面
    public class ViewHolder extends RecyclerView.ViewHolder {



        public TextView rg;
        public TextView author;
        public TextView content;
        public TextView time;
        public TextView url;
        public TextView id;
        public ImageView cover;



        public ViewHolder(View itemView) {
            super(itemView);
            rg = (TextView) itemView.findViewById(R.id.title4);
            author = (TextView)itemView.findViewById(R.id.author4);
            content = (TextView)itemView.findViewById(R.id.text4);
            time = itemView.findViewById(R.id.tv_time4);
            id = itemView.findViewById(R.id.id4);
            cover = itemView.findViewById(R.id.pic4);
        }
    }

    //适配器返回界面
    @Override
    public NumberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels ;
        screenHith = dm.heightPixels;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item4, parent, false);
        return new NumberAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NumberAdapter.ViewHolder holder, final int position) {

        MyUser feedback = dataList.get(position);



        holder.author.setText(feedback.getUsername());
        holder.time.setText(feedback.getCreatedAt());
        holder.id.setText(feedback.getObjectId());
        holder.content.setText(feedback.getRealname());
        holder.rg.setText(feedback.getSchool()+"/"+feedback.getGrade()+"/"+feedback.getClasses());
        Picasso.with(mContext).load(feedback.getUserpic_url()).memoryPolicy(NO_CACHE, NO_STORE)
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



    //得到item的参数
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(HuodongAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //设置点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }





}