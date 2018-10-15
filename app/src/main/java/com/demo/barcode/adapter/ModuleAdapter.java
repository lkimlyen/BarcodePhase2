package com.demo.barcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class ModuleAdapter extends RealmBaseAdapter<LogListModulePagkaging> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private onErrorListener onErrorListener;
    private onPrintListener onPrintListener;
    private onClickEditTextListener onClickEditTextListener;
    private boolean refresh;

    public ModuleAdapter(OrderedRealmCollection<LogListModulePagkaging> realmResults, OnItemClearListener listener,
                         OnEditTextChangeListener onEditTextChangeListener, onErrorListener onErrorListener, ModuleAdapter.onPrintListener onPrintListener, ModuleAdapter.onClickEditTextListener onClickEditTextListener) {
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
                    .inflate(R.layout.item_module, parent, false);
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
        RealmResults<LogListSerialPackPagkaging> list = item.getLogScanPackagingList().where().greaterThan("size", 0).findAll();
       holder.llContent.removeAllViews();
        for (LogListSerialPackPagkaging logListSerialPackPagkaging : list) {
            LayoutInflater inf = (LayoutInflater) CoreApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_header_scan, null);
            TextView txtSerialPack = v.findViewById(R.id.txt_serial_pack);
            TextView txtCodePack = v.findViewById(R.id.txt_code_pack);
            ListView lvCode = v.findViewById(R.id.lv_code);
            ImageButton btnPrint = v.findViewById(R.id.btn_print);
            txtSerialPack.setText(String.format(CoreApplication.getInstance().getString(R.string.text_serial), logListSerialPackPagkaging.getSerialPack()));
            txtCodePack.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_package), logListSerialPackPagkaging.getCodeProduct()));
            ScanPackagingAdapter adapter = new ScanPackagingAdapter(logListSerialPackPagkaging.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll(),
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
            }, new ScanPackagingAdapter.onClickEditTextListener() {
                @Override
                public void onClick() {
                    onClickEditTextListener.onClick();
                }
            });

            lvCode.setAdapter(adapter);

            lvCode.post(new Runnable() {
                @Override
                public void run() {
                    setListViewHeightBasedOnItems(lvCode);
                }
            });

            double sum = logListSerialPackPagkaging.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll().sum("numberInput").doubleValue();
            if (logListSerialPackPagkaging.getNumberTotal() == sum) {
                btnPrint.setVisibility(View.VISIBLE);
            } else {
                btnPrint.setVisibility(View.GONE);
            }
            btnPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPrintListener.onPrint(item.getId(), logListSerialPackPagkaging.getSerialPack());
                }
            });
            holder.llContent.addView(v);
        }


        SerialPackAdapter adapter = new SerialPackAdapter(item.getLogScanPackagingList().where().greaterThan("size", 0).findAll(),
                new SerialPackAdapter.OnItemClearListener() {
                    @Override
                    public void onItemClick(LogScanPackaging item) {
                        listener.onItemClick(item);
                    }
                }, new SerialPackAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanPackaging item, int number) {
                onEditTextChangeListener.onEditTextChange(item, number);
            }
        }, new SerialPackAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                onErrorListener.errorListener(message);
            }
        }, new SerialPackAdapter.onPrintListener() {
            @Override
            public void onPrint(long moduleId,String serialPack) {
                onPrintListener.onPrint(item.getId(), serialPack);
            }
        }, new SerialPackAdapter.onClickEditTextListener() {
            @Override
            public void onClick() {
                onClickEditTextListener.onClick();
            }
        });


    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
        notifyDataSetChanged();
    }

    public class HistoryHolder {

        TextView txtModule;
        LinearLayout llContent;

        private HistoryHolder(View v) {
            llContent = v.findViewById(R.id.ll_content);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
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
        void onPrint(long moduleId, String serialPack);
    }


    public interface onClickEditTextListener {
        void onClick();
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
