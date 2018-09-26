package com.demo.barcode.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.barcode.R;
import com.demo.barcode.manager.ListGroupManager;
import com.demo.barcode.manager.ListProductManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailGroupDialog extends DialogFragment {

    private String groupCode;

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_detail_group);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout layoutContent = (LinearLayout) dialog.findViewById(R.id.layout_content);
        TextView txtGroupCode = (TextView) dialog.findViewById(R.id.txt_group_code);
        txtGroupCode.setText(groupCode);
        GroupEntity groupEntity = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupCode);

        for (ProductGroupEntity productGroupEntity : groupEntity.getProducGroupList()) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_group_code_detail, null);
            TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_product);
            TextView txtNumber = (TextView) v.findViewById(R.id.txt_number);

            txtTitle.setText(productGroupEntity.getProductDetailName());
            txtNumber.setText(String.valueOf(productGroupEntity.getNumber()));
            layoutContent.addView(v);
        }

        return dialog;
    }


    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }


}
