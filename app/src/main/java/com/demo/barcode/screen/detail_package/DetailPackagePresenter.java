package com.demo.barcode.screen.detail_package;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackagePresenter implements DetailPackageContract.Presenter {

    private final String TAG = DetailPackagePresenter.class.getName();
    private final DetailPackageContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    DetailPackagePresenter(@NonNull DetailPackageContract.View view) {
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
