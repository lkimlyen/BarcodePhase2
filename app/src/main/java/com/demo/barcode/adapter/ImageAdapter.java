package com.demo.barcode.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.demo.architect.data.model.offline.ImageModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.dialogs.ViewImageDialog;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ImageAdapter extends RealmRecyclerViewAdapter<ImageModel, ImageAdapter.MyViewHolder> {

    private OnItemClearListener listener;
    private OnViewImageListener onViewImageListener;

    public ImageAdapter(OrderedRealmCollection<ImageModel> data, OnItemClearListener listener, OnViewImageListener onViewImageListener) {
        super(data, true);
        this.listener = listener;
        this.onViewImageListener = onViewImageListener;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ImageModel obj = getItem(position);
        holder.data = obj;
        holder.bind(obj.getPathFile(),onViewImageListener);
        final long itemId = obj.getId();
        File f = new File(obj.getPathFile());
        Bitmap bitmap = BitmapFactory.decodeFile(obj.getPathFile());
        int width = 0;
        int height = 0;
        if (bitmap.getHeight() < bitmap.getWidth()){
            width = 1280;
            height = bitmap.getHeight() / (bitmap.getWidth()/1280);
        }else {
            height = 1280;
            width = bitmap.getWidth() / (bitmap.getHeight()/1280);
        }
        Picasso.with(CoreApplication.getInstance()).load(f).resize(width, height).into(holder.image);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(itemId);
            }
        });

    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton btnDelete;
        public ImageView image;
        public ImageModel data;

        private MyViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.iv_image_error);
            btnDelete = (ImageButton) v.findViewById(R.id.btn_delete);
        }

        private void bind(String filePath,OnViewImageListener onViewImageListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onViewImageListener.OnViewImageClick(filePath);
                }
            });
        }


    }

    public interface OnItemClearListener {
        void onItemClick(long id);
    }

    public interface OnViewImageListener{
        void OnViewImageClick(String filePath);
    }
}
