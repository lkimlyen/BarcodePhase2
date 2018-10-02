package com.demo.barcode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.widgets.AnimatedExpandableListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter2 extends BaseAdapter {
    private Context context;
    private List<HistoryEntity> listModule;
    private OnClickPrintListener listener;

    public HistoryAdapter2(Context context, List<HistoryEntity> listDataHeader, OnClickPrintListener listener) {
        this.context = context;
        this.listModule = listDataHeader;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return listModule.size();
    }

    @Override
    public Object getItem(int position) {
        return listModule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listModule.get(position).getProductId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_history, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        final HistoryEntity item = (HistoryEntity) getItem(position);
        setDataToViews(viewHolder, item);
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, HistoryEntity item) {
        holder.txtModule.setText(String.format(CoreApplication.getInstance().getString(R.string.text_module), item.getModule()));
        holder.txtDate.setText(item.getDateTime());
        holder.llContent.removeAllViews();
        for (PackageEntity packageEntity : item.getPackageList()) {
            for (ProductPackagingEntity productPackagingEntity : packageEntity.getProductPackagingEntityList()){
                setLayoutGroup(packageEntity,productPackagingEntity, holder);
            }

        }
        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPrint(item);
            }
        });

    }

    private void setLayoutGroup(PackageEntity packageEntity,ProductPackagingEntity productPackagingEntity, HistoryHolder holder) {
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inf.inflate(R.layout.item_content_history, null);

        TextView txtSerialPack = (TextView) convertView
                .findViewById(R.id.txt_serial_pack);

        TextView txtPackCode = (TextView) convertView
                .findViewById(R.id.txt_pack_code);

        TextView txtNameProduct = (TextView) convertView
                .findViewById(R.id.txt_name_product);

        TextView txtCodeScan = (TextView) convertView
                .findViewById(R.id.txt_code_scan);

        TextView txtNumber = (TextView) convertView
                .findViewById(R.id.txt_number);

        txtSerialPack.setText(packageEntity.getSerialPack());
        txtPackCode.setText(packageEntity.getPackCode());
        txtNameProduct.setText(productPackagingEntity.getProductName());
        txtCodeScan.setText(productPackagingEntity.getBarcode());
        txtNumber.setText(String.valueOf((int) productPackagingEntity.getNumber()));
        holder.llContent.addView(convertView);


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

    public class HistoryHolder {

        TextView txtModule;
        // ListView lvCode;
        LinearLayout llContent;
        ImageButton btnPrint;
        TextView txtDate;


        private HistoryHolder(View v) {
            llContent = (LinearLayout) v.findViewById(R.id.ll_content);
            txtModule = (TextView) v.findViewById(R.id.txt_module);
            btnPrint = (ImageButton) v.findViewById(R.id.btn_print);
            txtDate = (TextView) v
                    .findViewById(R.id.txt_date);
        }

    }


    public interface OnClickPrintListener {
        void onClickPrint(HistoryEntity historyEntity);
    }

}
