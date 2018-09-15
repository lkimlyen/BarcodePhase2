package com.demo.barcode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.widgets.AnimatedExpandableListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private List<HistoryEntity> listModule;
    private HashMap<HistoryEntity, List<HashMap<ProductPackagingEntity, PackageEntity>>> listProduct;

    public HistoryAdapter(Context context, List<HistoryEntity> listDataHeader,
                          HashMap<HistoryEntity, List<HashMap<ProductPackagingEntity, PackageEntity>>> listChildData) {
        this.context = context;
        this.listModule = listDataHeader;
        this.listProduct = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listProduct.get(this.listModule.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

        final HashMap<ProductPackagingEntity, PackageEntity> childContent = (HashMap<ProductPackagingEntity, PackageEntity>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
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
        for (Map.Entry<ProductPackagingEntity, PackageEntity> map : childContent.entrySet()) {
            txtSerialPack.setText(map.getValue().getSerialPack());
            txtPackCode.setText(map.getValue().getPackCode());
            txtNameProduct.setText(map.getKey().getProductName());
            txtCodeScan.setText(map.getKey().getBarcode());
            txtNumber.setText(String.valueOf(map.getKey().getNumber()));
        }

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.listProduct.get(this.listModule.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listModule.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listModule.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        HistoryEntity headerContent = (HistoryEntity) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_header_history, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_module);
        lblListHeader.setTypeface(null, Typeface.NORMAL);
        lblListHeader.setText(String.format(CoreApplication.getInstance().getString(R.string.text_module),headerContent.getModule()));
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
