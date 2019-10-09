package com.demo.barcode.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.barcode.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;

public class ReasonAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReasonsEntity> list;
    private Set<Integer> countersToSelect = new HashSet<Integer>();

    public ReasonAdapter(Context c, List<ReasonsEntity> list ) {
        mContext = c;
        this.list = list;
    }

    public void setListCounterSelect(RealmList<Integer> list){
        countersToSelect = new HashSet<Integer>(list);
        notifyDataSetChanged();
    }

    public Set<Integer> getCountersToSelect() {
        return countersToSelect;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ServiceViewHolder holder;
        if (convertView == null) {
            holder = new ServiceViewHolder();
            convertView = inflater.inflate(R.layout.item_reason, null);

            holder.cbReason = (CheckBox) convertView.findViewById(R.id.cb_reason);
            convertView.setTag(holder);
        } else {
            holder = (ServiceViewHolder) convertView.getTag();
        }
        holder.cbReason.setText(list.get(position).getReason());
        holder.cbReason.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    countersToSelect.add(list.get(position).getReasonId());
                }else {
                    countersToSelect.remove(list.get(position).getReasonId());
                }

            }
        });

        holder.cbReason.setChecked(countersToSelect.contains(list.get(position).getReasonId()));
        return convertView;
    }


    public class ServiceViewHolder {
        private CheckBox cbReason;

    }

}
