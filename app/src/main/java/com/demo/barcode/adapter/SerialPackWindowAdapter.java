package com.demo.barcode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class SerialPackWindowAdapter extends RealmBaseAdapter<ListPackCodeWindowModel> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onPrintListener onPrintListener;
    private onClickEditTextListener onClickEditTextListener;
    private boolean refresh;

    public SerialPackWindowAdapter(OrderedRealmCollection<ListPackCodeWindowModel> realmResults, OnItemClearListener listener,
                                   OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener, SerialPackWindowAdapter.onPrintListener onPrintListener, SerialPackWindowAdapter.onClickEditTextListener onClickEditTextListener) {
        super(realmResults);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onPrintListener = onPrintListener;
        this.onClickEditTextListener = onClickEditTextListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_scan, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final ListPackCodeWindowModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, ListPackCodeWindowModel item) {
        holder.txtModule.setText(String.format(CoreApplication.getInstance().getString(R.string.text_pack_code), item.getPackCode()));
        holder.txtSerialPack.setText(String.format(CoreApplication.getInstance().getString(R.string.text_number_pack), item.getNumberSetOnPack()));

        ScanPackWindowAdapter adapter = new ScanPackWindowAdapter(item.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll(),
                new ScanPackWindowAdapter.OnItemClearListener() {
                    @Override
                    public void onItemClick(long logId) {
                        listener.onItemClick(logId, item.getPackCode(), item.getNumberSetOnPack());
                    }
                }, new ScanPackWindowAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(long logId, int number) {
                onEditTextChangeListener.onEditTextChange(logId, number, item.getPackCode(), item.getNumberSetOnPack(), item.getTotalNumber());
            }
        }, new ScanPackWindowAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                onErrorListener.errorListener(message);
            }
        }, new ScanPackWindowAdapter.onClickEditTextListener() {
            @Override
            public void onClick() {
                onClickEditTextListener.onClick();
            }
        }, item.getPackCode(),item.getNumberSetOnPack());

        holder.lvCode.setAdapter(adapter);

        holder.lvCode.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnItems(holder.lvCode);
            }
        });
        int sum = item.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD).findAll().sum("numberInput").intValue();
        if (item.getTotalNumber() == sum) {
            holder.btnPrint.setVisibility(View.VISIBLE);
        } else {
            holder.btnPrint.setVisibility(View.GONE);
        }
        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrintListener.onPrint(item.getId());
            }
        });

    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
        notifyDataSetChanged();
    }

    public class HistoryHolder {

        TextView txtModule;
        TextView txtSerialPack;
        TextView txtCodePack;
        ListView lvCode;
        ImageButton btnPrint;

        private HistoryHolder(View v) {
            lvCode = (ListView) v.findViewById(R.id.lv_code);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
            txtSerialPack = (TextView) v.findViewById(R.id.txt_serial_pack);
            btnPrint = (ImageButton) v.findViewById(R.id.btn_print);
            txtCodePack = (TextView) v.findViewById(R.id.txt_code_pack);
            txtCodePack.setVisibility(View.GONE);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(long logId,String packCode, int numberOnPack);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(long logId, int number,String packCode, int numberOnPack, int totalNumber);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }

    public interface onPrintListener {
        void onPrint(long mainId);
    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public interface onClickEditTextListener {
        void onClick();
    }
}
