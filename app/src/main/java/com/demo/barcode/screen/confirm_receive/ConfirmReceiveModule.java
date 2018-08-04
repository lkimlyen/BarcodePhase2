package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ConfirmReceiveModule {
    private final ConfirmReceiveContract.View CreateCodePackageView;

    public ConfirmReceiveModule(ConfirmReceiveContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    ConfirmReceiveContract.View provideCreateCodePackageView() {
        return this.CreateCodePackageView;
    }
}

