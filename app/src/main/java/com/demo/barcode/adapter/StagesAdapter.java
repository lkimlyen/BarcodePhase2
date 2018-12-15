package com.demo.barcode.adapter;

import android.graphics.Paint;
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

import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class StagesAdapter extends RealmBaseAdapter<LogScanStages> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onOpenDetailListener onOpenDetailListener;
    private onClickEditTextListener onClickEditTextListener;

    public StagesAdapter(OrderedRealmCollection<LogScanStages> realmResults, OnItemClearListener listener,
                         OnEditTextChangeListener onEditTextChangeListener, StagesAdapter.onErrorListener onErrorListener, StagesAdapter.onOpenDetailListener onOpenDetailListener, StagesAdapter.onClickEditTextListener onClickEditTextListener) {
        super(realmResults);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onOpenDetailListener = onOpenDetailListener;
        this.onClickEditTextListener = onClickEditTextListener;
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

    boolean edit = false;

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
                        holder.edtNumberScan.setText((int) item.getNumberInput() + "");
                        onErrorListener.errorListener(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput - item.getNumberInput() > numberInputModel.getNumberRest()) {
                        onErrorListener.errorListener(item, numberInput, null);
                        return;
                    }
                    if (numberInput == item.getNumberInput()) {
                        return;
                    }
//                    edit = false;
//                    final Timer timer = new Timer();
//                    if (!edit) {
//                        edit = true;
//                        timer.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                edit = false;
//                                timer.cancel();
//                            }
//                        }, 1000);
//                    } else {
//                        timer.cancel();
                    onEditTextChangeListener.onEditTextChange(item, numberInput);

//                    }


                } catch (Exception e) {

                }
            }
        };
        holder.txtBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductDetail().getProductName());
        holder.txtModule.setText(item.getModule());
        holder.txtQuantityProduct.setText((int) numberInputModel.getNumberTotal() + "");
        holder.txtQuantityRest.setText((int) numberInputModel.getNumberRest() + "");
        holder.txtQuantityScan.setText((int) numberInputModel.getNumberSuccess() + "");
        holder.edtNumberScan.setText(String.valueOf((int) item.getNumberInput()));
        holder.edtNumberScan.setSelection(holder.edtNumberScan.getText().length());

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

        if (numberInputModel.getNumberRest() > 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
        } else if (numberInputModel.getNumberRest() == 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

        } else {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorYellow));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
        }

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

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
