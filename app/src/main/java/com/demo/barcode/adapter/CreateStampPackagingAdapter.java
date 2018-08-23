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


import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.widgets.AnimatedExpandableListView;

/**
 * Created by Thinh on 08/08/2017.
 */

public class CreateStampPackagingAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private LogListModulePagkaging list;
    private OnEditTextChangeListener listener;
    private OnItemClearListener onItemClearListener;
    private onErrorListener onErrorListener;
    private boolean open = false;
    private int positionChoose;

    public CreateStampPackagingAdapter(Context context, LogListModulePagkaging list, OnEditTextChangeListener listener, OnItemClearListener onItemClearListener, CreateStampPackagingAdapter.onErrorListener onErrorListener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.onItemClearListener = onItemClearListener;
        this.onErrorListener = onErrorListener;
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

        final LogListSerialPackPagkaging listScan = (LogListSerialPackPagkaging) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_content, null);
        }

        ScanPackagingAdapter adapter = new ScanPackagingAdapter(listScan.getList(),
                new ScanPackagingAdapter.OnItemClearListener() {
                    @Override
                    public void onItemClick(LogScanPackaging item) {
                        onItemClearListener.onItemClick(item);
                    }
                }, new ScanPackagingAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanPackaging item, int number) {
                listener.onEditTextChange(item, number);
            }
        }, new ScanPackagingAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                onErrorListener.errorListener(message);

            }
        });
        ListView lvScan = (ListView) convertView
                .findViewById(R.id.lv_scan);

        lvScan.setAdapter(adapter);

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
        return this.list.getList().get(groupPosition).getSerialPack();
    }

    @Override
    public int getGroupCount() {
        return this.list.getList().size();
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


        TextView txtHeader = (TextView) convertView
                .findViewById(R.id.txt_serial_pack);
        txtHeader.setTypeface(null, Typeface.NORMAL);
        txtHeader.setText(headerTitle);
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


    public interface OnItemClearListener {
        void onItemClick(LogScanPackaging item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanPackaging item, int number);
    }

    public interface onErrorListener {
        void errorListener(String message);
    }
}
