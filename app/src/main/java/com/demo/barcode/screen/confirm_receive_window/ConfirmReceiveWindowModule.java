package com.demo.barcode.screen.confirm_receive_window;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ConfirmReceiveWindowModule {
    private final ConfirmReceiveWindowContract.View ConfirmReceiveView;

    public ConfirmReceiveWindowModule(ConfirmReceiveWindowContract.View ConfirmReceiveView) {
        this.ConfirmReceiveView = ConfirmReceiveView;
    }

    @Provides
    @NonNull
    ConfirmReceiveWindowContract.View provideConfirmReceiveView() {
        return this.ConfirmReceiveView;
    }
}

