package com.demo.barcode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;
import com.demo.barcode.widgets.AnimatedExpandableListView;

import java.util.HashSet;
import java.util.Set;

import io.realm.RealmResults;

public class GroupCodeAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private RealmResults<ListGroupCode> list;
    private Set<ListGroupCode> countersToSelect = new HashSet<ListGroupCode>();
    private ListGroupCode ListGroupCodeSelect = null;


    public ListGroupCode getListGroupCodeSelect() {
        return ListGroupCodeSelect;
    }

    private OnItemClearListener onItemClearListener;

    public GroupCodeAdapter(Context context, RealmResults<ListGroupCode> list, OnItemClearListener onItemClearListener) {
        this.context = context;
        this.list = list;
        this.onItemClearListener = onItemClearListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.list.get(groupPosition).getList().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

        final ListGroupCode listScan = (ListGroupCode) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_group_code_content, null);
        }

        GroupCodeContentAdapter adapter = new GroupCodeContentAdapter(listScan.getList(), new GroupCodeContentAdapter.OnRemoveListener() {
            @Override
            public void onRemove(LogScanStages item) {
                onItemClearListener.onItemClick(listScan,item);
            }
        });
        ListView lvScan = (ListView) convertView
                .findViewById(R.id.lv_scan);

        lvScan.setAdapter(adapter);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.list.get(groupPosition).getList()
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.list.get(groupPosition).getGroupCode();
    }

    @Override
    public int getGroupCount() {
        return this.list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ListGroupCode listGroupCode = ((ListGroupCode) getGroup(groupPosition));
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_group_code_header, null);
        }
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
        txtHeader.setText(listGroupCode.getGroupCode());

        CheckBox cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
        RadioButton rbSelect = (RadioButton) convertView.findViewById(R.id.rb_select);
        rbSelect.setVisibility(View.VISIBLE);
        cbSelect.setVisibility(View.GONE);
        rbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListGroupCodeSelect = listGroupCode;
            }
        });
        rbSelect.setChecked(ListGroupCodeSelect.equals(listGroupCode));


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
        void onItemClick(ListGroupCode groupCode,LogScanStages item);
    }

}
