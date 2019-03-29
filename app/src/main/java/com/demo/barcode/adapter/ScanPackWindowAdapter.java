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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.PositionScanWindow;
import com.demo.architect.data.model.offline.LogScanPackWindowModel;
import com.demo.architect.data.model.offline.ProductPackWindowModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.PositionScanWindowManager;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class ScanPackWindowAdapter extends RealmBaseAdapter<LogScanPackWindowModel> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onClickEditTextListener onClickEditTextListener;
    private String packCode;
    private int numberOnPack;

    public ScanPackWindowAdapter(OrderedRealmCollection<LogScanPackWindowModel> realmResults, OnItemClearListener listener,
                                 OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener,
                                 ScanPackWindowAdapter.onClickEditTextListener onClickEditTextListener, String packCode, int numberOnPack) {
        super(realmResults);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onClickEditTextListener = onClickEditTextListener;
        this.packCode = packCode;
        this.numberOnPack = numberOnPack;
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
            final LogScanPackWindowModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    boolean edit = false;

    private void setDataToViews(HistoryHolder holder, LogScanPackWindowModel item) {
        final ProductPackWindowModel productPackagingModel = item.getProductPackWindowModel();
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
                        holder.edtNumberScan.setText((int) item.getNumberScan() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_number_bigger_zero));
                        return;

                    }
                    if (numberInput == item.getNumberScan()) {
                        return;
                    }
                    PositionScanWindow positionScan = PositionScanWindowManager.getInstance()
                            .getPositionScan();
                    if (positionScan != null) {
                        if (!positionScan.getPackCode().equals(packCode) || positionScan.getNumberPack() != numberOnPack) {
                            holder.edtNumberScan.setText((int) item.getNumberScan() + "");
                            onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                            return;

                        }

                    }

                    if (numberInput > numberOnPack * item.getNumberOnSet()) {
                        holder.edtNumberScan.setText((int) item.getNumberScan() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_quantity_input_bigger_quantity_rest));
                        return;
                    }
                    if (numberInput - item.getNumberScan() > productPackagingModel.getNumberRest()) {
                        holder.edtNumberScan.setText((int) item.getNumberScan() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getString(R.string.text_detail_scan_enough));
                        return;
                    }

                    onEditTextChangeListener.onEditTextChange(item.getId(), Integer.parseInt(holder.edtNumberScan.getText().toString()));

                } catch (Exception e) {

                }
            }
        };
        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(productPackagingModel.getProductSetDetailName());
        holder.txtQuantityProduct.setText((int) productPackagingModel.getNumberTotal() + "");
        holder.txtQuantityRest.setText((int) productPackagingModel.getNumberRest() + "");
        holder.txtQuantityScan.setText((int) productPackagingModel.getNumberScan() + "");
        holder.edtNumberScan.setText(String.valueOf((int) item.getNumberScan()));
        holder.edtNumberScan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onClickEditTextListener.onClick();
                    holder.edtNumberScan.addTextChangedListener(textWatcher);
                } else {

                    holder.edtNumberScan.removeTextChangedListener(textWatcher);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item.getId());
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
            txtPackCode.setVisibility(View.GONE);
            txtSerialPack.setVisibility(View.GONE);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(long logId);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(long logId, int number);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }

    public interface onClickEditTextListener {
        void onClick();
    }

}
