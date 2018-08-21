package com.demo.barcode.screen.detail_error;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailErrorPresenter implements DetailErrorContract.Presenter {

    private final String TAG = DetailErrorPresenter.class.getName();
    private final DetailErrorContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    DetailErrorPresenter(@NonNull DetailErrorContract.View view) {
        this.view = view;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void addImage(String pathFile) {
        view.showProgressBar();
        localRepository.addImageModel(pathFile).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
           view.hideProgressBar();
           view.showSuccess(CoreApplication.getInstance().getString(R.string.text_add_success));
            }
        });
    }

    @Override
    public void deleteImage(int id) {
        view.showProgressBar();
        localRepository.deleteImageModel(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_image_success));
            }
        });
    }
}
