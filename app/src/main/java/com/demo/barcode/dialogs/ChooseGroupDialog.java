package com.demo.barcode.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;
import com.demo.barcode.adapter.GroupCodeContentAdapter;
import com.demo.barcode.manager.ListProductGroupManager;
import com.demo.barcode.manager.ListProductManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;

public class ChooseGroupDialog extends DialogFragment {

    private OnItemSaveListener listener;

    private List<ProductGroupEntity>  list;

    private Set<ProductGroupEntity> countersToSelect = new HashSet<ProductGroupEntity>();
    private List<RadioButton> radioButtonList = new ArrayList<>();

    public void setListener(OnItemSaveListener listener) {
        this.listener = listener;
    }



    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_choose_group);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent_black_hex_11);

        LinearLayout layoutContent  =  (LinearLayout) dialog.findViewById(R.id.layoutContent);
        for (ProductGroupEntity productGroupEntity : list){
            List<ProductGroupEntity> result = ListProductGroupManager.getInstance().getListProductByGroupCode(productGroupEntity.getGroupCode());
            List<ProductEntity> productEntities = new ArrayList<>();
            for (ProductGroupEntity productGroupEntity1 : result){
                ProductEntity productEntity = ListProductManager.getInstance().getProductById(productGroupEntity1.getProductDetailID());
                productEntities.add(productEntity);
            }
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_content_group, null);
            TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_detail);
            RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
            rbSelect.setTag(productGroupEntity);
            radioButtonList.add(rbSelect);
            final ListView lvCode = (ListView) v.findViewById(R.id.lv_code);
            rbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // rbSelect.getTag().equals(countersToSelect.iterator().next()))
                    if (countersToSelect.size() > 0) {
                        for (RadioButton radioButton : radioButtonList) {
                            if (radioButton.getTag().equals(countersToSelect.iterator().next())) {
                                radioButton.setChecked(false);
                                break;
                            }
                        }
                    }
                    countersToSelect.clear();
                    countersToSelect.add((ProductGroupEntity) rbSelect.getTag());

                }
            });

            txtTitle.setText(productGroupEntity.getGroupCode());
            ArrayAdapter<ProductEntity> adapter = new ArrayAdapter<ProductEntity>(getActivity(), R.layout.item_name_detail, productEntities);
//            islandContentAdapter.setListGroupCode(item);
            lvCode.setAdapter(adapter);

            lvCode.post(new Runnable() {
                @Override
                public void run() {
                    setListViewHeightBasedOnItems(lvCode);
                }
            });

//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//        param.gravity = Gravity.CENTER;
//        v.setLayoutParams(param);
            layoutContent.addView(v);
        }

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void setList(List<ProductGroupEntity> list) {
        this.list = list;
    }

    public interface OnItemSaveListener {
        void onSave(ProductGroupEntity productGroupEntity);
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
}
