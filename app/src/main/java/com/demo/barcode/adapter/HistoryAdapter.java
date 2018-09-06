package com.demo.barcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.barcode.R;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryEntity> list;

    public HistoryAdapter(Context c, List<HistoryEntity> list ) {
        mContext = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_history, null);

            holder.txtDateCreate = (TextView) convertView.findViewById(R.id.txt_date_create);

            holder.txtTotal = (TextView) convertView.findViewById(R.id.txt_total);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtDateCreate.setText(list.get(position).getDateTime());
        holder.txtTotal.setText(String.valueOf(list.get(position).totalQuantity()));

        return convertView;
    }


    public class ViewHolder {
        private TextView txtTotal;
        private TextView txtDateCreate;


    }

}
