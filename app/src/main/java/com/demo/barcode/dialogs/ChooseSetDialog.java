package com.demo.barcode.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.barcode.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseSetDialog extends DialogFragment {

    private OnItemSaveListener listener;

    private List<GroupSetEntity>  list;

    private Set<GroupSetEntity> countersToSelect = new HashSet<GroupSetEntity>();
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
        for (GroupSetEntity item : list){
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_content_group, null);
            TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvTitle.setText(getString(R.string.text_choose_group_set_widow));
            TextView tvNameSet = (TextView) v.findViewById(R.id.txt_name_detail);
            RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
            LinearLayout layoutMain = (LinearLayout) v.findViewById(R.id.layoutMain);
            layoutMain.setVisibility(View.GONE);
            ImageButton btnEdit = (ImageButton) v.findViewById(R.id.btn_edit);
           btnEdit.setVisibility(View.GONE);
            ImageButton btnSave = (ImageButton) v.findViewById(R.id.btn_save);
            btnSave.setVisibility(View.GONE);
            rbSelect.setTag(item);
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
                    countersToSelect.add((GroupSetEntity) rbSelect.getTag());
                }
            });
            tvNameSet.setText(item.getPackCode() + "-" + item.getNumberSetOnPack());

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

    public void setList(List<GroupSetEntity> list) {
        this.list = list;
    }

    public interface OnItemSaveListener {
        void onSave(GroupSetEntity groupEntity);
    }

}
