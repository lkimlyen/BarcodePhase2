package com.demo.barcode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class ModuleAdapter extends RealmBaseAdapter<LogListModulePagkaging> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onPrintListener onPrintListener;

    private boolean refresh;

    public ModuleAdapter(OrderedRealmCollection<LogListModulePagkaging> realmResults, OnItemClearListener listener,
                         OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener, ModuleAdapter.onPrintListener onPrintListener) {
        super(realmResults);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.onErrorListener = onErrorListener;
        this.onPrintListener = onPrintListener;
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
            final LogListModulePagkaging item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogListModulePagkaging item) {
        holder.txtModule.setText(String.format(CoreApplication.getInstance().getString(R.string.text_module), item.getModule()));
        ScanPackagingAdapter adapter = new ScanPackagingAdapter(item.getLogScanPackagingList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll(),
                new ScanPackagingAdapter.OnItemClearListener() {
                    @Override
                    public void onItemClick(LogScanPackaging item) {
                        listener.onItemClick(item);
                    }
                }, new ScanPackagingAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanPackaging item, int number) {
                onEditTextChangeListener.onEditTextChange(item, number);
            }
        }, new ScanPackagingAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                onErrorListener.errorListener(message);
            }
        });

        holder.lvCode.setAdapter(adapter);

        holder.lvCode.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnItems(holder.lvCode);
            }
        });
        if (refresh) {
            setListViewHeightBasedOnItems(holder.lvCode);
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
        ListView lvCode;
        ImageButton btnPrint;

        private HistoryHolder(View v) {
            lvCode = (ListView) v.findViewById(R.id.lv_code);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
            btnPrint = (ImageButton) v.findViewById(R.id.btn_print);
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

    public interface onPrintListener {
        void onPrint(long module);
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
}
