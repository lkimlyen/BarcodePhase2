package com.demo.barcode.screen.create_packaging;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class CreatePackagingModule {
    private final CreatePackagingContract.View CreateCodePackageView;

    public CreatePackagingModule(CreatePackagingContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    CreatePackagingContract.View provideCreateCodePackageView() {
        return this.CreateCodePackageView;
    }
}

