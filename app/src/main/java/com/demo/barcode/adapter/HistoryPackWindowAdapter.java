package com.demo.barcode.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.barcode.R;

import java.util.List;

public class HistoryPackWindowAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryPackWindowEntity> listModule;
    private OnClickPrintListener listener;

    public HistoryPackWindowAdapter(Context context, List<HistoryPackWindowEntity> listDataHeader, OnClickPrintListener listener) {
        this.context = context;
        this.listModule = listDataHeader;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return listModule.size();
    }

    @Override
    public Object getItem(int position) {
        return listModule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listModule.get(position).getMasterId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_pack_window, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        final HistoryPackWindowEntity item = (HistoryPackWindowEntity) getItem(position);
        setDataToViews(viewHolder, item);
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, HistoryPackWindowEntity item) {
        holder.tvBarcode.setText(item.getBarcode());
        holder.tvBarcodeName.setText(item.getBarcodeName());
        holder.tvNumberOnPack.setText(String.valueOf(item.getNumberSetOnPack()));
        holder.tvPackCode.setText(item.getPackCode());
        if (!TextUtils.isEmpty(item.getPackName()))
            holder.tvPackName.setText(item.getPackName());

        holder.btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPrint(item.getMasterId());
            }
        });

    }


    public class HistoryHolder {

        TextView tvBarcode;
        TextView tvBarcodeName;
        TextView tvPackCode;
        TextView tvNumberOnPack;
        TextView tvPackName;
        ImageView btPrint;


        private HistoryHolder(View v) {
            tvBarcode = (TextView) v
                    .findViewById(R.id.tv_barcode);
            tvBarcodeName = (TextView) v
                    .findViewById(R.id.tv_barcode_name);
            tvPackCode = (TextView) v.findViewById(R.id.tv_pack_code);
            tvNumberOnPack = (TextView) v.findViewById(R.id.tv_number_on_pack);
            btPrint =  v.findViewById(R.id.bt_print);
            tvPackName = (TextView) v
                    .findViewById(R.id.tv_pack_name);
        }

    }


    public interface OnClickPrintListener {
        void onClickPrint(long id);
    }

}
