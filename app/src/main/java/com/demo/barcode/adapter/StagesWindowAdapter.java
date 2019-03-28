package com.demo.barcode.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class StagesWindowAdapter extends RealmRecyclerViewAdapter<LogScanStagesWindowModel, StagesWindowAdapter.HistoryHolder> {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private OnErrorListener onErrorListener;
    private Context mContext;

    public StagesWindowAdapter(OrderedRealmCollection<LogScanStagesWindowModel> realmResults, OnItemClearListener listener,
                               OnEditTextChangeListener onEditTextChangeListener, OnErrorListener onErrorListener, Context mContext) {
        super(realmResults, true);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.mContext = mContext;

        setHasStableIds(true);
    }



    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_stages_window, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final LogScanStagesWindowModel obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, LogScanStagesWindowModel item) {
        final ProductDetailWindowModel productDetail = item.getProductDetail();
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
                        holder.etNumberScan.setText(item.getNumberInput() + "");
                        onErrorListener.onError(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }

                    if (numberInput == item.getNumberInput()) {
                        return;
                    }
                    if (numberInput - item.getNumberInput() > productDetail.getNumberRest()) {
                        onErrorListener.onError(item, numberInput, null);
                        return;
                    }

                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };
        holder.tvBarcode.setText(item.getBarcode());
        holder.tvNameDetail.setText(productDetail.getProductSetDetailName());
        holder.tvProduct.setText(productDetail.getProductSetName());
        holder.tvQuantityProduct.setText( productDetail.getNumberTotal() + "");
        holder.tvQuantityRest.setText( productDetail.getNumberRest() + "");
        holder.tvQuantityScan.setText(productDetail.getNumberSuccess() + "");
        holder.etNumberScan.setText(String.valueOf((int) item.getNumberInput()));
        holder.etNumberScan.setSelection(holder.etNumberScan.getText().length());
        holder.etNumberScan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    holder.etNumberScan.addTextChangedListener(textWatcher);
                } else {

                    holder.etNumberScan.removeTextChangedListener(textWatcher);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        if (productDetail.getNumberRest() > 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.tvQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

            holder.etNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
        } else if (productDetail.getNumberRest() == 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));
            holder.etNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
            holder.tvQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

        } else {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorYellow));
            holder.etNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.tvQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView tvBarcode;
        TextView tvNameDetail;
        TextView tvProduct;
        ImageView imgDelete;
        TextView tvQuantityProduct;
        TextView tvQuantityRest;
        TextView tvQuantityScan;
        EditText etNumberScan;
        RelativeLayout llMain;

        private HistoryHolder(View v) {
            super(v);
            tvBarcode = (TextView) v.findViewById(R.id.tv_barcode);
            tvProduct = (TextView) v.findViewById(R.id.tv_product);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            tvNameDetail = (TextView) v.findViewById(R.id.tv_name_detail);
            tvQuantityProduct = (TextView) v.findViewById(R.id.tv_quantity_product);
            tvQuantityRest = (TextView) v.findViewById(R.id.tv_quantity_rest);
            tvQuantityScan = (TextView) v.findViewById(R.id.tv_quantity_scan);
            etNumberScan = (EditText) v.findViewById(R.id.et_number);
            llMain = v.findViewById(R.id.layout_main);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(LogScanStagesWindowModel item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanStagesWindowModel item, int number);
    }

    public interface OnErrorListener {
        void onError(LogScanStagesWindowModel item, int number, String message);
    }
}