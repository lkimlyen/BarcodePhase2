package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class GroupCodeContentAdapter extends RealmBaseAdapter<GroupCode> implements ListAdapter {
    private OnRemoveListener onRemoveListener;

    public GroupCodeContentAdapter(OrderedRealmCollection<GroupCode> realmResults, OnRemoveListener onRemoveListener) {
        super(realmResults);
        this.onRemoveListener = onRemoveListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_code_in_group, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final GroupCode item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, GroupCode item) {

        holder.txtNameDetail.setText(item.getProductDetailName());
        holder.txtNumberGroup.setText(String.valueOf(item.getNumber()));
        holder.txtNumberScan.setText(String.valueOf(item.getNumberTotal()));
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveListener.onRemove(item);
            }
        });


    }


    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameDetail;
        TextView txtNumberGroup;
        TextView txtNumberScan;
        ImageView imgRemove;

        private HistoryHolder(View v) {
            super(v);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            txtNumberGroup = (TextView) v.findViewById(R.id.txt_number_group);
            txtNumberScan = (TextView) v.findViewById(R.id.txt_number_scan);
            imgRemove = (ImageView) v.findViewById(R.id.btn_remove);
        }

    }


    public interface OnRemoveListener {
        void onRemove(GroupCode item);
    }
}
