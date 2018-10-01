package com.demo.barcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrintStampAdapter extends BaseAdapter {
    private Context mContext;
    private List<LogScanPackaging> list;

    public PrintStampAdapter(Context c, List<LogScanPackaging> list) {
        mContext = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LogScanPackaging getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_print_pack, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }
            final LogScanPackaging item = getItem(position);
            setDataToViews(viewHolder, item);

        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanPackaging item) {
        holder.txtCodeColor.setText(item.getProductPackagingModel().getProductColor());
        holder.txtHeight.setText((int)item.getProductPackagingModel().getHeight() + "");
        holder.txtLength.setText((int)item.getProductPackagingModel().getLength() + "");
        holder.txtWidth.setText((int)item.getProductPackagingModel().getWidth() + "");
        holder.txtNumber.setText((int)item.getNumberInput() + "");
        holder.txtNameProduct.setText(item.getProductPackagingModel().getProductName());
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameProduct;
        TextView txtWidth;
        TextView txtHeight;
        TextView txtLength;
        TextView txtCodeColor;
        TextView txtNumber;

        private HistoryHolder(View v) {
            super(v);
            txtNameProduct = (TextView) v.findViewById(R.id.txt_name_product);
            txtWidth = (TextView) v.findViewById(R.id.txt_width);
            txtHeight = (TextView) v.findViewById(R.id.txt_height);
            txtLength = (TextView) v.findViewById(R.id.txt_lenght);
            txtCodeColor = (TextView) v.findViewById(R.id.txt_code_color);
            txtNumber = (TextView) v.findViewById(R.id.txt_number);
        }
    }

}
