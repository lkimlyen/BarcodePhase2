package com.demo.barcode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.demo.architect.data.model.offline.LogListScanPackagingMain;
import com.demo.architect.data.model.offline.LogListScanPagkaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.barcode.R;
import com.demo.barcode.widgets.AnimatedExpandableListView;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Thinh on 08/08/2017.
 */

public class CreateStampPackagingAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private LogListScanPackagingMain list;
    private boolean open = false;
    private int positionChoose;

    public CreateStampPackagingAdapter(Context context, LogListScanPackagingMain list) {
        this.context = context;
        this.list = list;
    }

    public void setOpen(boolean open, int position) {
        this.open = open;
        this.positionChoose = position;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.list.getList().get(groupPosition).getList().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_content, null);
        }

        ListView lvScan = (ListView) convertView
                .findViewById(R.id.lv_scan);

//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.txt_answer);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.list.getList().get(groupPosition).getList()
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listQuestion.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listQuestion.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_header, null);
        }
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layout_main);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_arrow);

        if (isExpanded) {
            Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotation);
            rotation.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(rotation);
            imageView.setImageResource(R.drawable.arrow_blue);
            // relativeLayout.setBackgroundResource(R.drawable.bg_corner_white_three_background);

        } else {
            Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotation90);
            rotation.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(rotation);
            imageView.setImageResource(R.drawable.arrow_grey);
            //relativeLayout.setBackgroundResource(R.drawable.bg_corner_white_stroke_background);
        }


        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_serial_pack);
        lblListHeader.setTypeface(null, Typeface.NORMAL);
        lblListHeader.setText(headerTitle);
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
