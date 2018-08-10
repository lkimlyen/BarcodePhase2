package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.GetInputForProductDetail;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmReceivePresenter implements ConfirmReceiveContract.Presenter {

    private final String TAG = ConfirmReceivePresenter.class.getName();
    private final ConfirmReceiveContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmReceivePresenter(@NonNull ConfirmReceiveContract.View view) {
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
