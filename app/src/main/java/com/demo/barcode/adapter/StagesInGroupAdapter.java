package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class StagesInGroupAdapter extends RealmBaseAdapter<LogScanStages> implements ListAdapter {

    public StagesInGroupAdapter(OrderedRealmCollection<LogScanStages> realmResults) {
        super(realmResults);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_in_group, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final LogScanStages item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanStages item) {
        final ProductDetail productDetail = item.getProductDetail();

        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductDetail().getProductName());
        holder.txtModule.setText(item.getModule());
        holder.txtQuantityProduct.setText(productDetail.getNumberTotal() + "");
        holder.txtQuantityRest.setText(productDetail.getNumberRest() + "");
        holder.txtQuantityScan.setText(productDetail.getNumberSuccess() + "");
        holder.txtNumberScan.setText(String.valueOf(item.getNumberInput()));



        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onItemClick(item);
            }
        });

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtBarcode;
        TextView txtNameDetail;
        TextView txtModule;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        TextView txtNumberScan;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            txtQuantityProduct = (TextView) v.findViewById(R.id.txt_quantity_product);
            txtQuantityRest = (TextView) v.findViewById(R.id.txt_quantity_rest);
            txtQuantityScan = (TextView) v.findViewById(R.id.txt_quantity_scan);
            txtNumberScan = (TextView) v.findViewById(R.id.txt_number);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(LogScanStages item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanStages item, int number);
    }

    public interface onErrorListener {
        void errorListener(LogScanStages item, int number, String message);
    }
}
