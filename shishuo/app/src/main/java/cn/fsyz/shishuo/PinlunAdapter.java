package cn.fsyz.shishuo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.fsyz.shishuo.Bean.Discuss;
import cn.fsyz.shishuo.AdapterControl.WhiteAdapter;

import com.fsyz.shishuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JALOR on 2018/2/2.
 */


public class PinlunAdapter extends RecyclerView.Adapter<PinlunAdapter.ViewHolder> {

    Context mContext;
    private List<Discuss> dataList = new ArrayList<>();
    private WhiteAdapter.OnItemClickListener onItemClickListener;



    public void addAllData(List<Discuss> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public PinlunAdapter(Context context) {

        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView content;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView)itemView.findViewById(R.id.author_2);
            content = (TextView)itemView.findViewById(R.id.text_2);
            time = itemView.findViewById(R.id.tv_time_2);
        }
    }

    @Override
    public PinlunAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reclycle_view_item_2, parent, false);
        return new PinlunAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PinlunAdapter.ViewHolder holder, final int position) {
        Discuss feedback = dataList.get(position);

        holder.author.setText(feedback.getBmobUser().getUsername());
        holder.content.setText(feedback.getDiscuss());
        holder.time.setText(feedback.getCreatedAt());

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
