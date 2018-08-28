package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class GroupCodeLVAdapter extends RealmBaseAdapter<LogScanStages> implements ListAdapter {
    private boolean inChooseMode = true;
    private Set<LogScanStages> countersToSelect = new HashSet<LogScanStages>();

    void enableDeletionMode(boolean enabled) {
        inChooseMode = enabled;
        if (!enabled) {
            countersToSelect.clear();
        }
        notifyDataSetChanged();
    }

   public Set<LogScanStages> getCountersToSelect() {
        return countersToSelect;
    }

    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;

    public GroupCodeLVAdapter(OrderedRealmCollection<LogScanStages> realmResults, OnEditTextChangeListener onEditTextChangeListener, GroupCodeLVAdapter.onErrorListener onErrorListener) {
        super(realmResults);
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group_code, parent, false);
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
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int numberInput = Integer.parseInt(s.toString());
                    if (numberInput <= 0) {
                        holder.edtNumberGroup.setText(item.getNumberInput() + "");
                        onErrorListener.errorListener(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput > item.getNumberInput()) {
                        onErrorListener.errorListener(item, numberInput, null);
                        return;
                    }
                    if (numberInput == item.getNumberGroup()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };
        holder.txtNameDetail.setText(item.getProductDetail().getProductName());
        holder.edtNumberGroup.setText(String.valueOf(item.getNumberGroup()));
        holder.txtNumberScan.setText(String.valueOf(item.getNumberInput()));
        holder.edtNumberGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.edtNumberGroup.addTextChangedListener(textWatcher);
                } else {
                    holder.edtNumberGroup.removeTextChangedListener(textWatcher);
                }
            }
        });

        if (inChooseMode) {
            holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        countersToSelect.add(item);
                    }else {
                        countersToSelect.remove(item);
                    }

                }
            });
        } else {
            holder.cbSelect.setOnCheckedChangeListener(null);
        }
        holder.cbSelect.setChecked(countersToSelect.contains(item));
        holder.cbSelect.setVisibility(inChooseMode ? View.VISIBLE : View.GONE);

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameDetail;
        EditText edtNumberGroup;
        TextView txtNumberScan;
        CheckBox cbSelect;

        private HistoryHolder(View v) {
            super(v);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            edtNumberGroup = (EditText) v.findViewById(R.id.edt_number);
            txtNumberScan = (TextView) v.findViewById(R.id.txt_number_scan);
            cbSelect = (CheckBox) v.findViewById(R.id.cb_select);
        }

    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanStages item, int number);
    }

    public interface onErrorListener {
        void errorListener(LogScanStages item, int number, String message);
    }
}
