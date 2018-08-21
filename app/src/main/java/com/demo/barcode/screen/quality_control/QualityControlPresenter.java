package com.demo.barcode.screen.quality_control;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlPresenter implements QualityControlContract.Presenter {

    private final String TAG = QualityControlPresenter.class.getName();
    private final QualityControlContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    QualityControlPresenter(@NonNull QualityControlContract.View view) {
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


}
