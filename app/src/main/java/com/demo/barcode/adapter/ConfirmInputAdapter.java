package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ConfirmInputAdapter extends RealmRecyclerViewAdapter<LogScanConfirm, ConfirmInputAdapter.HistoryHolder> {
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private OnWarningListener onWarningListener;

    public ConfirmInputAdapter(OrderedRealmCollection<LogScanConfirm> data, OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener, OnWarningListener onWarningListener) {
        super(data, true);
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onWarningListener = onWarningListener;
        setHasStableIds(true);
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_order, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final LogScanConfirm obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, LogScanConfirm item) {
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
                    if (numberInput < 0) {
                        holder.edtNumberReceive.setText(String.valueOf((int) item.getNumberConfirmed()));
                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;
                    }
                    if (numberInput == item.getNumberConfirmed()) {
                        return;
                    }

//                    if (item.getDeliveryNoteModel().getNumberRest() == 0){
//                        holder.edtNumberReceive.setText(String.valueOf((int) item.getNumberConfirmed()));
//                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest).toString());
//                        return;
//                    }
                    int numberResidualDeli = (int) (item.getNumberUsedInTimes() + item.getNumberOut() - item.getNumberTotalOrder());

                    if (numberResidualDeli > 0 && item.getDeliveryNoteModel().getNumberRest() > 0) {
                        if (numberInput - item.getNumberConfirmed() - item.getNumberRestInTimes() > numberResidualDeli ||
                                numberInput - item.getNumberConfirmed() > item.getDeliveryNoteModel().getNumberRest()) {
                            holder.edtNumberReceive.setText(String.valueOf((int) item.getNumberConfirmed()));
                            onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest).toString());
                            return;
                        }
                        if (numberInput - item.getNumberConfirmed() > item.getNumberRestInTimes()) {
                            onWarningListener.warningListener(item, numberInput);
                            return;
                        }
                    } else {
                        if (numberInput - item.getNumberConfirmed() > item.getNumberRestInTimes()) {
                            holder.edtNumberReceive.setText(String.valueOf((int) item.getNumberConfirmed()));
                            onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest).toString());
                            return;
                        }


                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);
                } catch (Exception e) {

                }
            }
        };

        holder.txtSerialModule.setText(item.getModule());
        holder.txtNameDetail.setText(item.getProductDetailName());
        holder.txtNumberDelivery.setText(String.valueOf((int) item.getNumberOut()));
        holder.txtNumberConfirmed.setText(String.valueOf((int) item.getDeliveryNoteModel().getNumberUsed()));
        holder.edtNumberReceive.setText(String.valueOf((int) item.getNumberConfirmed()));

        holder.edtNumberReceive.setSelection(holder.edtNumberReceive.getText().length());

        switch (item.getStatusConfirm()) {
            case Constants.FULL:
                holder.txtStatus.setText(CoreApplication.getInstance().getString(R.string.text_full));
                holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));
                break;

            case Constants.RESIDUAL:
                holder.txtStatus.setText(CoreApplication.getInstance().getString(R.string.text_residual));
                holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorYellow));
                break;
            case Constants.INCOMPLETE:
                holder.txtStatus.setText(CoreApplication.getInstance().getString(R.string.text_incomplete));
                holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.txtStatus.setText("");
                holder.layoutMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
                break;
        }


        holder.edtNumberReceive.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    holder.edtNumberReceive.addTextChangedListener(textWatcher);
                } else {
                    holder.edtNumberReceive.removeTextChangedListener(textWatcher);
                }
            }
        });

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(item);
//            }
//        });

    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView txtSerialModule;
        TextView txtNameDetail;
        //ImageView btnDelete;
        LinearLayout layoutMain;
        TextView txtNumberDelivery;
        EditText edtNumberReceive;
        TextView txtStatus;
        TextView txtNumberConfirmed;

        private HistoryHolder(View v) {
            super(v);
            txtSerialModule = (TextView) v.findViewById(R.id.txt_serial_module);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            layoutMain = (LinearLayout) v.findViewById(R.id.layoutMain);
            txtNumberDelivery = (TextView) v.findViewById(R.id.txt_number_delivery);

            txtNumberConfirmed = (TextView) v.findViewById(R.id.txt_number_confirmed);
            edtNumberReceive = (EditText) v.findViewById(R.id.txt_number_receive);
            txtStatus = (TextView) v.findViewById(R.id.txt_status);
        }
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanConfirm item, int number);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }

    public interface onClickEditTextListener {
        void onClick();
    }

    public interface OnWarningListener {
        void warningListener(LogScanConfirm item, int number);
    }
}
