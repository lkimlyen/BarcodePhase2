package com.demo.barcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReasonAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReasonsEntity> list;
    private Set<ReasonsEntity> countersToSelect = new HashSet<ReasonsEntity>();

    public ReasonAdapter(Context c, List<ReasonsEntity> list ) {
        mContext = c;
        this.list = list;
    }

    public Set<ReasonsEntity> getCountersToSelect() {
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
                    countersToSelect.add(list.get(position));
                }else {
                    countersToSelect.remove(list.get(position));
                }

            }
        });

        holder.cbReason.setChecked(countersToSelect.contains(list.get(position)));
        return convertView;
    }


    public class ServiceViewHolder {
        private CheckBox cbReason;

    }

}
