package com.demo.barcode.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanPackWindowModel;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class PrintStampWindowAdapter extends RealmBaseAdapter<LogScanPackWindowModel> implements ListAdapter {
    public PrintStampWindowAdapter(OrderedRealmCollection<LogScanPackWindowModel> realmResults) {
        super(realmResults);

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
            final LogScanPackWindowModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanPackWindowModel item) {
        holder.txtCodeColor.setText(item.getProductPackWindowModel().getProductSetDetailCode());
        holder.txtHeight.setText((int)item.getProductPackWindowModel().getHeight() + "");
        holder.txtLength.setText((int)item.getProductPackWindowModel().getLength() + "");
        holder.txtWidth.setText((int)item.getProductPackWindowModel().getWidth() + "");
        holder.txtNumber.setText((int)item.getNumberScan() + "");
        holder.txtNameProduct.setText(item.getProductPackWindowModel().getProductSetDetailName());
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
