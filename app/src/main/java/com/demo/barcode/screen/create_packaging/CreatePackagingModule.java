package com.demo.barcode.screen.create_packaging;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class CreatePackagingModule {
    private final CreatePackagingContract.View CreatePackagingView;

    public CreatePackagingModule(CreatePackagingContract.View CreatePackagingView) {
        this.CreatePackagingView = CreatePackagingView;
    }

    @Provides
    @NonNull
    CreatePackagingContract.View provideCreatePackagingView() {
        return this.CreatePackagingView;
    }
}

