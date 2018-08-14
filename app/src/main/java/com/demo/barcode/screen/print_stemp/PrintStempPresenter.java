package com.demo.barcode.screen.print_stemp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempPresenter implements PrintStempContract.Presenter {

    private final String TAG = PrintStempPresenter.class.getName();
    private final PrintStempContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view) {
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
