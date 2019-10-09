package com.demo.barcode.screen.detail_error;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class DetailErrorModule {
    private final DetailErrorContract.View CreateCodePackageView;

    public DetailErrorModule(DetailErrorContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    DetailErrorContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

