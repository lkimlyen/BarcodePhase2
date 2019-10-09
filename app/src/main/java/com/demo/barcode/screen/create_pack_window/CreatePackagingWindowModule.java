package com.demo.barcode.screen.create_pack_window;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class CreatePackagingWindowModule {
    private final CreatePackagingWindowContract.View CreatePackagingView;

    public CreatePackagingWindowModule(CreatePackagingWindowContract.View CreatePackagingView) {
        this.CreatePackagingView = CreatePackagingView;
    }

    @Provides
    @NonNull
    CreatePackagingWindowContract.View provideCreatePackagingView() {
        return this.CreatePackagingView;
    }
}

