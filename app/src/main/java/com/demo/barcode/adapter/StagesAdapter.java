package com.demo.barcode.adapter;


import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class StagesAdapter extends RealmRecyclerViewAdapter<LogScanStages, StagesAdapter.HistoryHolder> {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onOpenDetailListener onOpenDetailListener;
    private onClickEditTextListener onClickEditTextListener;

    public StagesAdapter(OrderedRealmCollection<LogScanStages> realmResults, OnItemClearListener listener,
                         OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener, onOpenDetailListener onOpenDetailListener, onClickEditTextListener onClickEditTextListener) {
        super(realmResults, true);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onOpenDetailListener = onOpenDetailListener;
        this.onClickEditTextListener = onClickEditTextListener;

        setHasStableIds(true);
    }



    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stages, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final LogScanStages obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, LogScanStages item) {
        final ProductDetail productDetail = item.getProductDetail();
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
                        holder.edtNumberScan.setText((int) item.getNumberInput() + "");
                        onErrorListener.errorListener(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput - item.getNumberInput() > productDetail.getNumberRest()) {
                        onErrorListener.errorListener(item, numberInput, null);
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
        holder.txtNameDetail.setText(item.getProductDetail().getProductName());
        holder.txtModule.setText(item.getModule());
        holder.txtQuantityProduct.setText((int) productDetail.getNumberTotal() + "");
        holder.txtQuantityRest.setText((int) productDetail.getNumberRest() + "");
        holder.txtQuantityScan.setText((int) productDetail.getNumberSuccess() + "");
        holder.edtNumberScan.setText(String.valueOf((int) item.getNumberInput()));
        holder.edtNumberScan.setSelection(holder.edtNumberScan.getText().length());

        //kiểm tra item này thuộc loại quét nào nếu là loại quét theo group thì hiển thị thêm nhóm và show tất cả chi tiết nằm trong group này
        if (item.getTypeScan()) {
            holder.llGroup.setVisibility(View.GONE);
            holder.txtCodeGroup.setOnClickListener(null);
        } else {
            holder.llGroup.setVisibility(View.VISIBLE);
            holder.txtCodeGroup.setText(item.getGroupCode());
            holder.txtCodeGroup.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.txtCodeGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenDetailListener.onOpenDetail(item.getGroupCode());
                }
            });
        }
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
                listener.onItemClick(item);
            }
        });

        if (productDetail.getNumberRest() > 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
        } else if (productDetail.getNumberRest() == 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

        } else {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorYellow));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView txtBarcode;
        TextView txtNameDetail;
        TextView txtModule;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        EditText edtNumberScan;
        TextView txtCodeGroup;
        LinearLayout llGroup;
        RelativeLayout llMain;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            txtQuantityProduct = (TextView) v.findViewById(R.id.txt_quantity_product);
            txtQuantityRest = (TextView) v.findViewById(R.id.txt_quantity_rest);
            txtQuantityScan = (TextView) v.findViewById(R.id.txt_quantity_scan);
            edtNumberScan = (EditText) v.findViewById(R.id.edt_number);
            txtCodeGroup = (TextView) v.findViewById(R.id.txt_group_code);
            llGroup = v.findViewById(R.id.layout_group);
            llMain = v.findViewById(R.id.layout_main);
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

    public interface onOpenDetailListener {
        void onOpenDetail(String groupCode);
    }

    public interface onClickEditTextListener {
        void onClick();
    }
}