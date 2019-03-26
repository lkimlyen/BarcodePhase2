package com.demo.barcode.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListGroupManager;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmRecyclerViewAdapter;

public class GroupCodeLVAdapter extends RealmRecyclerViewAdapter<GroupCode, GroupCodeLVAdapter.HistoryHolder> {

    private boolean inChooseMode = false;
    private Set<GroupCode> countersToSelect = new HashSet<GroupCode>();
    private OnRemoveListener onRemoveListener;
    private onClickEditTextListener onClickEditTextListener;

    public void enableSelectMode(boolean enabled) {
        inChooseMode = enabled;
        if (!enabled) {
            countersToSelect.clear();
        }
        notifyDataSetChanged();
    }

    public Set<GroupCode> getCountersToSelect() {
        return countersToSelect;
    }

    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;

    public GroupCodeLVAdapter(OrderedRealmCollection<GroupCode> realmResults, OnRemoveListener onRemoveListener, GroupCodeLVAdapter.onClickEditTextListener onClickEditTextListener, OnEditTextChangeListener onEditTextChangeListener, GroupCodeLVAdapter.onErrorListener onErrorListener) {
        super(realmResults, true);
        this.onRemoveListener = onRemoveListener;
        this.onClickEditTextListener = onClickEditTextListener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        setHasStableIds(true);
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_code, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final GroupCode obj = getItem(position);
        setDataToViews(holder, obj);

    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
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
                    int numberInput = Integer.parseInt(s.toString());
                    if (numberInput <= 0) {
                        holder.edtNumberGroup.setText((int) item.getNumber() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }

                    if (numberInput + ListGroupManager.getInstance().totalNumberProductGroup(item.getProductDetailId()) > item.getNumberTotal()) {
                        holder.edtNumberGroup.setText((int) item.getNumber() + "");
                        onErrorListener.errorListener(CoreApplication.getInstance().getText(R.string.text_number_group_bigger_number_total).toString());
                        return;
                    }
                    if (numberInput == item.getNumber()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);


                } catch (Exception e) {

                }
            }
        };
        holder.txtNameDetail.setText(item.getProductDetailName());
        holder.edtNumberGroup.setText(String.valueOf((int) item.getNumber()));
        holder.edtNumberGroup.setSelection(holder.edtNumberGroup.getText().length());
        holder.txtNumberTotal.setText(String.valueOf((int) item.getNumberTotal()));
        holder.txtModule.setText(item.getModule());
        holder.edtNumberGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onClickEditTextListener.onClick();
                    holder.edtNumberGroup.addTextChangedListener(textWatcher);
                } else {
                    holder.edtNumberGroup.removeTextChangedListener(textWatcher);
                }
            }
        });

        if (inChooseMode) {
            holder.cbSelect.setChecked(true);
            countersToSelect.add(item);
        } else {
            holder.cbSelect.setChecked(false);
            holder.cbSelect.setOnCheckedChangeListener(null);
        }
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    countersToSelect.add(item);
                } else {
                    countersToSelect.remove(item);
                }

            }
        });

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveListener.onRemove(item.getId());
            }
        });
        // holder.cbSelect.setChecked(countersToSelect.contains(item));
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameDetail;
        EditText edtNumberGroup;
        TextView txtModule;
        TextView txtNumberTotal;
        CheckBox cbSelect;
        ImageView imgRemove;

        private HistoryHolder(View v) {
            super(v);
            txtNameDetail = (TextView) v.findViewById(R.id.txt_name_detail);
            edtNumberGroup = (EditText) v.findViewById(R.id.edt_number);
            txtNumberTotal = (TextView) v.findViewById(R.id.txt_number_total);
            cbSelect = (CheckBox) v.findViewById(R.id.cb_select);
            imgRemove = (ImageView) v.findViewById(R.id.btn_remove);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
        }

    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(GroupCode item, int number);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }

    public interface OnRemoveListener {
        void onRemove(long id);
    }

    public interface onClickEditTextListener {
        void onClick();
    }

}
