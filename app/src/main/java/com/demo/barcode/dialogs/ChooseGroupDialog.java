package com.demo.barcode.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.GroupCodeEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListGroupManager;
import com.demo.barcode.manager.ListProductManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChooseGroupDialog extends DialogFragment {

    private OnItemSaveListener listener;

    private List<GroupEntity>  list;

    private Set<GroupEntity> countersToSelect = new HashSet<GroupEntity>();
    private List<RadioButton> radioButtonList = new ArrayList<>();

    public void setListener(OnItemSaveListener listener) {
        this.listener = listener;
    }



    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_choose_group);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout layoutContent  =  (LinearLayout) dialog.findViewById(R.id.layoutContent);
        for (GroupEntity groupCodeEntity : list){
            List<ProductGroupEntity> result = groupCodeEntity.getProducGroupList();
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_content_group, null);
            TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_detail);
            RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
            LinearLayout layoutMain = (LinearLayout) v.findViewById(R.id.layoutMain);
            ImageButton btnEdit = (ImageButton) v.findViewById(R.id.btn_edit);
           btnEdit.setVisibility(View.GONE);
            ImageButton btnSave = (ImageButton) v.findViewById(R.id.btn_save);
            btnSave.setVisibility(View.GONE);

            rbSelect.setTag(groupCodeEntity);
            radioButtonList.add(rbSelect);

            rbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // rbSelect.getTag().equals(countersToSelect.iterator().next()))
                    if (countersToSelect.size() > 0) {
                        for (RadioButton radioButton : radioButtonList) {
                            if (radioButton.getTag().equals(countersToSelect.iterator().next()) && !((RadioButton) v).getTag().equals(countersToSelect.iterator().next())) {
                                radioButton.setChecked(false);
                                break;
                            }
                        }
                    }
                    countersToSelect.clear();
                    countersToSelect.add((GroupEntity) rbSelect.getTag());
                }
            });
            txtTitle.setText(groupCodeEntity.getGroupCode());
            for (ProductGroupEntity product : result) {
                View view = inf.inflate(R.layout.item_code_in_group, null);
                TextView txtNameDetail = (TextView) view.findViewById(R.id.txt_name_detail);
                TextView txtNumberGroup = (TextView) view.findViewById(R.id.txt_number_group);
                EditText edtNumberGroup = (EditText) view.findViewById(R.id.edt_number_group);
                TextView txtNumberScan = (TextView) view.findViewById(R.id.txt_number_scan);
                TextView txtModule = (TextView) view.findViewById(R.id.txt_module);
                ImageView imgRemove = (ImageView) view.findViewById(R.id.btn_remove);
                txtNameDetail.setText(product.getProductDetailName());
                txtNumberGroup.setText(String.valueOf((int)product.getNumber()));
                edtNumberGroup.setText(String.valueOf((int)product.getNumber()));
                edtNumberGroup.setTag(product);
                txtNumberScan.setTag(product);
                txtModule.setText(product.getModule());
                txtNumberScan.setText(String.valueOf((int)product.getNumberTotal()));
                imgRemove.setVisibility(View.GONE);
                txtNumberGroup.setVisibility(View.GONE);
                txtNumberScan.setVisibility(View.GONE);
                layoutMain.addView(view);
            }
            layoutContent.addView(v);
        }

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button button = (Button)dialog.findViewById(R.id.btn_save1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(), "Bạn chưa chọn nhóm!", Toast.LENGTH_SHORT).show();
                if (countersToSelect.size() == 0){
                    Toast.makeText(getActivity(), "Bạn chưa chọn nhóm!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();
                listener.onSave(countersToSelect.iterator().next());
            }
        });

        return dialog;
    }

    public void setList(List<GroupEntity> list) {
        this.list = list;
    }

    public interface OnItemSaveListener {
        void onSave(GroupEntity groupEntity);
    }

}
