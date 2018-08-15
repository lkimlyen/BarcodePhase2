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

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
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
                    .inflate(R.layout.item_stages, parent, false);
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
        final ProductDetail productDetail = item.getProductDetail();
        final NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", item.getTimes()).findFirst();
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
                        onErrorListener.errorListener(item, numberInput,CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput - item.getNumberInput() > numberInputModel.getNumberRest()) {
                        onErrorListener.errorListener(item, numberInput, null);
                        return;
                    }
                    if (numberInput == numberInputModel.getNumberRest()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };
        holder.txtBarcode.setText(item.getBarcode());
        holder.txtModule.setText(item.getModule());
        holder.txtQuantityProduct.setText(numberInputModel.getNumberTotal() + "");
        holder.txtQuantityRest.setText(numberInputModel.getNumberRest() + "");
        holder.txtQuantityScan.setText(numberInputModel.getNumberSuccess() + "");
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

        TextView txtBarcode;
        TextView txtModule;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        EditText edtNumberScan;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
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
        void errorListener(LogScanStages item, int number, String message);
    }
}
