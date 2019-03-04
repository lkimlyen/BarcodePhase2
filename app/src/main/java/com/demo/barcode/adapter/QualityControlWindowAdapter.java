package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class QualityControlWindowAdapter extends RealmBaseAdapter<QualityControlWindowModel> implements ListAdapter {
    private OnItemClearListener listener;

    public QualityControlWindowAdapter(OrderedRealmCollection<QualityControlWindowModel> realmResults, OnItemClearListener listener) {
        super(realmResults);
        this.listener = listener;
    }

    public int countDataEdit() {
        int count = 0;
        if (adapterData != null) {
            for (QualityControlWindowModel model : adapterData){
                if(model.isEdit()){
                    count++;
                }
            }
        }
        return count;
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
            final QualityControlWindowModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, QualityControlWindowModel item) {

        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductSetDetailName());
        holder.txtModule.setText(item.getProductSetName());
        holder.txtTotal.setText(item.getTotalNumber() + "");
        holder.tvTitle.setText(CoreApplication.getInstance().getString(R.string.text_window_name));
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item.getId());
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
        TextView tvTitle;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtModule = (TextView) v.findViewById(R.id.txt_serial_module);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            txtTotal = (TextView) v.findViewById(R.id.txt_number_order);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            layoutMain = (LinearLayout) v.findViewById(R.id.layout_main);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(long id);
    }


}
