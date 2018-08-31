package com.demo.barcode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class ProductHistoryAdapter extends BaseAdapter {
    private List<ProductPackagingEntity> list;

    public ProductHistoryAdapter( List<ProductPackagingEntity> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ProductPackagingEntity getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        final ProductPackagingEntity item = getItem(position);
        setDataToViews(viewHolder, item);

        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, ProductPackagingEntity item) {
        holder.txtCodeColor.setText(item.getProductColor());
        holder.txtHeight.setText(item.getHeight() + "");
        holder.txtLength.setText(item.getLength() + "");
        holder.txtWidth.setText(item.getWidth() + "");
        holder.txtNumber.setText(item.getNumber()+ "");
        holder.txtNameProduct.setText(item.getProductName());
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
