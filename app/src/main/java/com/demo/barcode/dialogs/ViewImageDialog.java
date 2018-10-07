package com.demo.barcode.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.demo.architect.data.model.GroupEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ViewImageDialog extends DialogFragment {


    private String filePath;


    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_view_image);
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout layoutMain = (RelativeLayout) dialog.findViewById(R.id.layout_main);
        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView ivImage = dialog.findViewById(R.id.iv_image);
        File f = new File(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int width = 0;
        int height = 0;
        if (bitmap.getHeight() < bitmap.getWidth()){
            width = 1280;
            height = bitmap.getHeight() / (bitmap.getWidth()/1280);
        }else {
            height = 1280;
            width = bitmap.getWidth() / (bitmap.getHeight()/1280);
        }
        Picasso.with(CoreApplication.getInstance()).load(f).into(ivImage);

        return dialog;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}
