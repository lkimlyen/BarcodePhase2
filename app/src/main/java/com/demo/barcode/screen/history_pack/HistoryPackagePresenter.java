package com.demo.barcode.screen.history_pack;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackagePresenter implements HistoryPackageContract.Presenter {

    private final String TAG = HistoryPackagePresenter.class.getName();
    private final HistoryPackageContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    HistoryPackagePresenter(@NonNull HistoryPackageContract.View view) {
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
