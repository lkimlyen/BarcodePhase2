package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.screen.detail_error.DetailErrorActivity;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class QualityControlAdapter extends RealmRecyclerViewAdapter<QualityControlModel, QualityControlAdapter.HistoryHolder> {

    private OnItemClearListener listener;
    private OnItemClickListener onItemClickListener;

    public QualityControlAdapter(OrderedRealmCollection<QualityControlModel> realmResults, OnItemClearListener listener, OnItemClickListener onItemClickListener) {
        super(realmResults, true);
        this.listener = listener;
        this.onItemClickListener = onItemClickListener;
        setHasStableIds(true);
    }
    public int countDataEdit() {
        int count = 0;
        if (getData() != null) {
            for (QualityControlModel model : getData()){
                if(model.isEdit()){
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quality_control, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final QualityControlModel obj = getItem(position);
        setDataToViews(holder, obj);
        holder.bind(obj.getId(),onItemClickListener);

    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    private void setDataToViews(HistoryHolder holder, QualityControlModel item) {

        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductName());
        holder.txtModule.setText(item.getModule());
        holder.txtTotal.setText((int)item.getTotalNumber() + "");

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

        private void bind(long id, OnItemClickListener onItemClickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onItemClickListener.onItemClick(id);
                }
            });
        }
    }

    public interface OnItemClearListener {
        void onItemClick(QualityControlModel item);
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }
}
