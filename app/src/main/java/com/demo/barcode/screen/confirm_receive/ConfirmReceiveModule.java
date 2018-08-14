package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ConfirmReceiveModule {
    private final ConfirmReceiveContract.View ConfirmReceiveView;

    public ConfirmReceiveModule(ConfirmReceiveContract.View ConfirmReceiveView) {
        this.ConfirmReceiveView = ConfirmReceiveView;
    }

    @Provides
    @NonNull
    ConfirmReceiveContract.View provideConfirmReceiveView() {
        return this.ConfirmReceiveView;
    }
}

