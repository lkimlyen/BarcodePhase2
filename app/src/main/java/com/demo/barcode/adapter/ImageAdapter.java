package com.demo.barcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.demo.architect.data.model.offline.ImageModel;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ImageAdapter extends RealmRecyclerViewAdapter<ImageModel, ImageAdapter.MyViewHolder> {

    private OnItemClearListener listener;

    public ImageAdapter(OrderedRealmCollection<ImageModel> data, OnItemClearListener listener) {
        super(data, true);
        this.listener = listener;
        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#hasStableIds()
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemId(int)
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
        final int itemId = obj.getId();
        File f = new File(obj.getPathFile());
        Picasso.with(CoreApplication.getInstance()).load(f).into(holder.image);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
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
        public ImageView imgDelete;
        public ImageView image;
        public ImageModel data;

        private MyViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.iv_image_error);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
        }

    }

    public interface OnItemClearListener {
        void onItemClick(int id);
    }

}
