package cn.fsyz.shishuo.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.fsyz.shishuo.R;

import java.util.List;

/**
 * Created by JALOR on 2017/3/23.
 */

public class textAdapter extends ArrayAdapter<text> {

    private int resourceId;

    public textAdapter(Context context, int textViewResourceId,
                        List<text> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        text fruit = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.fruitName = (TextView) view.findViewById(R.id.sys_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.fruitName.setText(fruit.getName());
        return view;
    }

    class ViewHolder {


        TextView fruitName;

    }

}
