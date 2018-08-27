package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class GroupCodeContentAdapter extends RealmBaseAdapter<GroupCode> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;

    public GroupCodeContentAdapter(OrderedRealmCollection<GroupCode> realmResults, OnItemClearListener listener,
                                   OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener) {
        super(realmResults);
        this.listener = listener;
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
            final GroupCode item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, GroupCode item) {
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
//                    int numberInput = Integer.parseInt(s.toString());
//                    if (numberInput <= 0) {
//                        holder.edtNumberScan.setText(item.getNumberInput() + "");
//                        onErrorListener.errorListener(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
//                        return;
//
//                    }
//                    if (numberInput - item.getNumberInput() > numberInputModel.getNumberRest()) {
//                        onErrorListener.errorListener(item, numberInput, null);
//                        return;
//                    }
//                    if (numberInput == item.getNumberInput()) {
//                        return;
//                    }
//                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };
        holder.txtNameDetail.setText(item.getProductDetailName());
        holder.edtNumberScan.setText(String.valueOf(item.getNumber()));

        holder.edtNumberScan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.edtNumberScan.addTextChangedListener(textWatcher);
                } else {

                    holder.edtNumberScan.removeTextChangedListener(textWatcher);
                }
            }
        });

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameDetail;
        EditText edtNumberScan;

        private HistoryHolder(View v) {
            super(v);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            edtNumberScan = (EditText) v.findViewById(R.id.edt_number);
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
