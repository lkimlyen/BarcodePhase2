package com.demo.barcode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import java.util.Map;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class PackagingHistoryAdapter extends BaseAdapter {

    private PackageEntity packageEntity;
    private Context context;

    public PackagingHistoryAdapter(Context context,PackageEntity packageEntity) {
        this.context = context;
        this.packageEntity = packageEntity;
    }

    @Override
    public int getCount() {
        return packageEntity.getProductPackagingEntityList().size();
    }

    @Override
    public Object getItem(int position) {
        return packageEntity.getProductPackagingEntityList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return packageEntity.getProductPackagingEntityList().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_content_history, null);
        }
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

        ProductPackagingEntity productPackagingEntity = (ProductPackagingEntity)getItem(position);

            txtSerialPack.setText(packageEntity.getSerialPack());
            txtPackCode.setText(packageEntity.getPackCode());
            txtNameProduct.setText(productPackagingEntity.getProductName());
            txtCodeScan.setText(productPackagingEntity.getBarcode());
            txtNumber.setText(String.valueOf((int) productPackagingEntity.getNumber()));


        return convertView;
    }


}
