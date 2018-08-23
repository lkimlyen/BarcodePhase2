package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class DetailPrintTempAdapter extends RealmBaseAdapter<LogScanPackaging> implements ListAdapter {

    public DetailPrintTempAdapter(OrderedRealmCollection<LogScanPackaging> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_print_pack, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }
        if (adapterData != null) {
            final LogScanPackaging item = adapterData.get(position);
            setDataToViews(viewHolder, item);
        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanPackaging item) {
        holder.txtCodeColor.setText(item.getProductPackagingModel().getProductColor());
        holder.txtHeight.setText(item.getProductPackagingModel().getHeight() + "");
        holder.txtLength.setText(item.getProductPackagingModel().getLength() + "");
        holder.txtWidth.setText(item.getProductPackagingModel().getWidth() + "");
        holder.txtNumber.setText(item.getNumberInput() + "");
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
