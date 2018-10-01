package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class ScanPackagingAdapter extends RealmBaseAdapter<LogScanPackaging> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;

    public ScanPackagingAdapter(OrderedRealmCollection<LogScanPackaging> realmResults, OnItemClearListener listener,
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
                    .inflate(R.layout.item_scan_create_pack, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final LogScanPackaging item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanPackaging item) {
        final ProductPackagingModel productPackagingModel = item.getProductPackagingModel();
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
                        holder.edtNumberScan.setText((int)item.getNumberInput() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_number_bigger_zero));
                        return;

                    }
                    if (numberInput - item.getNumberInput() > productPackagingModel.getNumberRest()) {
                        holder.edtNumberScan.setText((int)item.getNumberInput() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_quantity_input_bigger_quantity_rest));
                        return;
                    }
                    if (numberInput == item.getNumberInput()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };
        holder.txtBarcode.setText(item.getBarcode());
        holder.txtPackCode.setText(item.getCodePack());
        holder.txtSerialPack.setText(item.getSttPack());
        holder.txtNameDetail.setText(productPackagingModel.getProductName());
        holder.txtQuantityProduct.setText((int)productPackagingModel.getNumberTotal() + "");
        holder.txtQuantityRest.setText((int)productPackagingModel.getNumberRest() + "");
        holder.txtQuantityScan.setText((int)productPackagingModel.getNumberScan() + "");
        holder.edtNumberScan.setText(String.valueOf((int)item.getNumberInput()));
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
        if (item.getStatusScan() == Constants.FULL) {
            holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));

        } else {
            holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
        holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtBarcode;
        TextView txtNameDetail;
        TextView txtSerialPack;
        TextView txtPackCode;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        EditText edtNumberScan;
        RelativeLayout layoutMain;


        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtSerialPack = (TextView) v.findViewById(R.id.txt_serial_pack);
            txtPackCode = (TextView) v.findViewById(R.id.txt_pack_code);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_product);
            txtQuantityProduct = (TextView) v.findViewById(R.id.txt_quantity_product);
            txtQuantityRest = (TextView) v.findViewById(R.id.txt_quantity_rest);
            txtQuantityScan = (TextView) v.findViewById(R.id.txt_quantity_scan);
            edtNumberScan = (EditText) v.findViewById(R.id.edt_number);
            layoutMain = (RelativeLayout) v.findViewById(R.id.layout_main);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(LogScanPackaging item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanPackaging item, int number);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }
}
