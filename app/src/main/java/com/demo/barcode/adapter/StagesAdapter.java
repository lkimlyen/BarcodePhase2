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

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.util.ConvertUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class StagesAdapter extends RealmBaseAdapter<LogScanStages> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;

    public StagesAdapter(OrderedRealmCollection<LogScanStages> realmResults, OnItemClearListener listener,
                         OnEditTextChangeListener onEditTextChangeListener, StagesAdapter.onErrorListener onErrorListener) {
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
                    .inflate(R.layout.item_scan_create_pack, parent, false);
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
                        holder.edtNumberScan.setText(item.getNumberInput() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput - item.getNumberInput() > item.getNumberRest()) {
                        holder.edtNumberScan.setText(item.getNumberInput() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest).toString());
                        return;
                    }
                    if (numberInput == item.getNumberRest()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);


                } catch (Exception e) {

                }
            }
        };
        holder.txtRequestCode.setText(item.getBarcode());
        holder.txtDate.setText(ConvertUtils.ConvertStringToShortDate(item.getDateScan()));
        holder.txtQuantityProduct.setText(item.getNumberTotal() + "");
        holder.txtQuantityRest.setText(item.getNumberRest() + "");
        holder.txtQuantityScan.setText(item.getNumberScanned() + "");
        holder.edtNumberScan.setText(String.valueOf(item.getNumberInput()));

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

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtRequestCode;
        TextView txtDate;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        EditText edtNumberScan;

        private HistoryHolder(View v) {
            super(v);
            txtRequestCode = (TextView) v.findViewById(R.id.txt_request_code);
            txtDate = (TextView) v.findViewById(R.id.txt_date);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtQuantityProduct = (TextView) v.findViewById(R.id.txt_quantity_product);
            txtQuantityRest = (TextView) v.findViewById(R.id.txt_quantity_rest);
            txtQuantityScan = (TextView) v.findViewById(R.id.txt_quantity_scan);
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
        void errorListener(String message);
    }
}
