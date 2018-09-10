package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class QualityControlAdapter extends RealmBaseAdapter<QualityControlModel> implements ListAdapter {
    private OnItemClearListener listener;

    public QualityControlAdapter(OrderedRealmCollection<QualityControlModel> realmResults, OnItemClearListener listener) {
        super(realmResults);
        this.listener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_quality_control, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final QualityControlModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, QualityControlModel item) {

        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductName());
        holder.txtModule.setText(item.getModule());
        holder.txtTotal.setText(item.getTotalNumber() + "");

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        holder.layoutMain.setBackgroundColor(item.isEdit() ? CoreApplication.getInstance().getResources().getColor(R.color.colorGreen)
        : CoreApplication.getInstance().getResources().getColor(android.R.color.white));

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtBarcode;
        TextView txtNameDetail;
        TextView txtModule;
        ImageView imgDelete;
        TextView txtTotal;
        LinearLayout layoutMain;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtModule = (TextView) v.findViewById(R.id.txt_serial_module);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            txtTotal = (TextView) v.findViewById(R.id.txt_number_order);
            layoutMain = (LinearLayout) v.findViewById(R.id.layout_main);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(QualityControlModel item);
    }


}
