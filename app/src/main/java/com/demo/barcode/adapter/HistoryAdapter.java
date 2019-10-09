package com.demo.barcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryEntity> listModule;
    private OnClickPrintListener listener;

    public HistoryAdapter(Context context, List<HistoryEntity> listDataHeader, OnClickPrintListener listener) {
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
        holder.txtSerialPack.setText(String.format(CoreApplication.getInstance().getString(R.string.text_serial), item.getPackageList().get(0).getSerialPack()));
        holder.txtPackCode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_package), item.getPackageList().get(0).getPackCode()));

        holder.txtDate.setText(item.getDateTime());
        holder.llContent.removeAllViews();
        for (PackageEntity packageEntity : item.getPackageList()) {
            for (ProductPackagingEntity productPackagingEntity : packageEntity.getProductPackagingEntityList()){
                setLayoutGroup(productPackagingEntity, holder);
            }

        }
        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPrint(item);
            }
        });

    }

    private void setLayoutGroup(ProductPackagingEntity productPackagingEntity, HistoryHolder holder) {
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inf.inflate(R.layout.item_content_history, null);
        TextView txtNameProduct = (TextView) convertView
                .findViewById(R.id.txt_name_product);

        TextView txtCodeScan = (TextView) convertView
                .findViewById(R.id.txt_code_scan);

        TextView txtNumber = (TextView) convertView
                .findViewById(R.id.txt_number);


        txtNameProduct.setText(productPackagingEntity.getProductName());
        txtCodeScan.setText(productPackagingEntity.getBarcode());
        txtNumber.setText(String.valueOf(productPackagingEntity.getNumber()));
        holder.llContent.addView(convertView);


    }



    public class HistoryHolder {

        TextView txtModule;
        // ListView lvCode;
        LinearLayout llContent;
        ImageButton btnPrint;
        TextView txtDate;
        TextView txtSerialPack;
        TextView txtPackCode;



        private HistoryHolder(View v) {
            txtSerialPack = (TextView) v
                    .findViewById(R.id.txt_serial_pack);
            txtPackCode = (TextView) v
                    .findViewById(R.id.txt_code_pack);
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
